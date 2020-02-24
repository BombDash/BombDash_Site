package net.bombdash.core.api.methods.player.ban.add;

import net.bombdash.core.api.annotations.ProtectedMethod;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

@ProtectedMethod
@Component
public class PlayerBanAdd extends AbstractExecutor<PlayerBanAddRequest, Object> {
    @Override
    public Object execute(PlayerBanAddRequest json, BombDashUser user) {
        getQueries()
                .getTemplate()
                .update("INSERT INTO banlist (player_id, reason, banner, end) " +
                                "values (:id,:reason,:operator,FROM_UNIXTIME(:end))" +
                                " ON DUPLICATE KEY " +
                                " UPDATE reason = :reason,banner = :operator, end = FROM_UNIXTIME(:end)",
                        new MapSqlParameterSource()
                                .addValue("id", json.getId())
                                .addValue("reason", json.getReason())
                                .addValue("operator", json.getOperator())
                                .addValue("end", json.getEndCurrentTime())
                );
        return new Object();
    }
}
