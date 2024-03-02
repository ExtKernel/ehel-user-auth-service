package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.model.UserAccessTokenEntity;
import com.tes.ebayuserauthservice.model.UserRefreshTokenEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class UserAuthUtils {
    @Autowired
    private UserRefreshTokenService refreshTokenService;

    @Autowired
    private UserAccessTokenService accessTokenService;

    @Autowired
    private EbayTokenRetriever tokenRetriever;

    public List<Object> generateRefreshAndAccessTokens() {
        return saveAuthCodeExchangeResult(tokenRetriever.exchangeUserCodeForRefreshToken());
    }

    private List<Object> saveAuthCodeExchangeResult(Map<?, ?> exchangeResult) {
        List<Object> refreshAndAccessTokenList = new ArrayList<>();
        refreshAndAccessTokenList.add(refreshTokenService.save(stripRefreshTokensFromEbayJsonMap(exchangeResult)));
        refreshAndAccessTokenList.add(accessTokenService.save(stripAccessTokensFromEbayJsonMap(exchangeResult)));

        return refreshAndAccessTokenList;
    }

    public UserRefreshTokenEntity stripRefreshTokensFromEbayJsonMap(Map<?, ?> ebayRefreshTokenJsonMap) {
        UserRefreshTokenEntity entity = new UserRefreshTokenEntity();
        entity.setRefreshToken((String) ebayRefreshTokenJsonMap.get("refresh_token"));
        entity.setExpiresIn((int) ebayRefreshTokenJsonMap.get("refresh_token_expires_in"));

        return entity;
    }

    public UserAccessTokenEntity stripAccessTokensFromEbayJsonMap(Map<?, ?> ebayRefreshTokenJsonMap) {
            UserAccessTokenEntity entity = new UserAccessTokenEntity();
            entity.setAccessToken((String) ebayRefreshTokenJsonMap.get("access_token"));
            entity.setExpiresIn((int) ebayRefreshTokenJsonMap.get("expires_in"));

            return entity;
    }
}
