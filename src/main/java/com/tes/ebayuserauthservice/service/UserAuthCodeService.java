package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.model.UserAuthCodeEntity;
import com.tes.ebayuserauthservice.repository.UserAuthCodeRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAuthCodeService {

    @Autowired
    private UserAuthCodeRepository repository;

    public Optional<UserAuthCodeEntity> save(UserAuthCodeEntity entity) {
        return Optional.of(repository.save(entity));
    }

    public List<UserAuthCodeEntity> findAllByCreationDate(Date creationDate) {
        return repository.findAllByCreationDate(creationDate);
    }

    public List<UserAuthCodeEntity> findAllByCreationTimeBetween(
            Date creationTimeStart,
            Date creationTimeEnd
    ) {
        return repository.findAllByCreationTimeBetween(creationTimeStart, creationTimeEnd);
    }

    public List<UserAuthCodeEntity> findAllWithCreationDateTimeBefore(Date creationDateTime) {
        return repository.findAllWithCreationDateTimeBefore(creationDateTime);
    }

    public Optional<UserAuthCodeEntity> findNewest() {
        return repository.findFirstByOrderByCreationDateDesc();
    }
}
