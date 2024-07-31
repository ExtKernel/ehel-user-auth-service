package com.tes.ebayuserauthservice.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tes.ebayuserauthservice.TestObjectBuilder;
import com.tes.ebayuserauthservice.exception.NoRecordOfRefreshTokenException;
import com.tes.ebayuserauthservice.model.AuthModel;
import com.tes.ebayuserauthservice.model.EbayUser;
import com.tes.ebayuserauthservice.model.RefreshToken;
import com.tes.ebayuserauthservice.repository.RefreshTokenRepository;
import com.tes.ebayuserauthservice.service.GenericRefreshTokenService;
import com.tes.ebayuserauthservice.token.TokenManager;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.JpaRepository;

public class GenericRefreshTokenServiceTest {
    private final TestObjectBuilder testObjectBuilder = new TestObjectBuilder();
    private final EbayUser user = testObjectBuilder.buildTestEbayUser();

    @Mock
    private RefreshTokenRepository repository;

    @Mock
    private RefreshTokenRepository tokenRepository;

    @Mock
    private TokenManager<EbayUser, TestAuthModel> tokenManager;

    private GenericRefreshTokenService<EbayUser, TestAuthModel> service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TestGenericRefreshTokenService(
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
        TestAuthModel authModel = new TestAuthModel();
        RefreshToken expectedRefreshToken = new RefreshToken();
        when(tokenManager.getRefreshToken(
                user,
                authModel
        )).thenReturn(expectedRefreshToken);

        // When
        RefreshToken result = service.generate(
                user,
                authModel
        );

        // Then
        assertEquals(expectedRefreshToken, result);
        verify(tokenManager).getRefreshToken(
                user,
                authModel
        );
    }

    @Test
    void testFindLatest() throws NoRecordOfRefreshTokenException {
        // Given
        RefreshToken expectedRefreshToken = new RefreshToken();
        when(tokenRepository.findFirstByOrderByCreationDateDesc()).thenReturn(Optional.of(expectedRefreshToken));

        // When
        RefreshToken result = service.findLatest();

        // Then
        assertEquals(expectedRefreshToken, result);
        verify(tokenRepository).findFirstByOrderByCreationDateDesc();
    }

    @Test
    void testFindLatestNoRecord() {
        // Given
        when(tokenRepository.findFirstByOrderByCreationDateDesc()).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoRecordOfRefreshTokenException.class, () -> service.findLatest());
        verify(tokenRepository).findFirstByOrderByCreationDateDesc();
    }

    private EbayUser buildTestEbayUser() {
        return new EbayUser(
                "test-username",
                "test-client-id",
                "test-client-secret",
                "test-redirect-url"
        );
    }

    // Test AuthModel class
    private static class TestAuthModel extends AuthModel {
    }

    // Concrete implementation of GenericRefreshTokenService for testing
    private static class TestGenericRefreshTokenService extends GenericRefreshTokenService<EbayUser, TestAuthModel> {
        public TestGenericRefreshTokenService(
                JpaRepository<RefreshToken, Long> repository,
                RefreshTokenRepository tokenRepository,
                TokenManager<EbayUser, TestAuthModel> tokenManager
        ) {
            super(repository, tokenRepository, tokenManager);
        }
    }
}
