package com.tes.ebayuserauthservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

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
        requestBody.add("code", authCodeService.findNewest().get().getAuthCode()); // catch potential exceptions
        requestBody.add("redirect_uri", "Alexander_Gamja-Alexande-auctio-lzjzbqai");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        return (Map<?, ?>) restTemplate.exchange(ebayTokenUrl, HttpMethod.POST, requestEntity, Map.class).getBody();
    }

    public Map<?, ?> renewAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(Base64.getEncoder().encodeToString((cliendId + ":" + clientSecret).getBytes()));

        String refreshToken = refreshTokenService.findNewest().get().getRefreshToken(); // catch potential exceptions
        String requestBody = "grant_type=refresh_token&refresh_token=" + refreshToken;
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        return (Map<?, ?>) restTemplate.exchange(ebayTokenUrl, HttpMethod.POST, requestEntity, Map.class).getBody();
    }
}
