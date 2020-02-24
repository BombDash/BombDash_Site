package net.bombdash.core.api.methods.player.check;

import lombok.Data;

@Data
public class PlayerCheckRequest {
    public PlayerCheckRequest(int... players) {
        this.players = players;
    }

    private int[] players;
}
