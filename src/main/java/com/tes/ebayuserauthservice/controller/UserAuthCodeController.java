package com.tes.ebayuserauthservice.controller;

import com.tes.ebayuserauthservice.model.AuthCode;
import com.tes.ebayuserauthservice.service.AuthCodeService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/secured/auth-code")
@RestController
public class UserAuthCodeController {
    private final AuthCodeService service;

    @Autowired
    public UserAuthCodeController(AuthCodeService service) {
        this.service = service;
    }

    @GetMapping()
    public AuthCode save(
            @RequestParam String code,
            @RequestParam String expires_in
    ) {
        return service.save(Optional.of(new AuthCode(
                code,
                Integer.parseInt(expires_in)
        )));
    }

    @GetMapping("/latest")
    public AuthCode findLatest() {
        return service.findLatest();
    }
}
