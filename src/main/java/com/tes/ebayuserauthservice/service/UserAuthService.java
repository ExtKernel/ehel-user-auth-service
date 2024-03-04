package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.token.EbayTokenRetriever;
import com.tes.ebayuserauthservice.token.UserAuthMapsStripper;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {
    @Lazy
    @Autowired
    UserAuthMapsStripper authMapsStripper;

    @Lazy
    @Autowired
    EbayTokenRetriever tokenRetriever;

    @Lazy
    @Autowired
    UserAccessTokenService accessTokenService;

    @Lazy
    @Autowired
    UserRefreshTokenService refreshTokenService;

    /**
     * Generates refresh and access tokens by exchanging user authorization code
     * for both refresh and access tokens.
     *
     * @return A map containing the generated tokens. The map includes:
     *         - "accessToken": The UserAccessTokenEntity object.
     *         - "refreshToken": The UserRefreshTokenEntity object.
     */
    public Map<String, Object> generateRefreshAndAccessTokens() {
        Map<?, ?> userCodeExchangeResult = tokenRetriever
                .exchangeUserCodeForRefreshAndAccessTokens();

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", authMapsStripper
                .stripAccessTokenFromEbayJsonMap(userCodeExchangeResult));
        tokens.put("refreshToken", authMapsStripper
                .stripRefreshTokenFromEbayJsonMap(userCodeExchangeResult));

        return tokens;
    }

    /**
     * Generates and saves refresh and access tokens by exchanging user authorization code
     * for both refresh and access tokens.
     *
     * @return A map containing the generated tokens. The map includes:
     *         - "accessToken": The UserAccessTokenEntity object.
     *         - "refreshToken": The UserRefreshTokenEntity object.
     */
    public Map<String, Object> generateAndSaveRefreshAndAccessTokens() {
        Map<String, Object> savedTokens = new HashMap<>();
        savedTokens.put("accessToken", accessTokenService.generateFromAuthCodeAndSave());
        savedTokens.put("refreshToken", refreshTokenService.generateAndSave());

        return savedTokens;
    }
}
