package com.lemon.mdcode.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(name = "create_by")
    private String createBy;

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
