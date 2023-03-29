package com.lemon.mdcord.repository;

import com.lemon.mdcord.domain.chat.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {
    Optional<AttachFile> findByRealFileName(String fileName);
}
