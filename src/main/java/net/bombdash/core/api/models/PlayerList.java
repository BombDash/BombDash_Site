package net.bombdash.core.api.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
public class PlayerList<T> {
    private int count;

    private List<PlayerListRow> players = new ArrayList<>();

    public PlayerListRow addRow(String id, int place, T data, PlayerProfile profile) {
        PlayerListRow row = new PlayerListRow(id, place, data, profile);
        players.add(row);
        return row;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Value
    public class PlayerListRow {
        private String id;
        private int place;
        private T data;
        private PlayerProfile profile;
    }
}
