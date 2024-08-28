package com.atquil.jwt_oauth2.entity;

import com.atquil.jwt_oauth2.roles.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="EVENT_ENTITY")
public class EventEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "HOST_NAME")
    private String hostName;

    @Column(name = "EVENT_NAME")
    private String eventName;

    @Column(name = "HOST_EMAIL_ID")
    private String emailId;

    @Column(name = "MAIN_DESC")
    private String mainDesc;

    @Column(name = "SUB_DESC")
    private String subDesc;

    @Column(name = "DATE")
    private String date;

    @Column(name = "TIME")
    private String time;

    @Column(name = "POSTER_IMAGE")
    private String posterImagePath;

    @Column(name = "BANNER_IMAGE")
    private String bannerImagePath;


}
