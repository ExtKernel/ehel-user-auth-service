package com.tes.ebayuserauthservice.controller;

import com.tes.ebayuserauthservice.model.UserAuthCodeEntity;
import com.tes.ebayuserauthservice.service.UserAuthCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth-code")
@RestController
public class UserAuthCodeController {
    @Autowired
    private UserAuthCodeService service;

    @GetMapping()
    public UserAuthCodeEntity saveAuthCode(@RequestParam String code, @RequestParam String expires_in) {
        return service.save(generateUserAuthCodeEntityFromRequest(code, expires_in));
    }

    @GetMapping("/latest")
    public UserAuthCodeEntity getLatestAuthCode() {
        return service.findNewest();
    }

    private UserAuthCodeEntity generateUserAuthCodeEntityFromRequest(
            String code,
            String expiresIn) {
        // map params to an entity
        UserAuthCodeEntity userAuthCode = new UserAuthCodeEntity();
        userAuthCode.setAuthCode(code);
        userAuthCode.setExpiresIn(Integer.parseInt(expiresIn));

        return userAuthCode;
    }
}
