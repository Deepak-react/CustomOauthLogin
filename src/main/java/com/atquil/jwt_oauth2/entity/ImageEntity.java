package com.atquil.jwt_oauth2.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity

@Table(name="IMAGE_INFO")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    @Column(name = "IMAGE_NAME")
    private String imgName;

    @Column(name = "IMAGE_URL")
    private String imgUrl;

    @Column(name = "IMAGE_SIZE")
    private Long imgSize;


}
