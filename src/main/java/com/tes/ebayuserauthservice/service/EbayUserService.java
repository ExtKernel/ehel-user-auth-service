package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.exception.NoRecordOfRefreshTokenException;
import com.tes.ebayuserauthservice.exception.RefreshTokenIsNullException;
import com.tes.ebayuserauthservice.model.AccessToken;
import com.tes.ebayuserauthservice.model.AuthCode;
import com.tes.ebayuserauthservice.model.EbayUser;
import com.tes.ebayuserauthservice.model.RefreshToken;
import com.tes.ebayuserauthservice.repository.EbayUserRepository;
import com.tes.ebayuserauthservice.token.TokenManager;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EbayUserService
        extends GenericCrudService<EbayUser, Long>
        implements Oauth2UserService<EbayUser, Long> {
    private final AuthCodeService authCodeService;
    private final TokenService<EbayUser, RefreshToken, AuthCode> refreshTokenService;
    private final TokenService<EbayUser, AccessToken, RefreshToken> accessTokenService;

    @Autowired
    public EbayUserService(
            EbayUserRepository repository,
            AuthCodeService authCodeService,
            TokenService<EbayUser, RefreshToken, AuthCode> refreshTokenService,
            TokenService<EbayUser, AccessToken, RefreshToken> accessTokenService
    ) {
        super(repository);
        this.authCodeService = authCodeService;
        this.refreshTokenService = refreshTokenService;
        this.accessTokenService = accessTokenService;
    }

    @Override
    public AccessToken generateAccessToken(Long userId) {
        return accessTokenService.generate(
                findById(userId),
                getValidRefreshToken(userId)
        );
    }

    @Override
    public RefreshToken generateRefreshToken(Long userId) {
        return refreshTokenService.generate(
                findById(userId),
                authCodeService.getValid()
        );
    }

    @Override
    public RefreshToken saveRefreshToken(
            Long userId,
            Optional<RefreshToken> optionalRefreshToken
    ) {
        RefreshToken refreshToken = optionalRefreshToken.orElseThrow(() -> new RefreshTokenIsNullException("The refresh token is null"));

        // add the token to the list, so old tokens are not deleted
        List<RefreshToken> refreshTokens = getRefreshTokens(userId);
        refreshTokens.add(refreshToken);

        // set tokens to the user
        EbayUser user = findById(userId);
        user.setRefreshTokens(refreshTokens);

        // save
        refreshTokenService.save(optionalRefreshToken);
        save(Optional.of(user));

        return refreshToken;
    }

    @Override
    public RefreshToken getValidRefreshToken(Long userId) {
        try {
            RefreshToken refreshToken = refreshTokenService.findLatest();

            // check if the token is expired by adding its expiration time to the creation date
            // if the resulting date-time is before the current moment, the token is expired
            Date refreshTokenExpirationDate = Date.from(Instant.ofEpochMilli(
                    refreshToken.getCreationDate().getTime() + refreshToken.getExpiresIn()));

            if (!refreshTokenExpirationDate.after(new Date())) return saveRefreshToken(
                        userId,
                        Optional.of(generateRefreshToken(userId))
            );

            return refreshToken;
        } catch (NoRecordOfRefreshTokenException exception) {
            return saveRefreshToken(
                    userId,
                    Optional.of(generateRefreshToken(userId))
            );
        }
    }

    @Override
    public List<RefreshToken> getRefreshTokens(Long userId) {
        EbayUser user = findById(userId);
        List<RefreshToken> refreshTokens;

        if (user.getRefreshTokens() != null) {
            refreshTokens = user.getRefreshTokens();
        } else {
            refreshTokens = new ArrayList<>();
        }

        return refreshTokens;
    }
}
