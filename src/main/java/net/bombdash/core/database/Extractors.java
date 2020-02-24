package net.bombdash.core.database;

import net.bombdash.core.api.models.PlayerProfile;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.awt.*;

public class Extractors {
    public interface RowSetExtractor<T> {
        T extract(SqlRowSet set);
    }


    public static final ResultSetExtractor<Integer> firstIntExtractor = resultSet -> {
        if (resultSet.next())
            return resultSet.getInt(1);
        else
            return null;
    };

    public static PlayerProfile extractProfile(SqlRowSet set) {
        return extractProfile(set, null);
    }

    public static PlayerProfile extractProfile(SqlRowSet set, String prefix) {
        PlayerProfile.PlayerProfileBuilder builder = PlayerProfile.builder();
        if (prefix == null)
            prefix = "";
        else
            prefix = prefix + "_";
        String name = set.getString(prefix + "name");
        if (name == null) {
            return builder
                    .name("Null")
                    .color(Color.GRAY.getRGB())
                    .highlight(Color.GRAY.getRGB())
                    .character("neoSpaz")
                    .icon("bomb")
                    .global(false)
                    .build();
        } else {
            builder.name(name);
            return PlayerProfile.builder()
                    .name(name)
                    .color(set.getInt(prefix + "color"))
                    .highlight(set.getInt(prefix + "highlight"))
                    .character(set.getString(prefix + "character"))
                    .icon(set.getString(prefix + "icon"))
                    .global(set.getBoolean(prefix + "global"))
                    .build();
        }
    }
}
