package net.bombdash.core.api.methods.player.online;

import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.api.models.PlayerList;
import net.bombdash.core.database.Extractors;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PlayerOnline extends AbstractExecutor<Void, Map<String, PlayerList<Void>>> {
    @Override
    public Map<String, PlayerList<Void>> execute(Void json, BombDashUser user) {
        NamedParameterJdbcTemplate template = getQueries().getTemplate();
        SqlRowSet set = template.getJdbcTemplate().queryForRowSet(
                "SELECT \n" +
                        "    p.player_id,\n" +
                        "    p.last_ping,\n" +
                        "    s.name AS 'server',\n" +
                        "    pr.name,\n" +
                        "    pr.color,\n" +
                        "    pr.highlight,\n" +
                        "    c.name AS `character`,\n" +
                        "    i.name AS `icon`,\n" +
                        "    pr.global\n" +
                        "FROM\n" +
                        "    player p\n" +
                        "        JOIN\n" +
                        "    server s ON s.id = p.last_server\n" +
                        "        LEFT JOIN\n" +
                        "    `profile` pr ON p.last_profile = pr.id\n" +
                        "        LEFT JOIN\n" +
                        "    `character` c ON c.id = pr.character_type\n" +
                        "        LEFT JOIN\n" +
                        "    `icon` i ON i.id = pr.icon_type\n" +
                        "WHERE\n" +
                        "    (CURRENT_TIMESTAMP() - last_ping) < (5 * 60)\n" +
                        "ORDER BY last_ping,pr.name DESC"
        );
        Map<String, PlayerList<Void>> map = new HashMap<>();
        Map<String, Long> differentMap = new HashMap<>();
        while (set.next()) {
            String server = set.getString("server");
            long currentTime = set.getTimestamp("last_ping").getTime() / 1000;
            long time = differentMap.computeIfAbsent(server, s -> currentTime);
            if (currentTime - time > 5) {
                continue;
            }
            PlayerList<Void> list = map.computeIfAbsent(server, s -> new PlayerList<>(-1));
            list.addRow(
                    set.getString("player_id"),
                    set.getRow(),
                    null,
                    Extractors.extractProfile(set));
        }
        return map;
    }
}
