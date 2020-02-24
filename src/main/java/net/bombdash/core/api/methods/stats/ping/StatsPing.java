package net.bombdash.core.api.methods.stats.ping;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.annotations.ProtectedMethod;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;

@ProtectedMethod
@Component
public class StatsPing extends AbstractExecutor<StatsPingRequest, Object> {
    @Override
    public Object execute(StatsPingRequest json, BombDashUser user) throws MethodExecuteException {
        List<String> ids = json.getIds();
        if (ids == null)
            throw new MethodExecuteException(3,"Ids can't be null");
        SqlParameterSource[] params = new SqlParameterSource[ids.size()];
        String serverName = json.getServerName();
        Integer serverId;
        if (serverName == null)
            serverId = null;
        else
            serverId = getQueries().getServerId(serverName);
        for (int i = 0; i < ids.size(); i++) {
            params[i] = new MapSqlParameterSource()
                    .addValue("player", ids.get(i))
                    .addValue("server", serverId);
        }
        getQueries()
                .getTemplate()
                .batchUpdate("UPDATE player SET last_ping = CURRENT_TIMESTAMP,last_server = :server WHERE player_id = :player", params);
        return new Object();
    }
}
