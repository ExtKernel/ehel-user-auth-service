package com.tes.ebayuserauthservice.unit.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tes.ebayuserauthservice.TestObjectBuilder;
import com.tes.ebayuserauthservice.exception.AccessTokenJsonReadingException;
import com.tes.ebayuserauthservice.exception.RefreshTokenJsonReadingException;
import com.tes.ebayuserauthservice.model.AuthCode;
import com.tes.ebayuserauthservice.model.EbayUser;
import com.tes.ebayuserauthservice.model.RefreshToken;
import com.tes.ebayuserauthservice.token.EbayTokenRequestSender;
import com.tes.ebayuserauthservice.token.TokenRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

public class EbayTokenRequestSenderTest {
    private final TestObjectBuilder testObjectBuilder = new TestObjectBuilder();
    private final EbayUser user = testObjectBuilder.buildTestEbayUser();

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TokenRequestBuilder<EbayUser, AuthCode> requestBuilder;

    private EbayTokenRequestSender tokenRequestSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenRequestSender = new EbayTokenRequestSender(
                objectMapper,
                requestBuilder
        );
        ReflectionTestUtils.setField(tokenRequestSender, "ebayTokenUrl", "http://example.com/token");
        ReflectionTestUtils.setField(tokenRequestSender, "restTemplate", restTemplate);
    }

    @Test
    void testSendGetRefreshTokenRequest() throws JsonProcessingException {
        // Given
        AuthCode authCode = new AuthCode(
                "auth-code-value",
                3600
        );
        HttpEntity<String> httpEntity = new HttpEntity<>("body");
        ResponseEntity<String> responseEntity = ResponseEntity.ok("response");
        JsonNode refreshTokenJsonNode = objectMapper.createObjectNode();

        when(requestBuilder.buildAuthModelRequestBody(
                user,
                authCode
        )).thenReturn("body");
        when(requestBuilder.buildHttpRequestEntity(
                user,
                "body"
        )).thenReturn(httpEntity);
        when(restTemplate.exchange(
                eq("http://example.com/token"),
                eq(HttpMethod.POST),
                eq(httpEntity),
                eq(String.class)
        )).thenReturn(responseEntity);
        when(objectMapper.readTree(responseEntity.getBody())).thenReturn(refreshTokenJsonNode);

        // When
        JsonNode result = tokenRequestSender.sendGetRefreshTokenRequest(
                user,
                authCode
        );

        // Then
        assertEquals(
                refreshTokenJsonNode,
                result
        );
    }

    @Test
    void testSendGetRefreshTokenRequestThrowsException() throws JsonProcessingException {
        // Given
        AuthCode authCode = new AuthCode(
                "auth-code-value",
                3600
        );
        HttpEntity<String> httpEntity = new HttpEntity<>("body");
        ResponseEntity<String> responseEntity = ResponseEntity.ok("response");

        when(requestBuilder.buildAuthModelRequestBody(
                user,
                authCode
        )).thenReturn("body");
        when(requestBuilder.buildHttpRequestEntity(
                user,
                "body"
        )).thenReturn(httpEntity);
        when(restTemplate.exchange(
                eq("http://example.com/token"),
                eq(HttpMethod.POST),
                eq(httpEntity),
                eq(String.class)
        )).thenReturn(responseEntity);
        when(objectMapper.readTree(responseEntity.getBody()))
                .thenThrow(new JsonProcessingException("Error") {});

        // When & Then
        assertThrows(RefreshTokenJsonReadingException.class, () -> tokenRequestSender.sendGetRefreshTokenRequest(
                user,
                authCode
        ));
    }

    @Test
    void testSendGetAccessTokenRequest() throws JsonProcessingException {
        // Given
        RefreshToken refreshToken = new RefreshToken(
                "refresh-token-value",
                3600
        );
        HttpEntity<String> httpEntity = new HttpEntity<>("body");
        ResponseEntity<String> responseEntity = ResponseEntity.ok("response");
        JsonNode accessTokenJsonNode = objectMapper.createObjectNode();

        when(requestBuilder.buildRefreshTokenRequestBody(refreshToken)).thenReturn("body");
        when(requestBuilder.buildHttpRequestEntity(
                user,
                "body"
        )).thenReturn(httpEntity);
        when(restTemplate.exchange(
                eq("http://example.com/token"),
                eq(HttpMethod.POST),
                eq(httpEntity),
                eq(String.class)
        )).thenReturn(responseEntity);
        when(objectMapper.readTree(responseEntity.getBody())).thenReturn(accessTokenJsonNode);

        // When
        JsonNode result = tokenRequestSender.sendGetAccessTokenRequest(
                user,
                refreshToken
        );

        // Then
        assertEquals(
                accessTokenJsonNode,
                result
        );
    }

    @Test
    void testSendGetAccessTokenRequestThrowsException() throws JsonProcessingException {
        // Given
        RefreshToken refreshToken = new RefreshToken(
                "refresh-token-value",
                3600
        );
        HttpEntity<String> httpEntity = new HttpEntity<>("body");
        ResponseEntity<String> responseEntity = ResponseEntity.ok("response");

        when(requestBuilder.buildRefreshTokenRequestBody(refreshToken)).thenReturn("body");
        when(requestBuilder.buildHttpRequestEntity(
                user,
                "body"
        )).thenReturn(httpEntity);
        when(restTemplate.exchange(
                eq("http://example.com/token"),
                eq(HttpMethod.POST),
                eq(httpEntity),
                eq(String.class)
        )).thenReturn(responseEntity);
        when(objectMapper.readTree(responseEntity.getBody()))
                .thenThrow(new JsonProcessingException("Error") {});

        // When & Then
        assertThrows(AccessTokenJsonReadingException.class, () -> tokenRequestSender.sendGetAccessTokenRequest(
                user,
                refreshToken
        ));
    }
}
