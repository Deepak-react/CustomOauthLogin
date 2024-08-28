package com.atquil.jwt_oauth2.entity;

import com.atquil.jwt_oauth2.roles.*;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="EVENT_REGIS_USER")
public class eventUserEntity {
    @Id
    @GeneratedValue
    private Long id;


    @Column(name = "USER_NAME")
    private String userName;

    @Column(nullable = false, name = "EMAIL_ID", unique = true)
    private String emailId;

    @Column(nullable = false, name = "TICKET")
    private String ticket;

    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;

    @Column(name = "PEOPLE_COUNT")
    private String  people_count;


}
