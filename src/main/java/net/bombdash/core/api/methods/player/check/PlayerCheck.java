package net.bombdash.core.api.methods.player.check;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PlayerCheck extends AbstractExecutor<PlayerCheckRequest, Object> {
    @Override
    public Object execute(PlayerCheckRequest json, BombDashUser user) throws MethodExecuteException {
        int[] players = json.getPlayers();
        if (players == null || players.length == 0)
            throw new MethodExecuteException(3, "Array is empty");
        NamedParameterJdbcTemplate template = getQueries().getTemplate();
        MapSqlParameterSource source = new MapSqlParameterSource("players", players);
        template
                .update(
                        "DELETE FROM banlist  " +
                                "WHERE player_id in (:players) AND " +
                                "end IS NOT NULL AND " +
                                "end <= CURRENT_TIMESTAMP",
                        source);
        template
                .update(
                        "DELETE FROM privilege " +
                                "WHERE player_id in (:players) AND " +
                                "end IS NOT NULL AND " +
                                "end <= CURRENT_TIMESTAMP",
                        source
                );
        return new Object();
    }
}
