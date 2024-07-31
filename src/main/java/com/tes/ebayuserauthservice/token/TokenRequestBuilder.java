package com.tes.ebayuserauthservice.token;

import com.tes.ebayuserauthservice.model.AuthModel;
import com.tes.ebayuserauthservice.model.RefreshToken;
import com.tes.ebayuserauthservice.model.AccessToken;
import com.tes.ebayuserauthservice.model.Token;
import com.tes.ebayuserauthservice.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

/**
 * An interface to send token related requests.
 *
 * @param <AuthModelType> the type of the {@link AuthModel} object
 *                       that will be used for {@link RefreshToken} retrieval.
 */
public interface TokenRequestBuilder<UserType extends User, AuthModelType extends AuthModel> {

    /**
     * Builds an {@link HttpEntity}
     * for {@link Token}-related requests.
     *
     * @param requestBody a request body to include in the {@link HttpEntity}.
     * @return the {@link HttpEntity}.
     */
    HttpEntity buildHttpRequestEntity(
            UserType user,
            String requestBody
    );

    /**
     * Builds a request body, containing a {@link AuthModelType}
     * to perform a {@link } retrieval request.
     *
     * @param authModel the {@link AuthModelType}.
     * @return the request body.
     */
    String buildAuthModelRequestBody(
            UserType user,
            AuthModelType authModel
    );

    /**
     * Builds a request body, containing a {@link RefreshToken}
     * to perform a {@link AccessToken} retrieval request.
     *
     * @param refreshToken the {@link RefreshToken}.
     * @return the request body.
     */
    String buildRefreshTokenRequestBody(RefreshToken refreshToken);

    /**
     * Get a custom {@link RestTemplate} object, tailored for the specific needs.
     *
     * @return the custom {@link RestTemplate}.
     */
    RestTemplate getRestTemplate();
}
