package com.tes.ebayuserauthservice.repository;

import com.tes.ebayuserauthservice.model.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findFirstByOrderByCreationDateDesc();
}
