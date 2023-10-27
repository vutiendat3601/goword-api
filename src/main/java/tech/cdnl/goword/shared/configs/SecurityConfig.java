package tech.cdnl.goword.shared.configs;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.naming.AuthenticationException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tech.cdnl.goword.auth.services.impl.RefreshTokenProvider;
import tech.cdnl.goword.auth.services.impl.UsernamePasswordProvider;
import tech.cdnl.goword.exceptions.AppErrorCode;
import tech.cdnl.goword.exceptions.AppErrorMessage;
import tech.cdnl.goword.exceptions.ErrorResponse;
import tech.cdnl.goword.shared.filter.JwtTokenFilter;
import tech.cdnl.goword.shared.filter.RefreshTokenFilter;
import tech.cdnl.goword.shared.models.ApiResponseStatus;

@AllArgsConstructor
@Slf4j
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true, prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    Base64.Encoder base64Encoder() {
        return Base64.getEncoder();
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            log.error("[ %s ] %s".formatted(AuthenticationException.class.getSimpleName(), authException.getMessage()));
            ObjectMapper objMapper = new ObjectMapper();
            ErrorResponse errResp = new ErrorResponse(
                    ApiResponseStatus.ERROR,
                    AppErrorMessage.UNAUTHORIZED,
                    AppErrorCode.UNAUTHORIZED,
                    ZonedDateTime.now().toEpochSecond());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(401);
            response.getOutputStream().print(objMapper.writeValueAsString(errResp));
            response.getOutputStream().flush();
        };
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            UsernamePasswordProvider usernamePasswordProvider,
            RefreshTokenProvider refreshTokenProvider,
            AuthenticationEntryPoint authEntryPoint,
            JwtTokenFilter jwtTokenFilter,
            RefreshTokenFilter refTokenFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(req -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("*"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setMaxAge(3600L);
                    return config;
                }))
                .authorizeRequests(
                        reqs -> reqs
                                .antMatchers("/auth/sign-up").permitAll()
                                .anyRequest().authenticated())
                .addFilterAfter(
                        refTokenFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(authEntryPoint))
                .addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
                .authenticationProvider(usernamePasswordProvider)
                .authenticationProvider(refreshTokenProvider);
        return http.build();
    }

}
