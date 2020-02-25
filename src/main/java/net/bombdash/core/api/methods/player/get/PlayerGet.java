package net.bombdash.core.api.methods.player.get;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.api.models.BanInfo;
import net.bombdash.core.api.models.Particle;
import net.bombdash.core.api.models.PlayerProfile;
import net.bombdash.core.api.models.Prefix;
import net.bombdash.core.database.Extractors;
import net.bombdash.core.other.Utils;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PlayerGet extends AbstractExecutor<PlayerGetRequest, PlayerGetResponse> {
    private PlayerGetQuery extractor = new PlayerGetQuery();
    private PrefixExtractor prefixExtractor = new PrefixExtractor();
    private ScoreExtractor scoreExtractor = new ScoreExtractor();
    private final String basicSql = "SELECT  " +
            "    p.player_id, " +
            "    p.first_play, " +
            "    p.last_ping, " +
            "    server.name as 'server_name', " +
            "    IF(ban.player_id IS NULL, 0, 1) AS 'ban', " +
            "    ban.reason AS 'ban_reason', " +
            "    ban.end AS 'ban_end', " +
            "    ban.banner AS 'ban_banner', " +
            "    s.name AS 'status', " +
            "    IF(cp.player_id IS NULL, 0, 1) AS 'clan', " +
            "    c.name AS 'clan_name', " +
            "    c.prefix AS 'clan_prefix', " +
            "    IF(pref.player_id IS NULL, 0, 1) AS 'prefix', " +
            "    pref.text AS 'prefix_text', " +
            "    pref.speed as 'prefix_speed', " +
            "    IF(par.player_id IS NULL, 0, 1) AS 'particle', " +
            "    par.particle_type, " +
            "    par.emit_type 'particle_emit_type'" +
            "FROM " +
            "    `player` p " +
            "        LEFT JOIN " +
            "     `server` on server.id = p.last_server " +
            "        LEFT JOIN " +
            "    `banlist` ban ON ban.player_id = p.player_id " +
            "        LEFT JOIN " +
            "    `privilege` prv ON prv.player_id = p.player_id " +
            "        LEFT JOIN " +
            "    `status` s ON s.id = prv.status_id " +
            "        LEFT JOIN " +
            "    `clan_person` cp ON cp.player_id = p.player_id " +
            "        LEFT JOIN " +
            "    `clan` c ON c.id = cp.clan_id " +
            "        LEFT JOIN " +
            "    `prefix` pref ON pref.player_id = p.player_id " +
            "        LEFT JOIN " +
            "    `particle` par ON par.player_id = p.player_id " +
            "WHERE " +
            "    p.player_id = :player";

    @Override
    public PlayerGetResponse execute(PlayerGetRequest request, BombDashUser user) throws MethodExecuteException {
        NamedParameterJdbcTemplate template = getQueries().getTemplate();
        if (request.getId() == null)
            throw new MethodExecuteException(3, "Id can't be null");
        Map<String, Object> playerMap = Utils.getMap("player", request.getId());
        PlayerGetResponse.PlayerGetResponseBuilder response = template.query(basicSql, playerMap, extractor);
        if (response != null) {
            if (request.getFields() != null) {
                for (PlayerGetRequest.PlayerGetField field : request.getFields()) {
                    if (field == null)
                        continue;
                    switch (field) {
                        case profiles:
                            response.profiles(extractProfiles(template.queryForRowSet("SELECT  " +
                                            "pr.name, " +
                                            "pr.color, " +
                                            "pr.highlight, " +
                                            "c.name as 'character', " +
                                            "i.name as 'icon', " +
                                            "pr.global " +
                                            "FROM `profile` pr " +
                                            "join `character` c on c.id = pr.character_type " +
                                            "join `icon` i on i.id = pr.icon_type where pr.player_id = :player",
                                    new MapSqlParameterSource(playerMap))));
                            break;
                        case last_profile:
                            SqlRowSet set = template.queryForRowSet("SELECT  " +
                                            "pr.name, " +
                                            "pr.color, " +
                                            "pr.highlight, " +
                                            "c.name as 'character', " +
                                            "i.name as 'icon', " +
                                            "pr.global " +
                                            "FROM `player` p " +
                                            "left join `profile` pr on pr.id = p.last_profile " +
                                            "left join `character` c on c.id = pr.character_type " +
                                            "left join `icon` i on i.id = pr.icon_type where p.player_id = :player",
                                    playerMap);
                            if (set.next())
                                response.lastProfile(Extractors.extractProfile(set));
                            break;
                        case scores:
                            Map<String, Integer> scores = template.query("SELECT  " +
                                    "    t.name, s.value " +
                                    "FROM " +
                                    "    score s " +
                                    "        JOIN " +
                                    "    score_type t ON t.id = s.score_type_id " +
                                    "where s.player_id = :player order by t.id", playerMap, scoreExtractor);
                            response.score(scores);
                            break;
                        case month_score:
                            Map<String, Integer> monthScores = template.query("SELECT  " +
                                    "    t.name, s.value " +
                                    "FROM " +
                                    "    month_score s " +
                                    "        JOIN " +
                                    "    score_type t ON t.id = s.score_type_id " +
                                    "where s.player_id = :player order by t.id", playerMap, scoreExtractor);
                            response.monthScore(monthScores);
                            break;
                        case last_games:
                            SqlRowSet lastGamesRows = template.queryForRowSet(
                                    "SELECT \n" +
                                            "    v.num,\n" +
                                            "    gh.id,\n" +
                                            "    gh.date,\n" +
                                            "    s.name AS 'server',\n" +
                                            "    ghr.player_id,\n" +
                                            "    pr.name,\n" +
                                            "    pr.color,\n" +
                                            "    pr.highlight,\n" +
                                            "    ch.name AS 'character',\n" +
                                            "    ic.name AS 'icon',\n" +
                                            "    pr.global\n" +
                                            "FROM\n" +
                                            "    `game_history` gh\n" +
                                            "        JOIN\n" +
                                            "    `game_history_row` ghr ON ghr.game_history_id = gh.id\n" +
                                            "        JOIN\n" +
                                            "    `server` s ON s.id = gh.server_id\n" +
                                            "        JOIN\n" +
                                            "    `player` p ON p.player_id = ghr.player_id\n" +
                                            "        LEFT JOIN\n" +
                                            "    `profile` pr ON pr.id = p.last_profile\n" +
                                            "        LEFT JOIN\n" +
                                            "    `icon` ic ON ic.id = pr.icon_type\n" +
                                            "        LEFT JOIN\n" +
                                            "    `character` ch ON ch.id = pr.character_type\n" +
                                            "        JOIN\n" +
                                            "    (SELECT \n" +
                                            "        @rownum:=@rownum + 1 AS 'num', game_history_id\n" +
                                            "    FROM\n" +
                                            "        game_history_row ghr\n" +
                                            "    CROSS JOIN (SELECT @rownum:=-1) r\n" +
                                            "    WHERE\n" +
                                            "        player_id = :player\n" +
                                            "    ORDER BY ghr.game_history_id DESC\n" +
                                            "    LIMIT 5) v ON v.game_history_id = gh.id\n" +
                                            "ORDER BY id DESC",
                                    playerMap);
                            List<PlayerGetResponse.Game> games = new ArrayList<>();
                            while (lastGamesRows.next()) {
                                int num = (int) lastGamesRows.getDouble("num");
                                PlayerGetResponse.Game game;
                                if (games.size() - 1 == num) {
                                    game = games.get(num);
                                } else {
                                    game = new PlayerGetResponse.Game(
                                            lastGamesRows.getString("server"),
                                            lastGamesRows.getTimestamp("date").getTime() / 1000
                                    );
                                    games.add(game);
                                }
                                Map<String, PlayerProfile> profiles = game.getPlayers();
                                PlayerProfile profile = Extractors.extractProfile(lastGamesRows);
                                String id = lastGamesRows.getString("player_id");
                                profiles.put(id, profile);
                            }
                            response.games(games);
                    }
                }
            }
            return response.build();
        } else
            throw new MethodExecuteException(1, "Player not found");
    }

    private List<PlayerProfile> extractProfiles(SqlRowSet set) {
        List<PlayerProfile> list = new ArrayList<>();
        while (set.next()) {
            list.add(Extractors.extractProfile(set));
        }
        return list;
    }

    private class PlayerGetQuery implements ResultSetExtractor<PlayerGetResponse.PlayerGetResponseBuilder> {

        @Override
        public PlayerGetResponse.PlayerGetResponseBuilder extractData(ResultSet set) throws DataAccessException, SQLException {
            PlayerGetResponse.PlayerGetResponseBuilder response = PlayerGetResponse.builder();
            if (set.next()) {
                String playerId = set.getString("player_id");
                response.status(set.getString("status"));
                response.firstPlay(set.getTimestamp("first_play").getTime() / 1000);
                response.lastPing(set.getTimestamp("last_ping").getTime() / 1000);
                String server = set.getString("server_name");
                if (server != null)
                    response.lastServer(server);
                if (Utils.parseBoolean(set.getInt("ban"))) {
                    Timestamp ban_end = set.getTimestamp("ban_end");
                    Long end;
                    if (ban_end == null)
                        end = null;
                    else
                        end = ban_end.getTime() / 1000L;
                    BanInfo info = new BanInfo(
                            set.getString("ban_reason"),
                            end,
                            set.getString("ban_banner")
                    );
                    response.ban(info);
                }
                if (Utils.parseBoolean(set.getInt("clan"))) {
                    PlayerGetResponse.Clan clan = new PlayerGetResponse.Clan(
                            set.getString("clan_name"),
                            set.getString("clan_prefix")
                    );
                    response.clan(clan);
                }
                if (Utils.parseBoolean(set.getInt("prefix"))) {
                    Prefix prefix = new Prefix(
                            set.getString("prefix_text"),
                            set.getInt("prefix_speed"),
                            getQueries().getTemplate().query(
                                    "SELECT color FROM prefix_animation where player_id = :player order by id",
                                    Utils.getMap("player", playerId),
                                    prefixExtractor
                            )
                    );
                    response.prefix(prefix);
                }
                if (Utils.parseBoolean(set.getInt("particle"))) {
                    Particle particle = new Particle(
                            set.getString("particle_type"),
                            set.getString("particle_emit_type")
                    );
                    response.particle(particle);
                }


            } else
                return null;
            return response;
        }
    }

    private class PrefixExtractor implements ResultSetExtractor<List<Integer>> {

        @Override
        public List<Integer> extractData(ResultSet resultSet) throws DataAccessException, SQLException {
            List<Integer> list = new ArrayList<>(resultSet.getFetchSize());
            while (resultSet.next()) {
                int colorInt = resultSet.getInt("color");

                list.add(colorInt);
            }
            return list;
        }
    }

    private class ScoreExtractor implements ResultSetExtractor<Map<String, Integer>> {
        @Override
        public Map<String, Integer> extractData(ResultSet set) throws DataAccessException, SQLException {
            Map<String, Integer> scores = new HashMap<>();
            while (set.next()) {
                scores.put(set.getString("name"), set.getInt("value"));
            }
            return scores;
        }
    }
}
