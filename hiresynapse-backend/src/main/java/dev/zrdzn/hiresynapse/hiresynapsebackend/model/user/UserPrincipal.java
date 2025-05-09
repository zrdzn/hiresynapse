package dev.zrdzn.hiresynapse.hiresynapsebackend.model.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;

public class UserPrincipal implements OidcUser {

    @Getter
    private final User user;
    private final OidcUser oidcUser;

    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(User user, OidcUser oidcUser, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.oidcUser = oidcUser;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

}
