package net.bombdash.core.api.methods.player.check;

import lombok.Data;

@Data
public class PlayerCheckRequest {
    private String[] players;

    public PlayerCheckRequest(String... players) {
        this.players = players;
    }
}
