package org.swp.entity;

import lombok.Data;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Column;
@Data
@MappedSuperclass
public class BaseEntity {
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime = LocalDateTime.now();
    @Column(name = "modified_time")
    private LocalDateTime modifiedTime;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

}
