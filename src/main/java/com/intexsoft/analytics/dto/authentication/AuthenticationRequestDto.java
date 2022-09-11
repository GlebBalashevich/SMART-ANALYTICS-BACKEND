package com.intexsoft.analytics.dto.authentication;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.intexsoft.analytics.validation.Password;
import lombok.Data;

@Data
public class AuthenticationRequestDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Password
    private String password;

}
