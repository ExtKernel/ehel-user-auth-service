package com.tes.ebayuserauthservice.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tes.ebayuserauthservice.TestObjectBuilder;
import com.tes.ebayuserauthservice.exception.NoRecordOfAccessTokenException;
import com.tes.ebayuserauthservice.model.AccessToken;
import com.tes.ebayuserauthservice.model.AuthCode;
import com.tes.ebayuserauthservice.model.EbayUser;
import com.tes.ebayuserauthservice.model.RefreshToken;
import com.tes.ebayuserauthservice.repository.AccessTokenRepository;
import com.tes.ebayuserauthservice.service.GenericAccessTokenService;
import com.tes.ebayuserauthservice.token.TokenManager;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.JpaRepository;

public class GenericAccessTokenServiceTest {
    private final TestObjectBuilder testObjectBuilder = new TestObjectBuilder();
    private final EbayUser user = testObjectBuilder.buildTestEbayUser();

    @Mock
    private AccessTokenRepository repository;

    @Mock
    private AccessTokenRepository tokenRepository;

    @Mock
    private TokenManager<EbayUser, AuthCode> tokenManager;

    private GenericAccessTokenService<EbayUser> service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TestGenericAccessTokenService(
                repository,
                tokenRepository,
                tokenManager
        );
    }

    @Test
    void testGenerate() {
        // Given
        long userId = 1L;
        user.setId(userId);
        RefreshToken refreshToken = new RefreshToken();
        AccessToken expectedAccessToken = new AccessToken();
        when(tokenManager.getAccessToken(
                user,
                refreshToken
        )).thenReturn(expectedAccessToken);

        // When
        AccessToken result = service.generate(
                user,
                refreshToken
        );

        // Then
        assertEquals(expectedAccessToken, result);
        verify(tokenManager).getAccessToken(
                user,
                refreshToken
        );
    }

    @Test
    void testFindLatest() throws NoRecordOfAccessTokenException {
        // Given
        AccessToken expectedAccessToken = new AccessToken();
        when(tokenRepository.findFirstByOrderByCreationDateDesc())
                .thenReturn(Optional.of(expectedAccessToken));

        // When
        AccessToken result = service.findLatest();

        // Then
        assertEquals(expectedAccessToken, result);
        verify(tokenRepository).findFirstByOrderByCreationDateDesc();
    }

    @Test
    void testFindLatestNoRecord() {
        // Given
        when(tokenRepository.findFirstByOrderByCreationDateDesc())
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoRecordOfAccessTokenException.class, () -> service.findLatest());
        verify(tokenRepository).findFirstByOrderByCreationDateDesc();
    }

    // Concrete implementation of GenericAccessTokenService for testing
    private static class TestGenericAccessTokenService extends GenericAccessTokenService {
        public TestGenericAccessTokenService(
                JpaRepository<AccessToken, Long> repository,
                AccessTokenRepository tokenRepository,
                TokenManager<EbayUser, AuthCode> tokenManager
        ) {
            super(repository, tokenRepository, tokenManager);
        }
    }
}
