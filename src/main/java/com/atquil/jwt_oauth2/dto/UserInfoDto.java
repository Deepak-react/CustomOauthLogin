package com.atquil.jwt_oauth2.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("email")
    private String email;

    @JsonProperty("pass")
    private String pass;


}
