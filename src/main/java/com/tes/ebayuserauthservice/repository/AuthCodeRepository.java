package com.tes.ebayuserauthservice.repository;

import com.tes.ebayuserauthservice.model.AuthCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodeRepository
        extends JpaRepository<AuthCode, Long> {
    Optional<AuthCode> findFirstByOrderByCreationDateDesc();
}
