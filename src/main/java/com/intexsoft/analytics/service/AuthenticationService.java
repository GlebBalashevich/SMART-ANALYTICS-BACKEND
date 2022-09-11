package com.intexsoft.analytics.service;

import com.intexsoft.analytics.dto.authentication.AuthenticationDto;
import com.intexsoft.analytics.dto.authentication.AuthenticationRequestDto;
import com.intexsoft.analytics.dto.authentication.AuthenticationResponseDto;
import com.intexsoft.analytics.exception.AuthenticationException;
import com.intexsoft.analytics.mapper.AnalyticsMapper;
import com.intexsoft.analytics.repository.AuthenticationRepository;
import com.intexsoft.analytics.security.JwtTokenProvider;
import com.intexsoft.analytics.util.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String USER_ALREADY_REGISTERED_ERROR_MESSAGE = "User with provided credentials already registered";

    private static final String USER_INVALID_CREDENTIALS_ERROR_MESSAGE = "Invalid email address or password";

    private final AuthenticationRepository authenticationRepository;

    private final AnalyticsMapper analyticsMapper;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    public Mono<AuthenticationDto> register(AuthenticationRequestDto requestDto) {
        return authenticationRepository.findAuthenticationByEmail(requestDto.getEmail())
                .switchIfEmpty(Mono.defer(() -> error(USER_INVALID_CREDENTIALS_ERROR_MESSAGE, HttpStatus.BAD_REQUEST,
                        ErrorCode.USER_INVALID_CREDENTIALS)))
                .filter(auth -> !StringUtils.hasText(auth.getPassword()))
                .switchIfEmpty(Mono.defer(() -> error(USER_ALREADY_REGISTERED_ERROR_MESSAGE, HttpStatus.BAD_REQUEST,
                        ErrorCode.USER_REGISTERED)))
                .doOnNext(auth -> auth.setPassword(passwordEncoder.encode(requestDto.getPassword())))
                .flatMap(authenticationRepository::save)
                .map(analyticsMapper::toAuthenticationDto);
    }

    public Mono<AuthenticationResponseDto> login(AuthenticationRequestDto requestDto) {
        return authenticationRepository.findAuthenticationByEmail(requestDto.getEmail())
                .filter(auth -> passwordEncoder.matches(requestDto.getPassword(), auth.getPassword()))
                .switchIfEmpty(Mono.defer(() -> error(USER_INVALID_CREDENTIALS_ERROR_MESSAGE, HttpStatus.BAD_REQUEST,
                        ErrorCode.USER_INVALID_CREDENTIALS)))
                .map(auth -> jwtTokenProvider.generateAccessToken(auth.getEmail(), auth.getRole().name(),
                        auth.getDepartmentId().toString()))
                .map(token -> AuthenticationResponseDto.builder().token(token).build());

    }

    private <T> Mono<T> error(String message, HttpStatus status, String errorCode) {
        log.error(message);
        return Mono.error(new AuthenticationException(message, status, errorCode));
    }

}
