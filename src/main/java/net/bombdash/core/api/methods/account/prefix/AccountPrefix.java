package net.bombdash.core.api.methods.account.prefix;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.MethodExecuteExceptionCode;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.database.Extractors;
import net.bombdash.core.site.auth.BombDashUser;
import net.bombdash.core.site.auth.Status;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.bombdash.core.api.methods.account.prefix.AccountPrefixResponse.Response;

@Component
public class AccountPrefix extends AbstractExecutor<AccountPrefixRequest, AccountPrefixResponse> {
    private CharsetEncoder encoder = StandardCharsets.US_ASCII.newEncoder();
    private Set<Status> disableCheck = Stream.of(Status.admin, Status.moderator).collect(Collectors.toSet());

    @Override
    public AccountPrefixResponse execute(AccountPrefixRequest json, BombDashUser user) throws MethodExecuteException {
        NamedParameterJdbcTemplate template = getQueries().getTemplate();
        if (user == null)
            throw new MethodExecuteException(MethodExecuteExceptionCode.AUTHORIZE_REQUIRED, "Can't execute without user");
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", user.getUsername());
        Integer count = template.query("SELECT COUNT(*) FROM privilege WHERE player_id = :id", source, Extractors.firstIntExtractor);
        if (count == null)
            throw new MethodExecuteException(MethodExecuteExceptionCode.UNKNOWN_ERROR, "Count is null, but why?/");
        if (count == 0) {
            throw new MethodExecuteException(MethodExecuteExceptionCode.ACCESS_DENIED, "You are not vip or higher");
        }
        int speed = json.getSpeed();
        String text = json.getText();
        if (!disableCheck.contains(user.getStatus())) {
            if (!encoder.canEncode(json.getText())) {
                return new AccountPrefixResponse(Response.UTF_8);
            }
            //TODO проверка на админа (строки) я в регулярках не тю тю
            if (speed <= 0 || speed >= 1000) {
                return new AccountPrefixResponse(Response.WRONG_SPEED);
            }
        }
        template.update("DELETE from prefix where player_id = :id", source);
        source
                .addValue("text", text)
                .addValue("speed", speed);
        template.update("INSERT INTO prefix (player_id, text, speed) VALUES (:id,:text,:speed)", source);

        int[] colors = json.getColors();
        MapSqlParameterSource[] sources = new MapSqlParameterSource[json.getColors().length];
        for (int i = 0; i < json.getColors().length; i++) {
            sources[i] = new MapSqlParameterSource()
                    .addValue("id", user.getId())
                    .addValue("color", colors[i]);
        }
        template.batchUpdate(
                "INSERT INTO prefix_animation (color, player_id) values (:color,:id)", sources
        );
        return new AccountPrefixResponse(Response.OK);
    }
}
