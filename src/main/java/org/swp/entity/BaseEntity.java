package org.swp.entity;

import lombok.Data;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;

@Data
@MappedSuperclass
public class BaseEntity {
    @Column(name = "created_time", nullable = true, updatable = true)
    private LocalDateTime createdTime;
    @Column(name = "modified_time", nullable = true, updatable = true)
    private LocalDateTime modifiedTime;
    @Column(name = "created_by", nullable = true, updatable = true)
    private String createdBy;
    @Column(name = "modified_by", nullable = true, updatable = true)
    private String modifiedBy;
    @Column(name = "is_deleted", nullable = true, updatable = true)
    private boolean isDeleted;
}
