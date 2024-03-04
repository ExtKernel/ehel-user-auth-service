package com.tes.ebayuserauthservice.controller;

import com.tes.ebayuserauthservice.service.UserAuthService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAuthController {
    @Autowired
    UserAuthService service;

    @GetMapping("/generate-tokens")
    public Map<String, Object> generateRefreshAndAccessTokens() {
        return service.generateRefreshAndAccessTokens();
    }

    @GetMapping("/generate-tokens/save")
    public Map<String, Object> generateAndSaveRefreshAndAccessTokens() {
        return service.generateAndSaveRefreshAndAccessTokens();
    }
}
