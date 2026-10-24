package com.dacn.job_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SavedJobId implements Serializable {

    @Column(name = "job_seeker_id", nullable = false)
    private UUID jobSeekerId;

    @Column(name = "job_id", nullable = false)
    private UUID jobId;
}
