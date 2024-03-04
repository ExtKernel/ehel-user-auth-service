package com.tes.ebayuserauthservice.token;

import com.tes.ebayuserauthservice.model.UserAccessTokenEntity;
import com.tes.ebayuserauthservice.model.UserRefreshTokenEntity;
import java.util.Map;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class UserAuthMapsStripper {

    /**
     * Extracts the refresh token and its expiration time from the eBay JSON map and constructs a UserRefreshTokenEntity object.
     *
     * @param ebayRefreshTokenJsonMap The eBay JSON map containing the refresh token and its expiration time.
     * @return A UserRefreshTokenEntity object initialized with the extracted refresh token and expiration time.
     */
    public UserRefreshTokenEntity stripRefreshTokenFromEbayJsonMap(Map<?, ?> ebayRefreshTokenJsonMap) {
        UserRefreshTokenEntity entity = new UserRefreshTokenEntity();
        entity.setRefreshToken((String) ebayRefreshTokenJsonMap.get("refresh_token"));
        entity.setExpiresIn((int) ebayRefreshTokenJsonMap.get("refresh_token_expires_in"));

        return entity;
    }

    /**
     * Extracts the access token and its expiration time from the eBay JSON map and constructs a UserAccessTokenEntity object.
     *
     * @param ebayRefreshTokenJsonMap The eBay JSON map containing the access token and its expiration time.
     * @return A UserAccessTokenEntity object initialized with the extracted access token and expiration time.
     */
    public UserAccessTokenEntity stripAccessTokenFromEbayJsonMap(Map<?, ?> ebayRefreshTokenJsonMap) {
            UserAccessTokenEntity entity = new UserAccessTokenEntity();
            entity.setAccessToken((String) ebayRefreshTokenJsonMap.get("access_token"));
            entity.setExpiresIn((int) ebayRefreshTokenJsonMap.get("expires_in"));

            return entity;
    }
}
