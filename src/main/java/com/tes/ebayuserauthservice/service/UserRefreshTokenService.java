package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.model.UserRefreshTokenEntity;
import com.tes.ebayuserauthservice.repository.UserRefreshTokenRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class UserRefreshTokenService {
    @Autowired
    private UserRefreshTokenRepository repository;

    @Autowired
    private UserAuthCodeService authCodeService;

    @Lazy
    @Autowired
    private UserAuthUtils authUtils;

    @Lazy
    @Autowired
    EbayTokenRetriever tokenRetriever;

    public Optional<UserRefreshTokenEntity> generateAndSaveRefreshToken() {
        return save(generateRefreshToken());
    }

    public UserRefreshTokenEntity generateRefreshToken() {
        if (authCodeService.findNewest().isPresent()) {
            return authUtils.stripRefreshTokensFromEbayJsonMap(tokenRetriever.exchangeUserCodeForRefreshToken());
        }

        return null; // catch potential exceptions
    }

    public Optional<UserRefreshTokenEntity> save(UserRefreshTokenEntity entity) {
        return Optional.of(repository.save(entity));
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
    public Optional<UserRefreshTokenEntity> findNewest() {
        return repository.findFirstByOrderByCreationDateDesc();
    }
}
