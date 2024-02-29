package com.tes.ebayuserauthservice.integration;

import com.tes.ebayuserauthservice.EbayUserAuthServiceApplication;
import com.tes.ebayuserauthservice.model.UserAuthCodeEntity;
import com.tes.ebayuserauthservice.service.UserAuthCodeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = EbayUserAuthServiceApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
@Transactional
public class UserAuthCodeControllerIT {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserAuthCodeService service;

    @Test
    public void saveAuthCode_WhenGivenCode_ShouldRespondWithUserAuthCodeEntity()
            throws Exception {
        String code = "some-sample-code";
        int expiresIn = 3600;

        mvc.perform(get("/auth-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("code", code)
                        .param("expires_in", String.valueOf(expiresIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authCode").value(code));
    }

    @Test
    public void getLatestAuthCode_WhenThereIsSavedEntity_ShouldRespondWithUserAuthCodeEntity()
            throws Exception {
        String code = "some-sample-code";
        int expiresIn = 3600;

        UserAuthCodeEntity entity = new UserAuthCodeEntity();
        entity.setAuthCode(code);
        entity.setExpiresIn(expiresIn);

        service.save(entity);

        mvc.perform(get("/auth-code/latest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
