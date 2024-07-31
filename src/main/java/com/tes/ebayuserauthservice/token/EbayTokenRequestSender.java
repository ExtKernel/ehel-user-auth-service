package com.tes.ebayuserauthservice.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tes.ebayuserauthservice.exception.AccessTokenJsonReadingException;
import com.tes.ebayuserauthservice.exception.RefreshTokenJsonReadingException;
import com.tes.ebayuserauthservice.model.AuthCode;
import com.tes.ebayuserauthservice.model.EbayUser;
import com.tes.ebayuserauthservice.model.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A component class, implementing {@link TokenRequestSender} interface.
 * Sends specific to the eBay API token related requests.
 */
@Component
public class EbayTokenRequestSender implements TokenRequestSender<EbayUser, AuthCode> {
    ObjectMapper objectMapper;
    RestTemplate restTemplate;
    TokenRequestBuilder<EbayUser, AuthCode> requestBuilder;

    @Value("${ebayTokenUrl}")
    private String ebayTokenUrl;

    @Autowired
    public EbayTokenRequestSender(
            ObjectMapper objectMapper,
            TokenRequestBuilder<EbayUser, AuthCode> requestBuilder
    ) {
        this.objectMapper = objectMapper;
        this.restTemplate = requestBuilder.getRestTemplate();
        this.requestBuilder = requestBuilder;
    }

    /**
     * @return a JSON node containing the refresh token, but also an access token.
     *         The access token is supposed to be ignored.
     */
    @Override
    public JsonNode sendGetRefreshTokenRequest(
            EbayUser user,
            AuthCode authCode
    ) {
        try {
            return objectMapper.readTree(restTemplate.exchange(
                    ebayTokenUrl,
                    HttpMethod.POST,
                    requestBuilder.buildHttpRequestEntity(
                            user,
                            requestBuilder.buildAuthModelRequestBody(
                                    user,
                                    authCode
                            )
                    ),
                    String.class
            ).getBody());
        } catch (JsonProcessingException exception) {
            throw new RefreshTokenJsonReadingException(
                    "An exception occurred while reading"
                            + " an eBay refresh token JSON received from the request"
                            + " to retrieve a refresh token",
                    exception
            );
        }
    }

    @Override
    public JsonNode sendGetAccessTokenRequest(
            EbayUser user,
            RefreshToken refreshToken
    ) {
        try {
            return objectMapper.readTree(restTemplate.exchange(
                    ebayTokenUrl,
                    HttpMethod.POST,
                    requestBuilder.buildHttpRequestEntity(
                            user,
                            requestBuilder.buildRefreshTokenRequestBody(refreshToken)
                    ),
                    String.class
            ).getBody());
        } catch (JsonProcessingException exception) {
            throw new AccessTokenJsonReadingException(
                    "An exception occurred while reading"
                            + " an eBay access token JSON received from the request"
                            + " to retrieve a access token",
                    exception
            );
        }
    }
}
