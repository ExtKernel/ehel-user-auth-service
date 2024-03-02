package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.exception.NoRecordOfRefreshTokenException;
import com.tes.ebayuserauthservice.model.UserRefreshTokenEntity;
import com.tes.ebayuserauthservice.repository.UserRefreshTokenRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class UserRefreshTokenService {
    @Autowired
    private UserRefreshTokenRepository repository;

    @Lazy
    @Autowired
    private UserAuthUtils authUtils;

    @Lazy
    @Autowired
    EbayTokenRetriever tokenRetriever;

    public UserRefreshTokenEntity generateAndSaveRefreshToken() {
        return save(generateRefreshToken());
    }

    public UserRefreshTokenEntity generateRefreshToken() {
        return authUtils.stripRefreshTokensFromEbayJsonMap(tokenRetriever.exchangeUserCodeForRefreshToken());
    }

    public UserRefreshTokenEntity save(UserRefreshTokenEntity entity) {
        return repository.save(entity);
    }

    @Cacheable(cacheNames = "RefreshTokenCache")
    public List<UserRefreshTokenEntity> findAllByCreationDate(Date creationDate) {
        return repository.findAllByCreationDate(creationDate);
    }

    @Cacheable(cacheNames = "RefreshTokenCache")
    public List<UserRefreshTokenEntity> findAllByCreationTimeBetween(
            Date creationTimeStart,
            Date creationTimeEnd
    ) {
        return repository.findAllByCreationDateBetween(creationTimeStart, creationTimeEnd);
    }

    @Cacheable(cacheNames = "RefreshTokenCache")
    public List<UserRefreshTokenEntity> findAllWithCreationDateTimeBefore(Date creationDateTime) {
        return repository.findAllWithCreationDateTimeBefore(creationDateTime);
    }

    @Cacheable(cacheNames = "RefreshTokenCache")
    public UserRefreshTokenEntity findNewest() {
        if (repository.findFirstByOrderByCreationDateDesc().isPresent()) {
            return repository.findFirstByOrderByCreationDateDesc().get();
        } else {
            throw new
                    NoRecordOfRefreshTokenException
                    ("The latest saved refresh token was not found, " +
                            "because no record exists in the database");
        }
    }
}
