package net.bombdash.core.site.auth;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import net.bombdash.core.api.models.PlayerProfile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@ToString(exclude = {"password"})
@Builder
@Value
public class BombDashUser implements UserDetails {
    private final String id;
    private final String email;
    private transient final String password;
    private final PlayerProfile last;
    private final Status status;


    public PlayerProfile getProfile() {
        return last;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(status);
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
