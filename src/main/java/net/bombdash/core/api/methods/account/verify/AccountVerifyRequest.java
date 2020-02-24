package net.bombdash.core.api.methods.account.verify;

import lombok.Data;
import net.bombdash.core.api.methods.stats.update.StatsUpdateRequest;

import java.util.Map;

@Data
public class AccountVerifyRequest {
    /**
     * ID игрока которого нужно проверить
     */
    private String id;
    /**
     * Его профили
     */
    private Map<String, StatsUpdateRequest.RawPlayerProfile> profiles;
}
