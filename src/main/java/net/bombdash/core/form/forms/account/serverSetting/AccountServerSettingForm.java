package net.bombdash.core.form.forms.account.serverSetting;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.methods.account.serverSetting.AccountServerSetting;
import net.bombdash.core.api.methods.account.serverSetting.AccountServerSettingRequest;
import net.bombdash.core.api.methods.account.serverSetting.AccountServerSettingResponse.Response;
import net.bombdash.core.api.models.Particle;
import net.bombdash.core.api.models.Prefix;
import net.bombdash.core.form.AbstractForm;
import net.bombdash.core.site.auth.BombDashUser;
import net.bombdash.core.site.lang.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AccountServerSettingForm extends AbstractForm {
    @Autowired
    private AccountServerSetting serverSetting;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, BombDashUser user, Locale locale) throws MethodExecuteException {
        Prefix prefix;
        Particle particle;
        Map<String, String[]> map = request.getParameterMap();
        //Парсинг префикса
        {
            List<Integer> colors = new ArrayList<>();

            for (int i = 1; i <= 8; i++) {
                String[] element = map.get("color" + i);
                if (element == null || element.length == 0)
                    break;
                Color color;
                try {
                    color = Color.decode(element[0]);
                } catch (Exception ex) {
                    continue;
                }
                colors.add(color.getRGB());
            }

            String prefixStr = map.getOrDefault("prefix", new String[]{user.getStatus().name()})[0];
            int prefixSpeed;
            try {
                String speedStr = map.get("speed")[0];
                prefixSpeed = Integer.parseInt(speedStr);
            } catch (Exception ex) {
                prefixSpeed = 250;
            }
            prefix = new Prefix(prefixStr, prefixSpeed, colors);
        }
        //Парсинг партиклов
        {
            String particleType = map.getOrDefault("particle_type", new String[]{"spark"})[0];
            String emitType = map.getOrDefault("emit_type", new String[]{"body"})[0];
            particle = new Particle(particleType, emitType);
        }
        AccountServerSettingRequest assr = new AccountServerSettingRequest(prefix, particle);

        Response serverSettingResponse = serverSetting.execute(assr, user).getResponse();
        switch (serverSettingResponse) {
            case OK:
                returnSuccess(locale.__("server_setting_ok").toString(), response);
                break;
            case WRONG_SPEED:
                returnError(locale.__("server_setting_wrong_speed").toString(), response);
                break;
            case FORBIDDEN_WORD:
                returnError(locale.__("server_setting_forbidden_word").toString(), response);
                break;
            case LENGTH:
                returnError(locale.__("server_setting_lenght").toString(), response);
            default:
                returnError("Неизвестный ответ сервера", response);
                break;
        }
    }
}
