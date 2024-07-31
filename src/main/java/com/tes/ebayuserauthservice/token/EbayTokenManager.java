package com.tes.ebayuserauthservice.token;

import com.tes.ebayuserauthservice.model.AccessToken;
import com.tes.ebayuserauthservice.model.AuthCode;
import com.tes.ebayuserauthservice.model.EbayUser;
import com.tes.ebayuserauthservice.model.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EbayTokenManager implements TokenManager<EbayUser, AuthCode> {
    private final TokenRequestSender<EbayUser, AuthCode> requestSender;
    private final TokenJsonObjectMapper jsonObjectMapper;

    @Autowired
    public EbayTokenManager(
            TokenRequestSender<EbayUser, AuthCode> requestSender,
            TokenJsonObjectMapper jsonObjectMapper
    ) {
        this.requestSender = requestSender;
        this.jsonObjectMapper = jsonObjectMapper;
    }

    @Override
    public RefreshToken getRefreshToken(
            EbayUser user,
            AuthCode authCode
    ) {
        return jsonObjectMapper.mapUserRefreshTokenJsonNodeToUserRefreshToken(
                requestSender.sendGetRefreshTokenRequest(
                        user,
                        authCode
                )
        );
    }

    @Override
    public AccessToken getAccessToken(
            EbayUser user,
            RefreshToken refreshToken
    ) {
        return jsonObjectMapper.mapUserAccessTokenJsonNodeToUserAccessToken(
                requestSender.sendGetAccessTokenRequest(
                        user,
                        refreshToken
                )
        );
    }
}
