package com.tes.ebayuserauthservice.service;

import com.tes.ebayuserauthservice.model.AuthCode;
import com.tes.ebayuserauthservice.model.EbayUser;
import com.tes.ebayuserauthservice.repository.RefreshTokenRepository;
import com.tes.ebayuserauthservice.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EbayRefreshTokenService extends GenericRefreshTokenService<EbayUser, AuthCode> {

    @Autowired
    public EbayRefreshTokenService(
            RefreshTokenRepository repository,
            RefreshTokenRepository tokenRepository,
            TokenManager<EbayUser, AuthCode> tokenManager
    ) {
        super(
                repository,
                tokenRepository,
                tokenManager
        );
    }
}
