package com.tes.ebayuserauthservice.token;

import com.tes.ebayuserauthservice.model.UserRefreshTokenEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class RefreshTokenObjectGenerator implements TokenObjectGenerator {
    @Autowired
    UserAuthMapsStripper authMapsStripper;

    @Autowired
    EbayTokenRetriever tokenRetriever;

    public UserRefreshTokenEntity generateTokenFromAuthCode() {
        return authMapsStripper.stripRefreshTokenFromEbayJsonMap(
                tokenRetriever.exchangeUserCodeForRefreshAndAccessTokens());
    }
}
