package com.tes.ebayuserauthservice.controller;

import com.tes.ebayuserauthservice.exception.NoRecordOfAccessTokenException;
import com.tes.ebayuserauthservice.model.UserAccessTokenEntity;
import com.tes.ebayuserauthservice.service.UserAccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/access-token")
@RestController
public class UserAccessTokenController {
    @Autowired
    private UserAccessTokenService service;

    @GetMapping("/latest")
    public UserAccessTokenEntity getLatestAccessToken() {
        return service.findNewest();
    }

    @GetMapping("/renew")
    public UserAccessTokenEntity renewAccessToken() {
        return service.generateFromRefreshTokenAndSave();
    }

    @GetMapping("/generate")
    public UserAccessTokenEntity generateAccessToken() {
        // generate from refresh token if there are already existing records of access tokens
        // if it's the first record being made, generate from the authorization code
        try {
            service.findNewest();

            return service.generateFromRefreshTokenAndSave();
        } catch (NoRecordOfAccessTokenException exception) {
            return service.generateFromAuthCodeAndSave();
        }
    }
}
