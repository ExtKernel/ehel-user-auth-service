package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.exception.NoRecordOfAccessTokenException;
import com.tes.ebayuserauthservice.exception.UnknownTokenObjectGeneratorWasInjected;
import com.tes.ebayuserauthservice.model.UserAccessTokenEntity;
import com.tes.ebayuserauthservice.repository.UserAccessTokenRepository;
import com.tes.ebayuserauthservice.token.AccessTokenObjectGenerator;
import com.tes.ebayuserauthservice.token.RefreshTokenObjectGenerator;
import com.tes.ebayuserauthservice.token.TokenObjectGenerator;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserAccessTokenService {
    @Autowired
    private UserAccessTokenRepository repository;

    @Qualifier("accessTokenObjectGenerator")
    @Lazy
    @Autowired
    TokenObjectGenerator tokenGenerator;

    public UserAccessTokenEntity generateFromRefreshTokenAndSave() {
        return save(generateFromRefreshToken());
    }

    private UserAccessTokenEntity generateFromRefreshToken() {
        try {
            if (checkTokenObjectGeneratorSignature() == AccessTokenObjectGenerator.class) {
                return ((AccessTokenObjectGenerator) tokenGenerator)
                        .generateTokenFromRefreshToken();
            } else {
                AccessTokenObjectGenerator
                        accessTokenObjectGenerator = new AccessTokenObjectGenerator();
                
                return accessTokenObjectGenerator
                        .generateTokenFromRefreshToken();
            }
        } catch (UnknownTokenObjectGeneratorWasInjected exception) {
            AccessTokenObjectGenerator
                    accessTokenObjectGenerator = new AccessTokenObjectGenerator();
            
            return accessTokenObjectGenerator
                    .generateTokenFromRefreshToken();
        }
    }

    public UserAccessTokenEntity generateFromAuthCodeAndSave() {
        return save(generateFromAuthCode());
    }

    private UserAccessTokenEntity generateFromAuthCode() {
        return (UserAccessTokenEntity) tokenGenerator.generateTokenFromAuthCode();
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

    public UserAccessTokenEntity findNewest()
            throws NoRecordOfAccessTokenException {
        if (repository.findFirstByOrderByCreationDateDesc().isPresent()) {
            return repository.findFirstByOrderByCreationDateDesc().get();
        } else {
            throw new
                    NoRecordOfAccessTokenException("The latest saved access token was not found, "
                            + "because no record exists in the database");
        }
    }

    private Class<? extends TokenObjectGenerator> checkTokenObjectGeneratorSignature()
            throws UnknownTokenObjectGeneratorWasInjected {
        if (tokenGenerator instanceof AccessTokenObjectGenerator) {
            return AccessTokenObjectGenerator.class;
        } else if (tokenGenerator instanceof RefreshTokenObjectGenerator) {
            log.error("A RefreshTokenObjectGenerator object was injected"
                    + " instead of the AccessTokenObjectGenerator");

            return RefreshTokenObjectGenerator.class;
        } else {
            log.error("An AccessTokenObjectGenerator object was not injected properly");

            throw new UnknownTokenObjectGeneratorWasInjected("An unknown object was injected"
                    + " instead of known TokenObjectGenerator objects");
        }
    }
}
