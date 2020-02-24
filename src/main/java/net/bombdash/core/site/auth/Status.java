package net.bombdash.core.site.auth;

import org.springframework.security.core.GrantedAuthority;

public enum Status implements GrantedAuthority {
    user, admin, moderator;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
