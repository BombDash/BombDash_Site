package net.bombdash.core.api.methods.player.get;

import lombok.Data;

@Data
public class PlayerGetRequest {
    public PlayerGetRequest(String id, PlayerGetField... fields) {
        this.id = id;
        this.fields = fields;
    }

    /**
     * id игрока которого нужно получить
     */
    public String id;
    /**
     * Доп поля
     */
    public PlayerGetField[] fields;

    public enum PlayerGetField {
        last_profile, scores, month_score, profiles, last_games
    }
}
