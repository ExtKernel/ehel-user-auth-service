package com.tes.ebayuserauthservice.repository;

import com.tes.ebayuserauthservice.model.AccessToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
    Optional<AccessToken> findFirstByOrderByCreationDateDesc();
}
