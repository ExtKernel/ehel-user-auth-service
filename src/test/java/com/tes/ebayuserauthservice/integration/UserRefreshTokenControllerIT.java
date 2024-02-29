package com.tes.ebayuserauthservice.integration;

import com.tes.ebayuserauthservice.EbayUserAuthServiceApplication;
import com.tes.ebayuserauthservice.model.UserAuthCodeEntity;
import com.tes.ebayuserauthservice.model.UserRefreshTokenEntity;
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
public class UserRefreshTokenControllerIT {
    @Autowired
    MockMvc mvc;

    @Autowired
    UserAuthCodeService authCodeService;

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
    public void generateRefreshToken_WhenGivenNothing_ShouldRespondWithUserRefreshTokenEntity()
            throws Exception {
        mvc.perform(get("/refresh-token/generate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.expiresIn").isNotEmpty());
    }

    @Order(2)
    @Test
    public void getLatestRefreshToken_WhenGivenNothing_ShouldRespondWithUserRefreshTokenEntity()
            throws Exception {
        UserRefreshTokenEntity refreshToken = new UserRefreshTokenEntity();
        refreshToken.setRefreshToken("some-sample-refresh-token");
        refreshToken.setExpiresIn(3600);
        refreshTokenService.save(refreshToken);

        mvc.perform(get("/refresh-token/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken").value(refreshToken.getRefreshToken()))
                .andExpect(jsonPath("$.expiresIn").value(refreshToken.getExpiresIn()));
    }
}
