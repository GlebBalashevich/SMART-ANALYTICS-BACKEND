package com.intexsoft.analytics.controller;

import com.intexsoft.analytics.dto.authentication.AuthenticationDto;
import com.intexsoft.analytics.dto.authentication.AuthenticationRequestDto;
import com.intexsoft.analytics.dto.authentication.AuthenticationResponseDto;
import com.intexsoft.analytics.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication API", description = "Api for performing authentication operations")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Department manager registration")
    public Mono<AuthenticationDto> register(@RequestBody @Validated AuthenticationRequestDto requestDto) {
        return authenticationService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(description = "Department manager authorization")
    public Mono<AuthenticationResponseDto> login(@RequestBody @Validated AuthenticationRequestDto requestDto) {
        return authenticationService.login(requestDto);
    }

}
