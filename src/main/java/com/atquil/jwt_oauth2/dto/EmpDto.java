package com.atquil.jwt_oauth2.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmpDto {
    @JsonProperty("name")
    private String emp_name;

    @JsonProperty("email")
    private String empEmailId;

    @JsonProperty("role")
    private String empRole;

    @JsonProperty("salary")
    private String salary;

    @JsonProperty("mobile")
    private String mobile;

    private boolean isNew;
}
