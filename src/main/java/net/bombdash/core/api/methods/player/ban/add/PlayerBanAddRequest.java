package net.bombdash.core.api.methods.player.ban.add;

import lombok.Value;

@Value
public class PlayerBanAddRequest {
    private String id;
    private String reason;
    private String operator;
    private Integer end;

    public long getEndCurrentTime() {
        if (end == null)
            return -1;
        long stamp = System.currentTimeMillis() / 1000L;
        stamp += end * 24 * 60 * 60;
        return stamp;
    }
}
