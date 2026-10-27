package com.dacn.job_service.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobCategoryResponse {
    private UUID id;
    private String name;
}
