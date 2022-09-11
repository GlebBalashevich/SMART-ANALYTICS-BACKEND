package com.intexsoft.analytics.configuration;

import com.intexsoft.analytics.security.JwtSecurityFilter;
import com.intexsoft.analytics.security.JwtTokenProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(WebFluxAutoConfiguration.class)
public class SecurityConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "spring.security", name = "enabled", havingValue = "true")
    public SecurityWebFilterChain securityFilterChain(final ServerHttpSecurity http,
            final JwtTokenProvider jwtTokenProvider) {
        return http
                .cors().disable()
                .csrf().disable()
                .httpBasic().disable()
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .pathMatchers("/api/**").authenticated())
                .addFilterAt(new JwtSecurityFilter(jwtTokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.security", name = "enabled", havingValue = "false")
    public SecurityWebFilterChain noSecurityFilterChain(final ServerHttpSecurity http) {
        return http
                .cors().disable()
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/api/**").permitAll()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
