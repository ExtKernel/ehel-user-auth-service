package com.tes.ebayuserauthservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class UserAuthCodeEntity extends UserAuthEntity {
    @Column(name = "auth_code")
    private String authCode;
}
