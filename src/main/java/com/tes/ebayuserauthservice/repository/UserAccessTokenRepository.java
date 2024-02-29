package com.tes.ebayuserauthservice.repository;

import com.tes.ebayuserauthservice.model.UserAccessTokenEntity;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAccessTokenRepository extends JpaRepository<UserAccessTokenEntity, Long> {
    List<UserAccessTokenEntity> findAllByCreationDate(Date creationDate);

    List<UserAccessTokenEntity> findAllByCreationDateBetween(
            Date creationTimeStart,
            Date creationTimeEnd
    );

    @Query("select a from UserAuthCodeEntity a where a.creationDate <= :creationDate")
    List<UserAccessTokenEntity> findAllWithCreationDateTimeBefore(
            @Param("creationDate") Date creationDate);

    Optional<UserAccessTokenEntity> findFirstByOrderByCreationDateDesc();
}
