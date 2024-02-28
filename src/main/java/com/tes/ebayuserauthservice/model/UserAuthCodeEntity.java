package com.tes.ebayuserauthservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

@Data
@Entity
public class UserAuthCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "auth_code")
    private String authCode;

    @Nullable
    @Column(name = "expires_in")
    private int expiresIn;

    @CreationTimestamp
    Date creationDate;
}
