package net.bombdash.core.api.methods.player.check;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class PlayerCheckRequest {
    private List<String> players;

    public PlayerCheckRequest(String... players) {
        this.players = Arrays.asList(players);
    }
}
