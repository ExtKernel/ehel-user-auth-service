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
        System.out.println("Auth code received: " + code);

        UserAuthCodeEntity entity = new UserAuthCodeEntity();
        entity.setAuthCode(code);
        entity.setExpiresIn(Integer.parseInt(expires_in));

        return service.save(entity).get();
    }

    @GetMapping("/latest")
    public UserAuthCodeEntity getLatestAuthCode() {
        return service.findNewest().get();
    }
}
