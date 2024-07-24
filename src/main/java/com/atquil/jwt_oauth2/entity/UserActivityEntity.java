package com.atquil.jwt_oauth2.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity

@Table(name="USER_ACTIVITY")
public class UserActivityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "ACTIVITY_TYPE")
    private String activityType;

    @Column(name = "TIME_STAMP")
    private LocalDateTime timeStamp;



}
