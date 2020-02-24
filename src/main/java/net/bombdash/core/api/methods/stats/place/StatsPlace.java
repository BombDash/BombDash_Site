package net.bombdash.core.api.methods.stats.place;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.database.Extractors;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class StatsPlace extends AbstractExecutor<StatsPlaceRequest, StatsPlaceResponse> {
    @Override
    @SuppressWarnings("all")
    public StatsPlaceResponse execute(StatsPlaceRequest json, BombDashUser user) throws MethodExecuteException {
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", json.getId())
                .addValue("type", json.getType());
        String table;
        if (json.isGlobal())
            table = "score";
        else
            table = "month_score";
        Integer place = getQueries().getTemplate().query(
                "select t.num from (\n" +
                        "SELECT \n" +
                        " ROW_NUMBER() OVER (ORDER BY s.value desc,s.player_id) as 'num',\n" +
                        " s.player_id,\n" +
                        "    s.value\n" +
                        "    FROM " + table + "  s\n" +
                        "join score_type st on st.id = s.score_type_id\n" +
                        "where st.name = :type) t where t.player_id = :id",
                source,
                Extractors.firstIntExtractor
        );
        if(place!=null)
            return new StatsPlaceResponse(place);
        else
            return new StatsPlaceResponse(-1);
    }
}
