package net.bombdash.core.site.auth;

import net.bombdash.core.api.models.PlayerProfile;
import net.bombdash.core.database.Extractors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserService implements UserDetailsService {
    @Autowired
    private NamedParameterJdbcTemplate template;

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        if (mail.equals("system")) {
            return new BombDashUser(
                    template,
                    "null",
                    "null",
                    "$2a$04$AYaJRPp3uQ4PZ.Pj95Z/J.eCnpwKJ0v1eWm5aKPqcy4kEzFHGK932",
                    new PlayerProfile(),
                    Status.admin
            );
        }
        SqlRowSet set = template.queryForRowSet(
                "   SELECT \n" +
                        "    p.player_id,\n" +
                        "    acc.email,\n" +
                        "    acc.password,\n" +
                        "    pr.name,\n" +
                        "    pr.color,\n" +
                        "    pr.highlight,\n" +
                        "    ch.name AS 'character',\n" +
                        "    ic.name AS 'icon',\n" +
                        "    pr.global,\n" +
                        "    `status`.name AS 'status',\n" +
                        "    prv.end\n" +
                        "FROM\n" +
                        "    account acc\n" +
                        "        JOIN\n" +
                        "    `player` p ON p.player_id = acc.player_id\n" +
                        "        LEFT JOIN\n" +
                        "    `profile` pr ON pr.id = p.last_profile\n" +
                        "        LEFT JOIN\n" +
                        "    `icon` ic ON ic.id = pr.icon_type\n" +
                        "        LEFT JOIN\n" +
                        "    `character` ch ON ch.id = pr.character_type\n" +
                        "        LEFT JOIN\n" +
                        "    `privilege` prv ON prv.player_id = p.player_id\n" +
                        "        LEFT JOIN\n" +
                        "    `status` ON `status`.id = prv.status_id\n" +
                        "WHERE\n" +
                        "    acc.email = :email"
                ,
                new MapSqlParameterSource("email", mail)
        );
        if (set.next()) {
            BombDashUser.BombDashUserBuilder builder = BombDashUser.builder();
            builder
                    .id(set.getString("player_id"))
                    .email(set.getString("email"))
                    .password(set.getString("password"));
            String statusStr = set.getString("status");
            Status status;
            if (statusStr == null)
                status = Status.user;
            else {
                try {
                    status = Status.valueOf(statusStr);
                } catch (Exception ex) {
                    status = Status.user;
                }
            }
            builder.status(status);
            PlayerProfile profile = Extractors.extractProfile(set);
            builder.last(profile);
            builder.template(template);
            return builder.build();
        } else
            throw new UsernameNotFoundException("User not found");
    }
}
