package com.lemon.mdcord.repository;

import com.lemon.mdcord.domain.chat.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {
}
