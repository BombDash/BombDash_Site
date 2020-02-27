package net.bombdash.core.site.auth;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import net.bombdash.core.api.models.PlayerProfile;
import net.bombdash.core.database.Extractors;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@ToString(exclude = {"password"})
@Builder
@Value
public class BombDashUser implements UserDetails {
    private final NamedParameterJdbcTemplate template;
    private final String id;
    private final String email;
    private transient final String password;
    private final PlayerProfile last;
    private final Status status;

    public Status getStatus() {
        if (status == null)
            return Status.user;
        else
            return status;
    }

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

    public Integer getBalance() {
        MapSqlParameterSource source = new MapSqlParameterSource("id", getId());
        return template.query("SELECT balance from account where player_id = :id", source, Extractors.firstIntExtractor);
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
