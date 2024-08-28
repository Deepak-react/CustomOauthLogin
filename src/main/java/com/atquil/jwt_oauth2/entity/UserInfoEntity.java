package com.atquil.jwt_oauth2.entity;
import com.atquil.jwt_oauth2.roles.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Entity
    @Table(name="USER_INFO")
    public class UserInfoEntity {
        @Id
        @GeneratedValue
        private Long id;


        @Column(name = "USER_NAME")
        private String userName;

        @Column(nullable = false, name = "EMAIL_ID", unique = true)
        private String emailId;

        @Column(nullable = false, name = "PASSWORD")
        private String password;

        @Column(name = "MOBILE_NUMBER")
        private String mobileNumber;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false, name = "ROLES")
        private Role roles;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        private List<RefreshTokenEntity> refreshTokens;
        @Override
             public String toString() {
                    return "UserInfoEntity{" +
                "id=" + id +
                ", username='" + userName +
                ",Email_Id"+emailId+
                ",Password"+password+
                ",Role"+roles+'\'' +
                '}';
    }


}


