package com.tes.ebayuserauthservice.integration;

import com.tes.ebayuserauthservice.EbayUserAuthServiceApplication;
import com.tes.ebayuserauthservice.model.UserAccessTokenEntity;
import com.tes.ebayuserauthservice.model.UserAuthCodeEntity;
import com.tes.ebayuserauthservice.model.UserRefreshTokenEntity;
import com.tes.ebayuserauthservice.service.UserAccessTokenService;
import com.tes.ebayuserauthservice.service.UserAuthCodeService;
import com.tes.ebayuserauthservice.service.UserRefreshTokenService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = EbayUserAuthServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class UserAccessTokenControllerIT {
    @Autowired
    MockMvc mvc;

    @Autowired
    UserAuthCodeService authCodeService;

    @Autowired
    UserAccessTokenService accessTokenService;

    @Autowired
    UserRefreshTokenService refreshTokenService;

    @Value("${testUserAuthCode}")
    private String testUserAuthCode;

    @Value("${testUserAuthCodeExpiresIn}")
    private String testUserAuthCodeExpiresIn;

    UserAuthCodeEntity userAuthCode;

    @BeforeAll
    public void setUpAndSaveUserAuthCode() {
        userAuthCode = new UserAuthCodeEntity();
        userAuthCode.setAuthCode(testUserAuthCode);
        userAuthCode.setExpiresIn(Integer.parseInt(testUserAuthCodeExpiresIn));

        authCodeService.save(userAuthCode);
    }

    @Order(1)
    @Test
    public void generateAccessToken_WhenGivenNothing_ShouldRespondWithUserAccessTokenEntity()
            throws Exception {
        mvc.perform(get("/access-token/generate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.expiresIn").isNotEmpty());
    }

    @Order(2)
    @Test
    public void getLatestAccessToken_WhenGivenSavedAccessToken_ShouldRespondWithUserAccessTokenEntity()
            throws Exception {
        UserAccessTokenEntity accessToken = new UserAccessTokenEntity();
        accessToken.setAccessToken("some-sample-access-token");
        accessToken.setExpiresIn(3600);
        accessTokenService.save(accessToken);

        mvc.perform(get("/access-token/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken.getAccessToken()))
                .andExpect(jsonPath("$.expiresIn").value(accessToken.getExpiresIn()));
    }

    @Order(3)
    @Test
    public void renewAccessToken_WhenGivenGeneratedRefreshToken_ShouldRespondWithUserAccessTokenEntity()
            throws Exception {
        refreshTokenService.generateAndSave();

        mvc.perform(get("/access-token/renew"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.expiresIn").isNotEmpty());
    }

    // testing errors
    @Test
    public void generateAccessToken_WhenGivenInvalidAuthCode_ShouldRespondWithInternalServerError()
            throws Exception {
        UserAuthCodeEntity invalidUserAuthCode = new UserAuthCodeEntity();
        invalidUserAuthCode.setAuthCode("some-invalid-auth-code");
        invalidUserAuthCode.setExpiresIn(3600);
        authCodeService.save(invalidUserAuthCode);

        mvc.perform(get("/access-token/generate"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message")
                        .value("The authorization code has expired. It was valid for: " +
                                invalidUserAuthCode.getExpiresIn() + " seconds"));
    }

    @Test
    public void generateAccessToken_WhenGivenInvalidRefreshToken_ShouldRespondWithInternalServerError()
            throws Exception {
        UserAccessTokenEntity initialAccessToken = new UserAccessTokenEntity();
        initialAccessToken.setAccessToken("some-initial-access-token");
        initialAccessToken.setExpiresIn(3600);
        accessTokenService.save(initialAccessToken);

        UserRefreshTokenEntity invalidRefreshToken = new UserRefreshTokenEntity();
        invalidRefreshToken.setRefreshToken("some-invalid-refresh-token");
        invalidRefreshToken.setExpiresIn(3600);
        refreshTokenService.save(invalidRefreshToken);

        mvc.perform(get("/access-token/generate"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message")
                        .value("The refresh token has expired. It was valid for: " +
                                refreshTokenService.findNewest().getExpiresIn() + " seconds"));
    }

    @Test
    public void getLatestAccessToken_WhenGivenNoSavedAccessToken_ShouldRespondWithNotFound()
            throws Exception {
        mvc.perform(get("/access-token/latest"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("The latest saved access token was not found," +
                                " because no record exists in the database"));
    }

    @Test
    public void renewAccessToken_WhenGivenInvalidRefreshToken_ShouldRespondWithInternalServerError()
            throws Exception {
        UserRefreshTokenEntity invalidRefreshToken = new UserRefreshTokenEntity();
        invalidRefreshToken.setRefreshToken("some-invalid-refresh-token");
        invalidRefreshToken.setExpiresIn(3600);
        refreshTokenService.save(invalidRefreshToken);

        mvc.perform(get("/access-token/renew"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message")
                        .value("The refresh token has expired. It was valid for: " +
                                invalidRefreshToken.getExpiresIn() + " seconds"));
    }
}
