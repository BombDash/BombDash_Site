package net.bombdash.core.api.methods.account.verify;

import net.bombdash.core.api.annotations.ProtectedMethod;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.Set;

import static net.bombdash.core.api.methods.account.verify.AccountVerifyResponse.AccountVerifyResponseCode.*;

@ProtectedMethod
@Component
public class AccountVerify extends AbstractExecutor<AccountVerifyRequest, AccountVerifyResponse> {

    @Override
    public AccountVerifyResponse execute(AccountVerifyRequest json, BombDashUser user) {
        NamedParameterJdbcTemplate jdbc = getQueries().getTemplate();
        MapSqlParameterSource source = new MapSqlParameterSource("player_id",
                json.getId());
        String currentAccountEmail = jdbc.query(
                "SELECT email from account where player_id = :player_id", source,
                (result) -> {
                    if (result.next())
                        return result.getString(1);
                    else
                        return null;
                });
        if (currentAccountEmail != null)
            return new AccountVerifyResponse(registered);
        Set<String> profiles = json
                .getProfiles()
                .keySet();
        if(profiles.size()==0)
            return new AccountVerifyResponse(no_code);
        SqlRowSet set = jdbc.queryForRowSet(
                "SELECT id " +
                        "from registration " +
                        "where verification_code in (:profiles)",
                new MapSqlParameterSource("profiles", profiles));
        int rowcount = 0;
        if (set.last()) {
            rowcount = set.getRow();
            set.beforeFirst();
        }
        if (rowcount > 1) {
            return new AccountVerifyResponse(many_codes);
        } else if (rowcount == 0) {
            return new AccountVerifyResponse(no_code);
        } else if (set.next()) {
            int id = set.getInt("id");
            source.addValue("id", id);
            jdbc.update(
                    "INSERT INTO account (player_id, email, password) " +
                            "SELECT :player_id, email, password from registration where id = :id",
                    source
            );
            jdbc.update("DELETE FROM registration where id = :id", source);
            return new AccountVerifyResponse(ok);
        } else
            return new AccountVerifyResponse(server_error);
    }
}
