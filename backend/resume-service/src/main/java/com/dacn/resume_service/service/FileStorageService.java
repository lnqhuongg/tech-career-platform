package com.dacn.resume_service.service;

import com.dacn.resume_service.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir:./uploads/resumes}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new StorageException("Không thể tạo thư mục lưu trữ file: " + uploadDir, ex);
        }
    }

    /**
     * Lưu trữ file tải lên vào hệ thống file cục bộ và trả về tên file duy nhất.
     */
    public String storeFile(MultipartFile file, UUID userId) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (originalFileName.contains("..")) {
            throw new StorageException("Tên file chứa ký tự không hợp lệ: " + originalFileName);
        }

        String extension = "";
        int i = originalFileName.lastIndexOf('.');
        if (i > 0) {
            extension = originalFileName.substring(i);
        }

        // Đặt tên file duy nhất: {userId}_{uuid}{ext}
        String storedFileName = userId + "_" + UUID.randomUUID() + extension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return storedFileName;
        } catch (IOException ex) {
            throw new StorageException("Không thể lưu file: " + originalFileName, ex);
        }
    }

    /**
     * Nạp file dưới dạng Resource để xem trước hoặc tải về.
     */
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("File không tồn tại hoặc không thể đọc: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new StorageException("Đường dẫn file không hợp lệ: " + fileName, ex);
        }
    }

    /**
     * Xóa file khỏi ổ đĩa.
     */
    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ignored) {
            // Không ngắt tiến trình nếu việc xóa file vật lý thất bại
        }
    }
}
