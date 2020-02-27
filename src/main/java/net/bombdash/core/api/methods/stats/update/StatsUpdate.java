package net.bombdash.core.api.methods.stats.update;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.MethodExecuteExceptionCode;
import net.bombdash.core.api.annotations.ProtectedMethod;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.database.Extractors;
import net.bombdash.core.other.Utils;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ProtectedMethod
@Component
public class StatsUpdate extends AbstractExecutor<StatsUpdateRequest, Object> {
    private Map<String, Integer> icons = new HashMap<>();
    private Map<String, Integer> scoreTypes = new HashMap<>();
    private Map<String, Integer> characters = new HashMap<>();


    @Override
    public Object execute(StatsUpdateRequest request, BombDashUser user) throws MethodExecuteException {
        if (request.getPlayers() == null)
            throw new MethodExecuteException(MethodExecuteExceptionCode.FIELD_MISSING, "Players array can't be null");
        List<String> processedPlayers = new ArrayList<>();
        for (StatsUpdateRequest.PlayerTop player : request.getPlayers()) {
            if (processedPlayers.contains(player.getId()))
                continue;
            getQueries().addPlayerIfNotExists(player.getId());
            player.getScores().forEach((name, value) -> addScore(player.getId(), name, value));
            addAllScores(player);
            updateProfiles(player.getId(), player.getProfiles(), player.getLastProfile(), player.getDevice());
            processedPlayers.add(player.getId());
        }

        addGame(
                request.getServer(),
                processedPlayers
        );
        return new Object();
    }

    private void addAllScores(StatsUpdateRequest.PlayerTop player) {
        Map<String, Integer> s = player.getScores();
        int all =
                s.getOrDefault("ffa_points", 0) +
                        s.getOrDefault("kills", 0) +
                        (4 * (s.getOrDefault("teams_points", 0)));
        addScore(player.getId(), "all", all);
    }


    public void addScore(String id, String name, Integer value) {
        Map<String, Object> param = Utils.getMap(
                "score", name,
                "value", value,
                "player", id
        );
        Integer scoreTypeId = scoreTypes.get(name);
        if (scoreTypeId == null) {
            getQueries().getTemplate().update("INSERT IGNORE INTO score_type (name) VALUES (:score)", param);
            scoreTypeId = getQueries().getTemplate().query("SELECT score_type.id from score_type where name = :score", param, Extractors.firstIntExtractor);
            scoreTypes.put(name, scoreTypeId);
        }
        param.put("score_type_id", scoreTypeId);
        Integer scoreId = getQueries().getTemplate().query("SELECT score.id from score where player_id = :player and score_type_id = :score_type_id", param, Extractors.firstIntExtractor);
        Integer monthScoreId = getQueries().getTemplate().query("SELECT month_score.id from month_score where player_id = :player and score_type_id = :score_type_id", param, Extractors.firstIntExtractor);
        if (scoreId == null) {
            getQueries().getTemplate().update(
                    "INSERT INTO score (value, score_type_id, player_id) " +
                            "VALUES (:value, :score_type_id, :player)", param);
        } else {
            param.put("score_id", scoreId);
            getQueries().getTemplate().update("UPDATE score set value = value + :value where score.id = :score_id", param);
        }
        if (monthScoreId == null) {
            getQueries().getTemplate().update(
                    "INSERT INTO month_score (value, score_type_id, player_id) " +
                            "VALUES (:value, :score_type_id, :player)", param);
        } else {
            param.put("score_id", monthScoreId);
            getQueries().getTemplate().update("UPDATE month_score set value = value + :value where month_score.id = :score_id", param);
        }
    }

