package com.atquil.jwt_oauth2.dto;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventUserRegisDto {

    @JsonProperty("eventRegisUser")
    private String eventRegisUser;

    @JsonProperty("eventRegisEmail")
    private String eventRegisEmail;

    @JsonProperty("tickets")
    private String tickets;

    @JsonProperty("peopleCount")
    private String peopleCount;

    @JsonProperty("eventRegisMobile")
    private String eventRegisMobile;
}
