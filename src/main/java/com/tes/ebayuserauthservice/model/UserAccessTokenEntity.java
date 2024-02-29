package com.tes.ebayuserauthservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class UserAccessTokenEntity extends UserAuthEntity {
    @Column(name = "access_token", length = 2560)
    private String accessToken;
}
