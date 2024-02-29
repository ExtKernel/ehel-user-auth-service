package com.tes.ebayuserauthservice.controller;

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
        return service.findNewest().get(); // catch potential exceptions
    }

    @GetMapping("/renew")
    public UserAccessTokenEntity renewAccessToken() {
        return service.renewAccessToken();
    }

    @GetMapping("/generate")
    public UserAccessTokenEntity generateAccessToken() {
        return service.generateAndSaveAccessToken().get(); // catch potential exceptions
    }
}
