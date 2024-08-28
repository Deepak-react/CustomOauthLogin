package com.atquil.jwt_oauth2.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {

    @JsonProperty("hostName")
    private String hostName;

    @JsonProperty("hostEmailId")
    private String hostEmailId;

    @JsonProperty("eventName")
    private String eventName;

    @JsonProperty("mainDesc")
    private String mainDesc;

    @JsonProperty("subDesc")
    private String subDesc;

    @JsonProperty("date")
    private String date;

    @JsonProperty("time")
    private String time;
}
