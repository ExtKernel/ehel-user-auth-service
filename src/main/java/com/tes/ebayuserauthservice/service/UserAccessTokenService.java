package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.model.UserAccessTokenEntity;
import com.tes.ebayuserauthservice.repository.UserAccessTokenRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class UserAccessTokenService {
    @Autowired
    private UserAccessTokenRepository repository;

    @Autowired
    private UserAuthCodeService authCodeService;

    @Lazy
    @Autowired
    private UserAuthUtils authUtils;

    @Lazy
    @Autowired
    private EbayTokenRetriever tokenRetriever;

    public UserAccessTokenEntity renewAccessToken() {
        return authUtils.stripAccessTokensFromEbayJsonMap(tokenRetriever.renewAccessToken());
    }

    public Optional<UserAccessTokenEntity> generateAndSaveAccessToken() {
        return save(generateAccessToken());
    }

    private UserAccessTokenEntity generateAccessToken() {
        if (authCodeService.findNewest().isPresent()) {
            return authUtils.stripAccessTokensFromEbayJsonMap(tokenRetriever.exchangeUserCodeForRefreshToken());
        }

        return null; // catch potential exceptions
    }

    public Optional<UserAccessTokenEntity> save(UserAccessTokenEntity entity) {
        return Optional.of(repository.save(entity));
    }

    public List<UserAccessTokenEntity> findAllByCreationDate(Date creationDate) {
        return repository.findAllByCreationDate(creationDate);
    }

    public List<UserAccessTokenEntity> findAllByCreationTimeBetween(
            Date creationTimeStart,
            Date creationTimeEnd
    ) {
        return repository.findAllByCreationDateBetween(creationTimeStart, creationTimeEnd);
    }

    public List<UserAccessTokenEntity> findAllWithCreationDateTimeBefore(Date creationDateTime) {
        return repository.findAllWithCreationDateTimeBefore(creationDateTime);
    }

    public Optional<UserAccessTokenEntity> findNewest() {
        return repository.findFirstByOrderByCreationDateDesc();
    }
}
