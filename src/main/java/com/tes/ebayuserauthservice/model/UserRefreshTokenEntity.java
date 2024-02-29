package com.tes.ebayuserauthservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class UserRefreshTokenEntity extends UserAuthEntity {
    @Column(name = "refresh_token")
    private String refreshToken;
}
