package com.tes.ebayuserauthservice.token;

import com.tes.ebayuserauthservice.model.UserAccessTokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class AccessTokenObjectGenerator implements TokenObjectGenerator {
    @Autowired
    UserAuthMapsStripper authMapsStripper;

    @Autowired
    EbayTokenRetriever tokenRetriever;

    public UserAccessTokenEntity generateTokenFromAuthCode() {
        return authMapsStripper.stripAccessTokenFromEbayJsonMap(
                tokenRetriever.exchangeUserCodeForRefreshAndAccessTokens());
    }

    public UserAccessTokenEntity generateTokenFromRefreshToken() {
        return authMapsStripper
                .stripAccessTokenFromEbayJsonMap(tokenRetriever.renewAccessToken());
    }
}
