package com.intexsoft.analytics.repository;

import java.util.UUID;

import com.intexsoft.analytics.model.Authentication;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AuthenticationRepository extends ReactiveCrudRepository<Authentication, UUID> {

    Mono<Authentication> findAuthenticationByEmail(String email);

}