    public void updateProfiles(
            String playerId,
            Map<String, StatsUpdateRequest.RawPlayerProfile> profiles,
            String lastProfile, String device) throws MethodExecuteException {
        Map<String, Object> map = Utils.getMap("player", playerId);
        getQueries().getTemplate().update("UPDATE player set player.last_profile = null where player.player_id = :player; ", map);
        getQueries().getTemplate().update("DELETE FROM profile where player_id = :player;", map);
        String deviceName = device.substring(1);
        char deviceIcon = device.charAt(0);
        Integer accountId = null;
        boolean lastUpdate = false;
        for (Map.Entry<String, StatsUpdateRequest.RawPlayerProfile> entry : profiles.entrySet()) {
            StatsUpdateRequest.RawPlayerProfile profile = entry.getValue();
            Integer charId = characters.get(profile.getCharacter());
            if (charId == null) {
                Map<String, Object> charMap = Utils.getMap("character", profile.getCharacter());
                getQueries().getTemplate().update("INSERT IGNORE INTO `character` (name) values (:character)", charMap);
                charId = getQueries().getTemplate().query("SELECT id from `character` where name = :character", charMap, Extractors.firstIntExtractor);
                characters.put(profile.getCharacter(), charId);
            }
            if (entry.getKey().equals("__account__")) {
                map.put("name", deviceName);
                map.put("icon", getIconId(PlayerProfileDeserializer.getIconName(deviceIcon)));
                map.put("global", 0);
                accountId = -1;
            } else {
                Integer iconId = icons.get(profile.getIcon());
                if (iconId == null) {
                    iconId = getIconId(profile.getIcon());
                }
                map.put("name", entry.getKey());
                if (profile.isGlobal())
                    map.put("icon", iconId);
                else
                    map.put("icon", getIconId("logo"));
                map.put("global", profile.isGlobal() ? 1 : 0);
            }
            map.put("char_id", charId);
            map.put("color", Utils.colorToRgb(profile.getColor()));
            map.put("highlight", Utils.colorToRgb(profile.getHighlight()));


            GeneratedKeyHolder key = new GeneratedKeyHolder();
            getQueries().getTemplate().update(
                    "INSERT INTO profile (player_id, name, color, highlight, character_type, icon_type, global) VALUES " +
                            "(:player, :name, :color, :highlight, :char_id, :icon, :global)",
                    new MapSqlParameterSource(map), key, new String[]{"id"});
            int profileId;
            try {
                profileId = key.getKey().intValue();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new MethodExecuteException(228, "Some error, im dont know why");
            }
            if (accountId != null && accountId.equals(-1))
                accountId = profileId;
            if ((entry.getKey().equals(lastProfile) || entry.getKey().substring(1).equals(lastProfile)) && !lastUpdate) {
                updateLast(profileId, playerId);
                lastUpdate = true;
            }
        }
        if (!lastUpdate && (accountId != null && accountId > 0)) {
            updateLast(accountId, playerId);
        }
    }

    private void updateLast(int profileId, String playerId) {
        getQueries().getTemplate().update("UPDATE player SET last_profile = :last where player_id = :player",
                new MapSqlParameterSource()
                        .addValue("last", profileId)
                        .addValue("player", playerId));
    }

    private Integer getIconId(String iconName) {
        Map<String, Object> iconMap = Utils.getMap("icon", iconName);
        getQueries().getTemplate().update("INSERT IGNORE INTO `icon` (name) values (:icon)", iconMap);
        Integer charId = getQueries().getTemplate().query("SELECT id from `icon` where name = :icon", iconMap, Extractors.firstIntExtractor);
        if (charId == null)
            throw new IllegalArgumentException("Impossible");
        icons.put(iconName, charId);
        return charId;
    }


    public void addGame(String server, List<String> players) {
        int serverId = getQueries().getServerId(server);
        GeneratedKeyHolder key = new GeneratedKeyHolder();
        getQueries().getTemplate().update(
                "INSERT INTO game_history (server_id) values (:server)",
                new MapSqlParameterSource(Utils.getMap("server", serverId)),
                key,
                new String[]{"id"});
        int gameHistoryId = key.getKey().intValue();
        SqlParameterSource[] params = new SqlParameterSource[players.size()];
        for (int i = 0; i < players.size(); i++) {
            params[i] = new MapSqlParameterSource()
                    .addValue("game_history", gameHistoryId)
                    .addValue("player", players.get(i));
        }
        getQueries()
                .getTemplate()
                .batchUpdate("INSERT INTO game_history_row (game_history_id, player_id) values (:game_history,:player)", params);
    }
}
