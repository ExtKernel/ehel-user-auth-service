package com.tes.ebayuserauthservice.token;

import com.tes.ebayuserauthservice.model.AccessToken;
import com.tes.ebayuserauthservice.model.AuthModel;
import com.tes.ebayuserauthservice.model.RefreshToken;
import com.tes.ebayuserauthservice.model.User;

/**
 * @param <AuthModelType> an object, which is supposed to be used for refresh token retrieval.
 */
public interface TokenManager<UserType extends User, AuthModelType extends AuthModel> {

    /**
     * Retrieves a {@link RefreshToken} using the given {@link AuthModelType} object.
     *
     * @param authModel the {@link AuthModelType} object
     *                 that will be used for the {@link RefreshToken} retrieval.
     * @return the {@link RefreshToken}.
     */
    RefreshToken getRefreshToken(
            UserType user,
            AuthModelType authModel
    );

    /**
     * Retrieves a {@link AccessToken} using the given {@link RefreshToken} object.
     *
     * @param refreshToken the {@link RefreshToken}
     *                    that will be used for the {@link AccessToken} retrieval.
     * @return the {@link AccessToken}.
     */
    AccessToken getAccessToken(
            UserType user,
            RefreshToken refreshToken
    );
}
