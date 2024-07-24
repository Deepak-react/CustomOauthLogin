package com.atquil.jwt_oauth2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="REFRESH_TOKENS")
public class RefreshTokenEntity {

    @Id
    @GeneratedValue
    private Long id;
    // Increase the length to a value that can accommodate your actual token lengths
    @Column(name = "REFRESH_TOKEN", nullable = false, length = 10000)
    private String refreshToken;

    @Column(name = "REVOKED")
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private UserInfoEntity user;


    @Override
    public String toString(){
        return "RefreshTokenEntity{"+"Id"+id+",Refresh_token"+refreshToken+"Revoked_Status"+revoked+'\'' +
                '}';
    }

}