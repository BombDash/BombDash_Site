package net.bombdash.core.api.methods.player.get;

import lombok.Value;

@Value
public class PlayerGetRequest {
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
