package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.exception.NoRecordOfAuthCodeException;
import com.tes.ebayuserauthservice.model.UserAuthCodeEntity;
import com.tes.ebayuserauthservice.repository.UserAuthCodeRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserAuthCodeService {
    @Autowired
    private UserAuthCodeRepository repository;

    public UserAuthCodeEntity save(UserAuthCodeEntity entity) {
        return repository.save(entity);
    }

    @Cacheable(cacheNames = "AuthCodeCache")
    public List<UserAuthCodeEntity> findAllByCreationDate(Date creationDate) {
        return repository.findAllByCreationDate(creationDate);
    }

    @Cacheable(cacheNames = "AuthCodeCache")
    public List<UserAuthCodeEntity> findAllByCreationTimeBetween(
            Date creationTimeStart,
            Date creationTimeEnd
    ) {
        return repository.findAllByCreationDateBetween(creationTimeStart, creationTimeEnd);
    }

    @Cacheable(cacheNames = "AuthCodeCache")
    public List<UserAuthCodeEntity> findAllWithCreationDateTimeBefore(Date creationDateTime) {
        return repository.findAllWithCreationDateTimeBefore(creationDateTime);
    }

    @Cacheable(cacheNames = "AuthCodeCache")
    public UserAuthCodeEntity findNewest() {
        if (repository.findFirstByOrderByCreationDateDesc().isPresent()) {
            return repository.findFirstByOrderByCreationDateDesc().get();
        } else {
            throw new
                    NoRecordOfAuthCodeException
                    ("The latest saved authorization code was not found, " +
                    "because no record exists in the database");
        }
    }
}
