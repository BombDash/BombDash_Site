package net.bombdash.core.api.methods.stats.place;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StatsPlaceRequest {
    /**
     * ID Игрока которого нужно получить
     */
    private String id;
    /**
     * Какие очки нужны
     */
    private boolean global;
    private String type;
}
