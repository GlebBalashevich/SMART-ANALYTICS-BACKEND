package com.intexsoft.analytics.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.intexsoft.analytics.validation.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Password
    private String password;
}
