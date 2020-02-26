package net.bombdash.core.api.methods.account.serverSetting;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.MethodExecuteExceptionCode;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.api.models.Particle;
import net.bombdash.core.api.models.Prefix;
import net.bombdash.core.database.Extractors;
import net.bombdash.core.site.auth.BombDashUser;
import net.bombdash.core.site.auth.Status;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.bombdash.core.api.methods.account.serverSetting.AccountServerSettingResponse.Response;

@Component
public class AccountServerSetting extends AbstractExecutor<AccountServerSettingRequest, AccountServerSettingResponse> {
    private CharsetEncoder encoder = StandardCharsets.US_ASCII.newEncoder();
    private Set<Status> disableCheck = Stream.of(Status.admin, Status.moderator).collect(Collectors.toSet());

    @Override
    public AccountServerSettingResponse execute(AccountServerSettingRequest json, BombDashUser user) throws MethodExecuteException {
        NamedParameterJdbcTemplate template = getQueries().getTemplate();
        if (user == null)
            throw new MethodExecuteException(MethodExecuteExceptionCode.AUTHORIZE_REQUIRED, "Can't execute without user");
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", user.getId());
        Integer count = template.query("SELECT COUNT(*) FROM privilege WHERE player_id = :id", source, Extractors.firstIntExtractor);
        if (count == null)
            throw new MethodExecuteException(MethodExecuteExceptionCode.UNKNOWN_ERROR, "Count is null, but why?/");
        if (count == 0) {
            throw new MethodExecuteException(MethodExecuteExceptionCode.ACCESS_DENIED, "You are not vip or higher");
        }
        Prefix prefix = json.getPrefix();
        template.update("DELETE from prefix where player_id = :id", source);
        if (prefix != null) {
            int speed = prefix.getSpeed();
            String text = prefix.getText();
            if (!disableCheck.contains(user.getStatus())) {
                if (!encoder.canEncode(prefix.getText())) {
                    return new AccountServerSettingResponse(Response.UTF_8);
                }
                if (Stream.of("admin", "moderator", "owner").anyMatch(s -> s.equalsIgnoreCase(text))) {
                    return new AccountServerSettingResponse(Response.FORBIDDEN_WORD);
                }
                if (speed <= 0 || speed >= 1000) {
                    return new AccountServerSettingResponse(Response.WRONG_SPEED);
                }
            }
            source
                    .addValue("text", text)
                    .addValue("speed", speed);
            template.update("INSERT INTO prefix (player_id, text, speed) VALUES (:id,:text,:speed)", source);

            List<Integer> colors = prefix.getAnimation();
            MapSqlParameterSource[] sources = new MapSqlParameterSource[colors.size()];
            for (int i = 0; i < colors.size(); i++) {
                sources[i] = new MapSqlParameterSource()
                        .addValue("id", user.getId())
                        .addValue("color", colors.get(i));
            }
            template.batchUpdate(
                    "INSERT INTO prefix_animation (color, player_id) values (:color,:id)", sources
            );
        }
        Particle particle = json.getParticle();
        template.update("DELETE FROM particle where player_id = :id", source);
        if (particle != null) {
            source
                    .addValue("emit", particle.getEmitType())
                    .addValue("particle", particle.getParticleType());
            template.update("INSERT INTO particle (player_id, particle_type, emit_type) VALUES (:id,:particle,:emit)", source);
        }

        return new AccountServerSettingResponse(Response.OK);
    }
}
