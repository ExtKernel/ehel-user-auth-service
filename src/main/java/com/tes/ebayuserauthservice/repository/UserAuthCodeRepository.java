package com.tes.ebayuserauthservice.repository;

import com.tes.ebayuserauthservice.model.UserAuthCodeEntity;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAuthCodeRepository
        extends JpaRepository<UserAuthCodeEntity, Long> {

    List<UserAuthCodeEntity> findAllByCreationDate(Date creationDate);

    List<UserAuthCodeEntity> findAllByCreationDateBetween(
            Date creationTimeStart,
            Date creationTimeEnd
    );

    @Query("select a from UserAuthCodeEntity a where a.creationDate <= :creationDate")
    List<UserAuthCodeEntity> findAllWithCreationDateTimeBefore(
            @Param("creationDate") Date creationDate);

    Optional<UserAuthCodeEntity> findFirstByOrderByCreationDateDesc();
}
