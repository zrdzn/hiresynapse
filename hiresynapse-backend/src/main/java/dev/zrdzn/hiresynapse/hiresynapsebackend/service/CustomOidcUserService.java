package dev.zrdzn.hiresynapse.hiresynapsebackend.service;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.User;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.UserPrincipal;
import dev.zrdzn.hiresynapse.hiresynapsebackend.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserService userService;

    public CustomOidcUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        try {
            User user = userService.getUserByEmail(oidcUser.getEmail())
                .orElseGet(() -> userService.createUser(
                    null,
                    oidcUser.getPreferredUsername(),
                    oidcUser.getEmail(),
                    oidcUser.getGivenName(),
                    oidcUser.getFamilyName(),
                    UserRole.RECRUITER,
                    oidcUser.getPicture()
                ));

            Collection<GrantedAuthority> authorities = new ArrayList<>(oidcUser.getAuthorities());
            authorities.add(new SimpleGrantedAuthority("RECRUITER"));

            return new UserPrincipal(user, oidcUser, authorities);
        } catch (Exception e) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error("processing_error"), "Error processing user details", e);
        }
    }

}
