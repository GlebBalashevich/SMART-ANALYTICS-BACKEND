package com.intexsoft.analytics.service;

import com.intexsoft.analytics.dto.RegistrationRequestDto;
import com.intexsoft.analytics.repository.AuthenticationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationRepository authenticationRepository;

    private final EmployeeService employeeService;

    public Mono<String> register(RegistrationRequestDto requestDto){
        return Mono.empty();
    }

}
