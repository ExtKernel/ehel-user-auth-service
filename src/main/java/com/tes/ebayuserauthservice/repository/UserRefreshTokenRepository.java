package com.tes.ebayuserauthservice.repository;

import com.tes.ebayuserauthservice.model.UserRefreshTokenEntity;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshTokenEntity, Long> {
    List<UserRefreshTokenEntity> findAllByCreationDate(Date creationDate);

    List<UserRefreshTokenEntity> findAllByCreationDateBetween(
            Date creationTimeStart,
            Date creationTimeEnd
    );

    @Query("select a from UserAuthCodeEntity a where a.creationDate <= :creationDate")
    List<UserRefreshTokenEntity> findAllWithCreationDateTimeBefore(
            @Param("creationDate") Date creationDate);

    Optional<UserRefreshTokenEntity> findFirstByOrderByCreationDateDesc();
}
