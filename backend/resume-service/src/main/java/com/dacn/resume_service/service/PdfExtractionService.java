package com.dacn.resume_service.service;

import com.dacn.resume_service.dto.request.SaveParsedDataRequest;
import com.dacn.resume_service.exception.StorageException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service bóc tách nội dung thô từ file PDF sử dụng Apache PDFBox,
 * đồng thời phân tích cơ bản (Regex/Keywords) để sinh ra bản nháp (Draft) cho ứng viên.
 */
@Service
public class PdfExtractionService {

    // Danh sách từ khóa kỹ năng CNTT phổ biến phục vụ trích xuất nhanh trước khi gọi AI chuyên sâu
    private static final List<String> COMMON_SKILLS = Arrays.asList(
            "Java", "Spring Boot", "Spring", "Hibernate", "Python", "Django", "Flask",
            "JavaScript", "TypeScript", "React", "Vue", "Angular", "Node.js", "Express",
            "HTML", "CSS", "Tailwind CSS", "Bootstrap",
            "C#", ".NET", "C++", "PHP", "Laravel", "Golang",
            "PostgreSQL", "MySQL", "MongoDB", "Redis", "Oracle",
            "Docker", "Kubernetes", "AWS", "Azure", "GCP", "CI/CD", "Git", "GitHub", "GitLab",
            "Linux", "RESTful API", "GraphQL", "Microservices", "Kafka", "RabbitMQ",
            "Figma", "UI/UX", "Machine Learning", "NLP", "Deep Learning", "TensorFlow", "PyTorch"
    );

    /**
     * Trích xuất toàn bộ văn bản thô từ file PDF.
     */
    public String extractTextFromPdf(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            if (document.isEncrypted()) {
                throw new StorageException("File PDF có mật khẩu bảo vệ, vui lòng gỡ mật khẩu trước khi tải lên");
            }

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            return stripper.getText(document).trim();

        } catch (IOException e) {
            throw new StorageException("Lỗi khi đọc và bóc tách văn bản từ file PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Phân tích sơ bộ từ văn bản thô để điền sẵn vào bản nháp JSON cho ứng viên xem xét.
     */
    public SaveParsedDataRequest generateDraftParsedData(String rawText) {
        String email = extractEmail(rawText);
        String phone = extractPhone(rawText);
        List<String> skills = extractSkills(rawText);

        return SaveParsedDataRequest.builder()
                .email(email)
                .phone(phone)
                .extractedSkills(skills)
                .educations(new ArrayList<>())
                .experiences(new ArrayList<>())
                .certifications(new ArrayList<>())
                .totalExperienceMonths(0)
                .summary(extractSummaryPreview(rawText))
                .build();
    }

    private String extractEmail(String text) {
        Matcher matcher = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}").matcher(text);
        return matcher.find() ? matcher.group(0) : null;
    }

    private String extractPhone(String text) {
        Matcher matcher = Pattern.compile("(\\+?84|0)(3|5|7|8|9)[0-9]{8}").matcher(text);
        return matcher.find() ? matcher.group(0) : null;
    }

    private List<String> extractSkills(String text) {
        Set<String> matched = new LinkedHashSet<>();
        String lowerText = " " + text.toLowerCase() + " ";
        for (String skill : COMMON_SKILLS) {
            // Kiểm tra theo ranh giới từ để tránh match nhầm (VD: "C" trong "CSS")
            Pattern pattern = Pattern.compile("(?i)\\b" + Pattern.quote(skill) + "\\b");
            if (pattern.matcher(lowerText).find()) {
                matched.add(skill);
            }
        }
        return new ArrayList<>(matched);
    }

    private String extractSummaryPreview(String text) {
        if (text == null || text.isBlank()) return "";
        String[] lines = text.split("\\r?\\n");
        StringBuilder preview = new StringBuilder();
        for (int i = 0; i < Math.min(lines.length, 5); i++) {
            if (!lines[i].isBlank()) {
                preview.append(lines[i].trim()).append(" ");
            }
        }
        String result = preview.toString().trim();
        return result.length() > 300 ? result.substring(0, 300) + "..." : result;
    }
}
