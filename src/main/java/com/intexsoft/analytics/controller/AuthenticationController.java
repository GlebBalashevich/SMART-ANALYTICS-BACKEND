package com.intexsoft.analytics.controller;

import com.intexsoft.analytics.dto.RegistrationRequestDto;
import com.intexsoft.analytics.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public Mono<String> register(@RequestBody @Validated RegistrationRequestDto requestDto){
        return authenticationService.register(requestDto);
    }

}
