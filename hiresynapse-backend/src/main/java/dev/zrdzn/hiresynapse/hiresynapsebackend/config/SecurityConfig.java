package dev.zrdzn.hiresynapse.hiresynapsebackend.config;

import dev.zrdzn.hiresynapse.hiresynapsebackend.service.CustomOidcUserService;
import dev.zrdzn.hiresynapse.hiresynapsebackend.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(
                auth ->
                    auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/v1/jobs/published").permitAll()
                        .requestMatchers("/v1/candidates").permitAll()
                        .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(new CustomOidcUserService(userService)))
                .loginProcessingUrl("/v1/login/oauth2/code/*")
                .defaultSuccessUrl("http://localhost:3000", true)
                .successHandler((request, response, authentication) -> {
                    response.sendRedirect("http://localhost:3000");
                })
                .failureUrl("/v1/login?error=true")
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect("/v1/login?error=true");
                })
            )
            .logout(logout ->
                logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            )
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors ->
                cors.configurationSource(
                    request -> {
                        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                        CorsConfiguration config = new CorsConfiguration();

                        config.setAllowedOrigins(List.of("http://localhost:3000", "https://dev-2x7pq0i3.us.auth0.com"));
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
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .build();
    }

}
