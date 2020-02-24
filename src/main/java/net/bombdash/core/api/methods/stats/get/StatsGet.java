package net.bombdash.core.api.methods.stats.get;

import lombok.Value;
import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.api.models.PlayerList;
import net.bombdash.core.api.models.PlayerProfile;
import net.bombdash.core.database.Extractors;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.Types;

@SuppressWarnings("SqlResolve")
@Component
public class StatsGet extends AbstractExecutor<StatsGetRequest, PlayerList<StatsGet.StatsGetData>> {

    @Override
    public PlayerList<StatsGetData> execute(StatsGetRequest json, BombDashUser user) throws MethodExecuteException {
        if (json.getType() == null)
            throw new MethodExecuteException(3, "Missing required params");
        int rPage;
        if (json.getPage() == null)
            rPage = 1;
        else
            rPage = json.getPage();
        int page = (rPage - 1) * 100;
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("type", json.getType())
                .addValue("page", new SqlParameterValue(Types.INTEGER, page));
        String tableName;

        if (json.isGlobal()) {
            tableName = "score";
        } else
            tableName = "month_score";
        String sqlRows, sqlCount;
        if (json.getType().equals("all")) {
            sqlRows = "SELECT \n" +
                    "    t.*,\n" +
                    "    pr.name,\n" +
                    "    pr.color,\n" +
                    "    pr.highlight,\n" +
                    "    c.name AS `character`,\n" +
                    "    i.name AS `icon`,\n" +
                    "    pr.global\n" +
                    "FROM\n" +
                    "    (SELECT s.player_id, SUM(s.value * st.k) as score\n" +
                    "FROM "+tableName+" AS s\n" +
                    "INNER JOIN (\n" +
                    "    SELECT id, 1 as k FROM score_type WHERE name IN ('ffa_points', 'kills')\n" +
                    "    UNION \n" +
                    "    SELECT id, 4 as  k FROM score_type WHERE name = 'teams_points'  \n" +
                    ") as st ON (st.id = s.score_type_id)\n" +
                    "GROUP by player_id\n" +
                    "ORDER BY score DESC, s.player_id\n" +
                    "LIMIT 100 OFFSET :page) t\n" +
                    "        JOIN\n" +
                    "    `player` p ON t.player_id = p.player_id\n" +
                    "        LEFT JOIN\n" +
                    "    `profile` pr ON p.last_profile = pr.id\n" +
                    "        LEFT JOIN\n" +
                    "    `character` c ON c.id = pr.character_type\n" +
                    "        LEFT JOIN\n" +
                    "    `icon` i ON i.id = pr.icon_type";
            sqlCount = "SELECT \n" +
                    "    COUNT(DISTINCT player_id)\n" +
                    "FROM " + tableName;
        } else {
            sqlRows = "SELECT \n" +
                    "    p.player_id,\n" +
                    "    s.value AS `score`,\n" +
                    "    pr.name,\n" +
                    "    pr.color,\n" +
                    "    pr.highlight,\n" +
                    "    c.name AS `character`,\n" +
                    "    i.name AS `icon`,\n" +
                    "    pr.global\n" +
                    "FROM\n" +
                    "    " + tableName + " s\n" +
                    "        JOIN\n" +
                    "    `score_type` st ON s.score_type_id = st.id\n" +
                    "        JOIN\n" +
                    "    `player` p ON s.player_id = p.player_id\n" +
                    "        LEFT JOIN\n" +
                    "    `profile` pr ON p.last_profile = pr.id\n" +
                    "       LEFT JOIN\n" +
                    "    `character` c ON c.id = pr.character_type\n" +
                    "       LEFT JOIN\n" +
                    "    `icon` i ON i.id = pr.icon_type\n" +
                    "WHERE\n" +
                    "    st.name = :type\n" +
                    "    ORDER BY s.value desc, s.player_id\n" +
                    "LIMIT 100 OFFSET :page";
            sqlCount = "SELECT  " +
                    "    COUNT(*) " +
                    "FROM " +
                    "    " + tableName + " s " +
                    "        JOIN " +
                    "    score_type st ON s.score_type_id = st.id " +
                    "WHERE " +
                    "    st.name = :type";
        }
        NamedParameterJdbcTemplate template = getQueries().getTemplate();
        Integer count = template.query(sqlCount,
                source,
                Extractors.firstIntExtractor
        );
        if (count == null) {
            throw new MethodExecuteException(4, "Score type " + json.getType() + " does not exists");
        }
        SqlRowSet set;

        set = getQueries().getTemplate().queryForRowSet(sqlRows, source);

        PlayerList<StatsGetData> list = new PlayerList<>(count);

        while (set.next()) {
            PlayerProfile profile = Extractors.extractProfile(set);
            list.addRow(
                    set.getString("player_id"),
                    set.getRow() + page,
                    new StatsGetData(set.getInt("score")),
                    profile
            );
        }
        return list;
    }

    @Value
    public class StatsGetData {
        int score;
    }

}
