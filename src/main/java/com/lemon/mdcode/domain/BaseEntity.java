package com.lemon.mdcode.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @NotBlank
    @Column(name = "create_by")
    private String createBy;

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
