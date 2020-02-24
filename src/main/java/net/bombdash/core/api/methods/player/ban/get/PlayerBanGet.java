package net.bombdash.core.api.methods.player.ban.get;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.api.models.BanInfo;
import net.bombdash.core.api.models.PlayerList;
import net.bombdash.core.api.models.PlayerProfile;
import net.bombdash.core.database.Extractors;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class PlayerBanGet extends AbstractExecutor<PlayerBanGetRequest, PlayerList<BanInfo>> {

    @Override
    public PlayerList<BanInfo> execute(PlayerBanGetRequest json, BombDashUser user) throws MethodExecuteException {
        if (json.getPage() < 1)
            throw new MethodExecuteException(2, "Page can't be small 1");
        int page = (json.getPage() - 1) * 100;
        NamedParameterJdbcTemplate template = getQueries().getTemplate();
        Integer count = template.getJdbcTemplate().query("SELECT COUNT(*) from banlist", Extractors.firstIntExtractor);
        if (count == null)
            throw new MethodExecuteException(228, "Ooops");
        SqlRowSet row = template.queryForRowSet(
                "SELECT \n" +
                        "    p.player_id,\n" +
                        "    b.reason,\n" +
                        "    b.banner,\n" +
                        "    b.end,\n" +
                        "    pr.name,\n" +
                        "    pr.color,\n" +
                        "    pr.highlight,\n" +
                        "    c.name AS `character`,\n" +
                        "    i.name AS `icon`,\n" +
                        "    pr.global\n" +
                        "FROM\n" +
                        "    banlist b\n" +
                        "        JOIN\n" +
                        "    `player` p ON b.player_id = p.player_id\n" +
                        "        LEFT JOIN\n" +
                        "    `profile` pr ON p.last_profile = pr.id\n" +
                        "        JOIN\n" +
                        "    `character` c ON c.id = pr.character_type\n" +
                        "        JOIN\n" +
                        "    `icon` i ON i.id = pr.icon_type\n" +
                        "LIMIT 100 OFFSET :page",
                new MapSqlParameterSource("page", page)
        );
        PlayerList<BanInfo> response = new PlayerList<>(count);
        while (row.next()) {
            Timestamp end = row.getTimestamp("end");
            Long endLong;
            if(end==null)
                endLong = null;
            else
                endLong =end.getTime() / 1000L;
            PlayerProfile profile = Extractors.extractProfile(row);
            BanInfo info = new BanInfo(
                    row.getString("reason"),
                    endLong,
                    row.getString("banner"));
            response.addRow(row.getString("player_id"), page + row.getRow(), info, profile);

        }
        return response;
    }
}
