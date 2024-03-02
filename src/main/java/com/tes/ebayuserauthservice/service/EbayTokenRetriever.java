package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.exception.NoRecordOfRefreshTokenException;
import com.tes.ebayuserauthservice.exception.RestTemplateResponseErrorHandler;
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

    @Autowired
    private UserRefreshTokenService refreshTokenService;

    @Autowired
    private UserAuthCodeService authCodeService;

    public Map<?, ?> exchangeUserCodeForRefreshToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(Base64.getEncoder().encodeToString((cliendId + ":" + clientSecret).getBytes()));

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("code", authCodeService.findNewest().getAuthCode());
        requestBody.add("redirect_uri", "Alexander_Gamja-Alexande-auctio-lzjzbqai");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler(authCodeService))
                .build();

        return (Map<?, ?>) restTemplate.exchange(ebayTokenUrl, HttpMethod.POST, requestEntity, Map.class).getBody();
    }

    public Map<?, ?> renewAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(Base64.getEncoder().encodeToString((cliendId + ":" + clientSecret).getBytes()));

        String refreshToken;

        try {
            refreshToken = refreshTokenService.findNewest().getRefreshToken();
        } catch (NoRecordOfRefreshTokenException refreshTokenException) {
            refreshTokenService.generateAndSaveRefreshToken();
            refreshToken = refreshTokenService.findNewest().getRefreshToken();
        }

        String requestBody = "grant_type=refresh_token&refresh_token=" + refreshToken;
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler(refreshTokenService))
                .build();

        return (Map<?, ?>) restTemplate.exchange(ebayTokenUrl, HttpMethod.POST, requestEntity, Map.class).getBody();
    }
}
