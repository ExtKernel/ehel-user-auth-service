package com.tes.ebayuserauthservice.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tes.ebayuserauthservice.exception.WritingAuthCodeRequestBodyToJsonStringException;
import com.tes.ebayuserauthservice.exception.WritingRefreshTokenRequestBodyToJsonStringException;
import com.tes.ebayuserauthservice.model.AuthCode;
import com.tes.ebayuserauthservice.model.EbayUser;
import com.tes.ebayuserauthservice.model.RefreshToken;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EbayTokenRequestBuilder implements TokenRequestBuilder<EbayUser, AuthCode> {
    private final ObjectMapper objectMapper;

    @Autowired
    public EbayTokenRequestBuilder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public HttpEntity<String> buildHttpRequestEntity(
            EbayUser user,
            String requestBody
    ) {
        return new HttpEntity<>(
                requestBody,
                buildHeaders(user)
        );
    }

    @Override
    public String buildAuthModelRequestBody(
            EbayUser user,
            AuthCode authCode
    ) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("code", authCode.getAuthCode());
        requestBody.put("redirect_uri", user.getRedirectUrl());

        try {
            return objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException exception) {
            throw new WritingAuthCodeRequestBodyToJsonStringException(
                    "An exception occurred, while writing a request body,"
                            + " containing an auth code to a JSON string",
                    exception
            );
        }
    }

    @Override
    public String buildRefreshTokenRequestBody(RefreshToken refreshToken) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("grant_type", "refresh_token");
        requestBody.put("refresh_token", refreshToken.getToken());

        try {
            return objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException exception) {
            throw new WritingRefreshTokenRequestBodyToJsonStringException(
                    "An exception occurred, while writing a request body,"
                            + " containing a refresh token to a JSON string",
                    exception
            );
        }
    }

    @Override
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    private HttpHeaders buildHeaders(EbayUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(
                Base64.getEncoder()
                        .encodeToString((user.getClientId() + ":" + user.getClientSecret()).getBytes()));

        return headers;
    }
}
