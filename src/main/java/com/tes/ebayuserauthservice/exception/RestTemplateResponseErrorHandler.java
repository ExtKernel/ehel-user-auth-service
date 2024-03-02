package com.tes.ebayuserauthservice.exception;

import com.tes.ebayuserauthservice.service.UserAuthCodeService;
import com.tes.ebayuserauthservice.service.UserRefreshTokenService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@NoArgsConstructor
@AllArgsConstructor
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    private UserAuthCodeService authCodeService;
    private UserRefreshTokenService refreshTokenService;

    public RestTemplateResponseErrorHandler(UserAuthCodeService authCodeService) {
        this.authCodeService = authCodeService;
    }

    public RestTemplateResponseErrorHandler(UserRefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError()) {
            String responseBodyText = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);

            if (responseBodyText.contains("code")) {
                throw new
                        ExpiredAuthCodeException
                        ("The authentication code has expired. It was valid for: " +
                                authCodeService.findNewest().getExpiresIn() + " seconds");
            } else if (responseBodyText.contains("refresh")) {
                throw new
                        ExpiredRefreshTokenException
                        ("The refresh token has expired. It was valid for: " +
                                refreshTokenService.findNewest().getExpiresIn() + " seconds");
            }
        }
    }
}
