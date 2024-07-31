package com.tes.ebayuserauthservice.unit.token;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tes.ebayuserauthservice.TestObjectBuilder;
import com.tes.ebayuserauthservice.exception.WritingAuthCodeRequestBodyToJsonStringException;
import com.tes.ebayuserauthservice.exception.WritingRefreshTokenRequestBodyToJsonStringException;
import com.tes.ebayuserauthservice.model.AuthCode;
import com.tes.ebayuserauthservice.model.EbayUser;
import com.tes.ebayuserauthservice.model.RefreshToken;
import com.tes.ebayuserauthservice.token.EbayTokenRequestBuilder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

public class EbayTokenRequestBuilderTest {
    private final TestObjectBuilder testObjectBuilder = new TestObjectBuilder();
    private final EbayUser user = testObjectBuilder.buildTestEbayUser();

    @Mock
    private ObjectMapper objectMapper;

    private EbayTokenRequestBuilder requestBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestBuilder = new EbayTokenRequestBuilder(objectMapper);
    }

    @Test
    void testBuildHttpRequestEntity() {
        // Given
        String requestBody = "request-body-value";

        // When
        HttpEntity<String> result = requestBuilder.buildHttpRequestEntity(
                user,
                requestBody
        );

        // Then
        assertNotNull(result);
        assertEquals(requestBody, result.getBody());
        HttpHeaders headers = result.getHeaders();
        assertEquals(MediaType.APPLICATION_FORM_URLENCODED, headers.getContentType());
        assertEquals(
                "Basic " + Base64.getEncoder().encodeToString((user.getClientId() + ":" + user.getClientSecret()).getBytes()),
                headers.getFirst(HttpHeaders.AUTHORIZATION)
        );
    }

    @Test
    void testBuildAuthModelRequestBody() throws JsonProcessingException {
        // Given
        AuthCode authCode = new AuthCode(
                "auth-code-value",
                3600
        );
        Map<String, String> expectedRequestBody = new HashMap<>();
        expectedRequestBody.put("grant_type", "authorization_code");
        expectedRequestBody.put("code", "auth-code-value");
        expectedRequestBody.put("redirect_uri", user.getRedirectUrl());

        when(objectMapper.writeValueAsString(expectedRequestBody)).thenReturn("json-string-value");

        // When
        String result = requestBuilder.buildAuthModelRequestBody(
                user,
                authCode
        );

        // Then
        assertEquals("json-string-value", result);
    }

    @Test
    void testBuildAuthModelRequestBodyThrowsException() throws JsonProcessingException {
        // Given
        AuthCode authCode = new AuthCode(
                "auth-code-value",
                3600
        );
        Map<String, String> expectedRequestBody = new HashMap<>();
        expectedRequestBody.put("grant_type", "authorization_code");
        expectedRequestBody.put("code", "auth-code-value");
        expectedRequestBody.put("redirect_uri", user.getRedirectUrl());

        when(objectMapper.writeValueAsString(expectedRequestBody))
                .thenThrow(new JsonProcessingException("Error") {});

        // When & Then
        assertThrows(WritingAuthCodeRequestBodyToJsonStringException.class, () -> requestBuilder.buildAuthModelRequestBody(
                user,
                authCode
        ));
    }

    @Test
    void testBuildRefreshTokenRequestBody() throws JsonProcessingException {
        // Given
        RefreshToken refreshToken = new RefreshToken(
                "refresh-token-value",
                3600
        );
        Map<String, String> expectedRequestBody = new HashMap<>();
        expectedRequestBody.put("grant_type", "refresh_token");
        expectedRequestBody.put("refresh_token", "refresh-token-value");

        when(objectMapper.writeValueAsString(expectedRequestBody)).thenReturn("json-string-value");

        // When
        String result = requestBuilder.buildRefreshTokenRequestBody(refreshToken);

        // Then
        assertEquals("json-string-value", result);
    }

    @Test
    void testBuildRefreshTokenRequestBodyThrowsException() throws JsonProcessingException {
        // Given
        RefreshToken refreshToken = new RefreshToken(
                "refresh-token-value",
                3600
        );
        Map<String, String> expectedRequestBody = new HashMap<>();
        expectedRequestBody.put("grant_type", "refresh_token");
        expectedRequestBody.put("refresh_token", "refresh-token-value");

        when(objectMapper.writeValueAsString(expectedRequestBody))
                .thenThrow(new JsonProcessingException("Error") {});

        // When & Then
        assertThrows(WritingRefreshTokenRequestBodyToJsonStringException.class, () -> requestBuilder.buildRefreshTokenRequestBody(refreshToken));
    }

    @Test
    void testGetRestTemplate() {
        // When
        RestTemplate result = requestBuilder.getRestTemplate();

        // Then
        assertNotNull(result);
    }
}
