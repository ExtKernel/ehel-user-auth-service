package com.tes.ebayuserauthservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.Date;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Data
@MappedSuperclass
public class UserAuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "expires_in")
    private int expiresIn;

    @CreationTimestamp
    Date creationDate;
}
