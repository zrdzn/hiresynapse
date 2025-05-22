package dev.zrdzn.hiresynapse.hiresynapsebackend;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.UserPrincipal;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.user.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@TestConfiguration
@Profile("test")
public class TestSecurityConfig {

    @Bean
    public TestAuthenticationFilter testAuthenticationFilter() {
        return new TestAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http, TestAuthenticationFilter testAuthFilter) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .addFilterBefore(testAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
            .oauth2Login(AbstractHttpConfigurer::disable)
            .build();
    }

    public static class TestAuthenticationFilter extends OncePerRequestFilter {

        private static long currentUserId = 1L;

        public static void setCurrentUserId(Long userId) {
            currentUserId = userId;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            User user = new User(1L, Instant.now(), Instant.now(), "test", "test", "test", "test", UserRole.ADMIN, null);

            UserPrincipal userPrincipal = createUserPrincipal(user);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal,
                null,
                userPrincipal.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        }

        private UserPrincipal createUserPrincipal(User user) {
            OidcIdToken token = new OidcIdToken("fake", null, null,
                Map.of("sub", String.valueOf(user.getId()), "email", user.getEmail()));

            OidcUserInfo info = new OidcUserInfo(Map.of("email", user.getEmail()));

            DefaultOidcUser oidcUser = new DefaultOidcUser(
                List.of(new SimpleGrantedAuthority("ROLE_USER")), token, info);

            return new UserPrincipal(user, oidcUser, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        }
    }

}
