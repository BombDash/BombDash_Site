package net.bombdash.core.api.methods.player.search;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.api.methods.player.get.PlayerGet;
import net.bombdash.core.api.methods.player.get.PlayerGetRequest;
import net.bombdash.core.api.methods.player.get.PlayerGetResponse;
import net.bombdash.core.api.models.PlayerList;
import net.bombdash.core.api.models.PlayerProfile;
import net.bombdash.core.database.Extractors;
import net.bombdash.core.other.Utils;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PlayerSearch extends AbstractExecutor<PlayerSearchRequest, PlayerList<List<PlayerProfile>>> {
    @Autowired
    private PlayerGet get;

    @Override
    public PlayerList<List<PlayerProfile>> execute(PlayerSearchRequest json, BombDashUser user) throws MethodExecuteException {
        String nick = json.getNick();
        nick = nick
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_")
                .replace("[", "![");
        nick = "%" + nick + "%";
        JdbcTemplate template = getQueries().getTemplate().getJdbcTemplate();
        SqlRowSet set = template.queryForRowSet(
                "SELECT \n" +
                        "    t.*,\n" +
                        "    pr.name AS 'm_name',\n" +
                        "    pr.color AS 'm_color',\n" +
                        "    pr.highlight AS 'm_highlight',\n" +
                        "    c.name AS `m_character`,\n" +
                        "    i.name AS `m_icon`,\n" +
                        "    pr.global AS 'm_global'\n" +
                        "FROM\n" +
                        "    (SELECT \n" +
                        "        pr.player_id,\n" +
                        "            pr.name,\n" +
                        "            pr.color,\n" +
                        "            pr.highlight,\n" +
                        "            c.name AS `character`,\n" +
                        "            i.name AS `icon`,\n" +
                        "            pr.global\n" +
                        "    FROM\n" +
                        "        `profile` pr\n" +
                        "    LEFT JOIN `character` c ON c.id = pr.character_type\n" +
                        "    LEFT JOIN `icon` i ON i.id = pr.icon_type\n" +
                        "    WHERE\n" +
                        "        pr.name LIKE ? ESCAPE '!'\n" +
                        "    ORDER BY pr.id DESC) t\n" +
                        "        JOIN\n" +
                        "    player ON player.player_id = t.player_id\n" +
                        "        LEFT JOIN\n" +
                        "    profile pr ON pr.id = player.last_profile\n" +
                        "        LEFT JOIN\n" +
                        "    `character` c ON c.id = pr.character_type\n" +
                        "        LEFT JOIN\n" +
                        "    `icon` i ON i.id = pr.icon_type", nick);

        PlayerList<List<PlayerProfile>> list = new PlayerList<>(Utils.getSetSize(set));
        while (set.next()) {
            String id = set.getString("player_id");
            PlayerList<List<PlayerProfile>>.PlayerListRow row = getRow(list, id, set);
            row.getData().add(Extractors.extractProfile(set));
        }
        return list;
    }

    private PlayerList<List<PlayerProfile>>.PlayerListRow getRow(PlayerList<List<PlayerProfile>> list, String id, SqlRowSet set) {
        PlayerList<List<PlayerProfile>>.PlayerListRow row = list.getPlayers()
                .stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (row == null) {
            PlayerProfile profile = Extractors.extractProfile(set, "m");
            row = list.addRow(id, 0, new ArrayList<>(), profile);
        }
        return row;
    }
}
