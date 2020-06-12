package net.bombdash.core.api.methods.player.check;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.MethodExecuteExceptionCode;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerCheck extends AbstractExecutor<PlayerCheckRequest, Object> {

    @Override
    public Object execute(PlayerCheckRequest json, BombDashUser user) throws MethodExecuteException {
        check(json);
        return new Object();
    }

    @Async
    public void check(PlayerCheckRequest json) throws MethodExecuteException {
        List<String> players = json.getPlayers();
        if (players == null || players.size() == 0)
            throw new MethodExecuteException(MethodExecuteExceptionCode.FIELD_MISSING, "Array is empty");
        NamedParameterJdbcTemplate template = getQueries().getTemplate();
        MapSqlParameterSource source = new MapSqlParameterSource("players", players);
        int count = template.update(
                "INSERT INTO " +
                        "banlist_history (player_id, reason, banner, end) " +
                        "SELECT player_id, reason, banner, end from banlist " +
                        "WHERE player_id in (:players) AND " +
                        "end IS NOT NULL AND " +
                        "end <= CURRENT_TIMESTAMP",
                source);
        if (count > 0)
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
    }
}
