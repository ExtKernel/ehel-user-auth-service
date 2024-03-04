package com.tes.ebayuserauthservice.token;

import com.tes.ebayuserauthservice.exception.NoRecordOfRefreshTokenException;
import com.tes.ebayuserauthservice.exception.RestTemplateResponseErrorHandler;
import com.tes.ebayuserauthservice.service.UserAuthCodeService;
import com.tes.ebayuserauthservice.service.UserRefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Lazy
@Component
public class EbayTokenRetriever {
    @Value("${ebayClientId}")
    private String cliendId;

    @Value("${ebayClientSecret}")
    private String clientSecret;

    @Value("${ebayTokenUrl}")
    private String ebayTokenUrl;

    @Value("${EBAY_CLIENT_REDIRECT_URI}")
    private String redirectUri;

    @Autowired
    private UserRefreshTokenService refreshTokenService;

    @Autowired
    private UserAuthCodeService authCodeService;

    /**
     * Exchanges user authorization code for refresh and access tokens
     * by sending a POST request to the eBay token endpoint.
     * This method prepares the necessary headers and request body with the required parameters
     * and sends the request using RestTemplate.
     *
     * @return A Map containing the response body with tokens obtained from the eBay token endpoint.
     */
    public Map<?, ?> exchangeUserCodeForRefreshAndAccessTokens() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(
                Base64.getEncoder()
                        .encodeToString((cliendId + ":" + clientSecret).getBytes()));

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("code", authCodeService.findNewest().getAuthCode());
        requestBody.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>>
                requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler(authCodeService))
                .build();

        return (Map<?, ?>) restTemplate.exchange(
                ebayTokenUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        ).getBody();
    }

    /**
     * Renews the access token using the refresh token obtained from the refresh token service.
     * If no refresh token is found, a new one is generated and saved
     * before attempting to renew the access token.
     * This method sends a POST request to the eBay token endpoint
     * with the refresh token in the request body.
     *
     * @return A Map containing the response body
     * with the renewed access token obtained from the eBay token endpoint.
     */
    public Map<?, ?> renewAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(
                Base64.getEncoder()
                        .encodeToString((cliendId + ":" + clientSecret).getBytes()));

        String refreshToken;

        // try to retrieve the newest refresh token
        // from the refresh token service
        // if no record of a refresh token is found,
        // generate a new one and save it before retrying to retrieve
        try {
            refreshToken = refreshTokenService.findNewest().getRefreshToken();
        } catch (NoRecordOfRefreshTokenException refreshTokenException) {
            refreshTokenService.generateAndSave();
            refreshToken = refreshTokenService.findNewest().getRefreshToken();
        }

        String requestBody = "grant_type=refresh_token&refresh_token=" + refreshToken;
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler(refreshTokenService))
                .build();

        return (Map<?, ?>) restTemplate.exchange(
                ebayTokenUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        ).getBody();
    }
}
