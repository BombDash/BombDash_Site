package net.bombdash.core.database;

import net.bombdash.core.other.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ConstantConditions")
@Component
public class PreparedQueries {
    @Autowired
    private NamedParameterJdbcTemplate template;

    private Map<String, Integer> servers = new HashMap<>();

    public Integer getServerId(String name) {
        Integer serverId = servers.get(name);
        if (serverId == null) {
            Map<String, Object> map = Utils.getMap("server", name);
            template.update("INSERT IGNORE INTO server (name) VALUES (:server)", map);
            serverId = template.query("SELECT server.id FROM server where server.name = :server", map, Extractors.firstIntExtractor);
            servers.put(name, serverId);
        }
        return serverId;
    }

    public void addPlayerIfNotExists(String playerId) {
        getTemplate().update(
                "INSERT IGNORE INTO player (player_id) VALUES (:player)",
                Utils.getMap("player", playerId));
    }


    public NamedParameterJdbcTemplate getTemplate() {
        return template;
    }
}
