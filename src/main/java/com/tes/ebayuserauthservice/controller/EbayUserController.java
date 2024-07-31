package com.tes.ebayuserauthservice.controller;

import com.tes.ebayuserauthservice.model.EbayUser;
import com.tes.ebayuserauthservice.model.RefreshToken;
import com.tes.ebayuserauthservice.service.Oauth2UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/secured/user")
@RestController
public class EbayUserController {
    private final Oauth2UserService<EbayUser, Long> service;

    @Autowired
    public EbayUserController(
            Oauth2UserService<EbayUser, Long> service
    ) {
        this.service = service;
    }

    @PostMapping()
    public EbayUser save(@RequestBody EbayUser user) {
        return service.save(Optional.of(user));
    }

    @PostMapping("/refresh-token/generate/{userId}")
    public RefreshToken generateRefreshToken(@PathVariable Long userId) {
        return service.generateRefreshToken(userId);
    }

    @PostMapping("/refresh-token/save/{userId}")
    public RefreshToken saveRefreshToken(
            @RequestBody RefreshToken refreshToken,
            @PathVariable Long userId
    ) {
        return service.saveRefreshToken(
                userId,
                Optional.of(refreshToken)
        );
    }

    @GetMapping("/{userId}")
    public EbayUser findById(@PathVariable Long userId) {
        return service.findById(userId);
    }

    @PutMapping()
    public EbayUser update(@RequestBody EbayUser user) {
        return service.update(Optional.of(user));
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) {
        service.deleteById(userId);
    }
}
