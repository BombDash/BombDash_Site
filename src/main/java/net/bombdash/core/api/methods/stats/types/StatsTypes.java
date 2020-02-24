package net.bombdash.core.api.methods.stats.types;

import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StatsTypes extends AbstractExecutor<Object, List<String>> {
    @Override
    public List<String> execute(Object json, BombDashUser user) {
        return getQueries().getTemplate().getJdbcTemplate().query("SELECT name from score_type", set -> {
            List<String> types = new ArrayList<>();
            types.add("all");
            while (set.next()) {
                types.add(set.getString(1));
            }
            return types;
        });
    }
}
