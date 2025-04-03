package dev.zrdzn.hiresynapse.hiresynapsebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String clientUrl;

    public SecurityConfig(@Value("${client.url}") String clientUrl) {
        this.clientUrl = clientUrl;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(
                auth ->
                    auth.anyRequest().permitAll()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors ->
                cors.configurationSource(
                    request -> {
                        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                        CorsConfiguration config = new CorsConfiguration();

                        config.setAllowedOrigins(List.of(clientUrl));
                        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "HEAD", "OPTIONS"));
                        config.setAllowedHeaders(List.of(
                            "Content-Type",
                            "Accept",
                            "Authorization",
                            "Origin",
                            "Access-Control-Request-Method",
                            "Access-Control-Request-Headers"
                        ));
                        config.setAllowCredentials(true);

                        source.registerCorsConfiguration("/**", config);

                        return config;
                    }
                )
            )
            .addFilterBefore(new AuthFilter(), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(
                exception ->
                    exception.authenticationEntryPoint(
                        (request, response, authException) ->
                            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase())
                    )
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .build();
    }

}
