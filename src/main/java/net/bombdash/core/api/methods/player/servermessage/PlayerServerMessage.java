package net.bombdash.core.api.methods.player.servermessage;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class PlayerServerMessage extends AbstractExecutor<PlayerServerMessageRequest, String> {

    @Override
    public String execute(PlayerServerMessageRequest json, BombDashUser user) throws MethodExecuteException {
        if (json.getId() == null || json.getMessage() == null || json.getClass() == null)
            throw new MethodExecuteException(3, "Missing required params");
        int serverId = getQueries().getServerId(json.getServerName());
        getQueries().addPlayerIfNotExists(json.getId());
        getQueries()
                .getTemplate()
                .update("INSERT INTO server_chat (message, `from`, server_id) values (:message, :id,:server)",
                        new MapSqlParameterSource()
                                .addValue("id", json.getId())
                                .addValue("server", serverId)
                                .addValue("message", json.getMessage()));
        return "ok";
    }
}
