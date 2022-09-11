package com.intexsoft.analytics.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
public class SecurityConfiguration {

    @Bean
    @ConditionalOnProperty(name = "spring.security.enabled", havingValue = "true")
    public SecurityWebFilterChain securityFilterChain(final ServerHttpSecurity http,
            final ServerAuthenticationConverter authenticationConverter,
            final ReactiveAuthenticationManagerResolver<ServerWebExchange> resolver) {
        return http
                .cors().disable()
                .csrf().disable()
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/auth/register").permitAll()
                        .pathMatchers("/api/v1/auth/login").permitAll()
                        .pathMatchers("/api/**").authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .bearerTokenConverter(authenticationConverter)
                        .authenticationManagerResolver(resolver))
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "spring.security.enabled", havingValue = "false")
    public SecurityWebFilterChain securityFilterChain(final ServerHttpSecurity http) {
        return http
                .cors().disable()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/api/v1/auth/register").permitAll()
                .pathMatchers("/api/**").permitAll()
                .and()
                .build();
    }

}
