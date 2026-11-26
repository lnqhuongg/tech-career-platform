package com.dacn.resume_service.client;

import com.dacn.resume_service.dto.ai.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AiServiceClient {

    private static final Logger log = LoggerFactory.getLogger(AiServiceClient.class);

    private final RestClient restClient;

    public AiServiceClient(@Value("${app.ai-service.url:http://localhost:8000}") String aiServiceUrl) {
        log.info("Khởi tạo AiServiceClient kết nối đến: {}", aiServiceUrl);
        this.restClient = RestClient.builder()
                .baseUrl(aiServiceUrl)
                .build();
    }

    public AiParseResumeResponse parseResume(String rawText) {
        try {
            return restClient.post()
                    .uri("/ai/parse-resume")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(AiParseResumeRequest.builder().rawText(rawText).build())
                    .retrieve()
                    .body(AiParseResumeResponse.class);
        } catch (Exception e) {
            log.warn("Không thể gọi /ai/parse-resume trên ai-service ({}). Chuyển sang fallback nội bộ.", e.getMessage());
            return null;
        }
    }

    public AiJobMatchResponse matchJob(AiJobMatchRequest request) {
        try {
            return restClient.post()
                    .uri("/ai/match-job")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(AiJobMatchResponse.class);
        } catch (Exception e) {
            log.warn("Không thể gọi /ai/match-job trên ai-service ({}). Chuyển sang fallback nội bộ.", e.getMessage());
            return null;
        }
    }

    public AiChatResponse chat(AiChatRequest request) {
        try {
            return restClient.post()
                    .uri("/ai/chat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(AiChatResponse.class);
        } catch (Exception e) {
            log.warn("Không thể gọi /ai/chat trên ai-service ({}).", e.getMessage());
            return AiChatResponse.builder()
                    .reply("Trợ lý AI hiện đang bận hoặc gián đoạn kết nối. Bạn hãy kiểm tra lại ai-service trên cổng 8000.")
                    .build();
        }
    }

    public AiCvAnalysisResponse analyzeCv(AiCvAnalysisRequest request) {
        try {
            return restClient.post()
                    .uri("/ai/analyze-cv")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(AiCvAnalysisResponse.class);
        } catch (Exception e) {
            log.warn("Không thể gọi /ai/analyze-cv trên ai-service ({}). Chuyển sang kết quả dự phòng.", e.getMessage());
            return AiCvAnalysisResponse.builder()
                    .overallScore(75)
                    .formatScore(80)
                    .contentScore(70)
                    .strengths(java.util.List.of("Hồ sơ có bố cục cơ bản tương đối đầy đủ"))
                    .improvements(java.util.List.of("Nên bổ sung thêm số liệu định lượng cho các dự án"))
                    .actionableAdvice("Hãy rà soát lại các mục trong hồ sơ và cập nhật thêm các kỹ năng chuyên sâu.")
                    .build();
        }
    }
}
