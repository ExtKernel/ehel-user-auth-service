package com.tes.ebayuserauthservice.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.tes.ebayuserauthservice.model.AuthModel;
import com.tes.ebayuserauthservice.model.RefreshToken;
import com.tes.ebayuserauthservice.model.User;

/**
 * An interface to send token related requests.
 *
 * @param <AuthModelType> the type of the {@link AuthModel} object
 *                       that will be used for {@link RefreshToken} retrieval.
 */
public interface TokenRequestSender<UserType extends User, AuthModelType extends AuthModel> {

    /**
     * Sends a request to get a {@link JsonNode} representation of a refresh token.
     *
     * @param authModel an object that will be used for the refresh token retrieval.
     * @return a JSON node containing the refresh token.
     */
    JsonNode sendGetRefreshTokenRequest(
            UserType user,
            AuthModelType authModel
    );

    /**
     * Sends a request to get a {@link JsonNode} representation of an access token.
     *
     * @param refreshToken a refresh token to use for access token retrieval.
     * @return a JSON node containing the access token.
     */
    JsonNode sendGetAccessTokenRequest(
            UserType user,
            RefreshToken refreshToken
    );
}
