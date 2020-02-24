package net.bombdash.core.api.methods.player.get;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import net.bombdash.core.api.models.BanInfo;
import net.bombdash.core.api.models.PlayerProfile;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Builder
@Value
public class PlayerGetResponse {
    /**
     * Если нет привилегии то NULL
     */
    private String status;

    @SerializedName("first_play")
    private long firstPlay;

    @SerializedName("last_ping")
    private long lastPing;

    @SerializedName("last_server")
    private String lastServer;

    /**
     * Если нет бана то NULL
     */
    private BanInfo ban;
    /**
     * Собсна если нет префикса то логично что нулл,
     * вдруг чел с привилегией не захочет префикс,
     * или мы сможем устанавливать префикс без привилегии
     */

    private Prefix prefix;
    /**
     * Партиклы вокруг перса
     * Собсна так же как и префикс
     */
    private Particle particle;
    /**
     * Клан игрока
     */
    private Clan clan;
    /**
     * Послений профиль
     */
    @SerializedName("last_profile")
    private PlayerProfile lastProfile;
    /**
     * Все профили
     */
    private List<PlayerProfile> profiles;
    /**
     * История игр
     */
    private List<Game> games;

    /**
     * Глобальные очки
     */
    private Map<String, Integer> score;
    /**
     * Очки за этот месяц
     */
    private Map<String, Integer> monthScore;

    @Value
    public static class Prefix {
        private String text;
        private int speed;
        private List<Double[]> animation;

    }

    @Value
    public static class Particle {
        @SerializedName("particle_type")
        private String particleType;
        @SerializedName("emit_type")
        private String emitType;
    }

    @Value
    public static class Clan {
        /**
         * Имя клана
         */
        private String name;
        /**
         * Префикс клана
         */
        private String prefix;
    }

    @Data
    public static class Game {
        private final long time;

        public Game(String server, long time) {
            this.server = server;
            this.time = time;
        }

        String server;
        Map<String, PlayerProfile> players = new LinkedHashMap<>();
    }
}
