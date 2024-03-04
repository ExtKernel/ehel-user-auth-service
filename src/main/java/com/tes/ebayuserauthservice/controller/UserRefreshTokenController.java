package com.tes.ebayuserauthservice.controller;

import com.tes.ebayuserauthservice.model.UserRefreshTokenEntity;
import com.tes.ebayuserauthservice.service.UserRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/refresh-token")
@RestController
public class UserRefreshTokenController {
    @Autowired
    private UserRefreshTokenService service;

    @GetMapping("/latest")
    public UserRefreshTokenEntity getLatestRefreshToken() {
        return service.findNewest();
    }

    @GetMapping("/generate")
    public UserRefreshTokenEntity generateRefreshToken() {
        return service.generateAndSave();
    }
}
