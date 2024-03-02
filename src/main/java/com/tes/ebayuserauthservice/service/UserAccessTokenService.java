package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.exception.NoRecordOfAccessTokenException;
import com.tes.ebayuserauthservice.model.UserAccessTokenEntity;
import com.tes.ebayuserauthservice.repository.UserAccessTokenRepository;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Slf4j
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

    public UserAccessTokenEntity renewAndSaveAccessToken() {
        return save(renewAccessToken());
    }

    private UserAccessTokenEntity renewAccessToken() {
        return authUtils.stripAccessTokensFromEbayJsonMap(tokenRetriever.renewAccessToken());
    }

    public UserAccessTokenEntity generateAndSaveAccessToken() {
        return save(generateAccessToken());
    }

    private UserAccessTokenEntity generateAccessToken() {
        return authUtils.stripAccessTokensFromEbayJsonMap(tokenRetriever.exchangeUserCodeForRefreshToken());
    }

    public UserAccessTokenEntity save(UserAccessTokenEntity entity) {
        return repository.save(entity);
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

    public UserAccessTokenEntity findNewest() {
        if (repository.findFirstByOrderByCreationDateDesc().isPresent()) {
            return repository.findFirstByOrderByCreationDateDesc().get();
        } else {
            throw new
                    NoRecordOfAccessTokenException
                    ("The latest saved access token was not found, " +
                            "because no record exists in the database");
        }
    }
}
