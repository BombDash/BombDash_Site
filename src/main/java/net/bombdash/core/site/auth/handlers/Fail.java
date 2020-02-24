package net.bombdash.core.site.auth.handlers;

import com.google.gson.Gson;
import net.bombdash.core.form.json.FailedJson;
import net.bombdash.core.site.lang.Locale;
import net.bombdash.core.site.utils.Utils;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Fail extends SimpleUrlAuthenticationFailureHandler {

    private final Utils utils;
    private Gson gson;

    public Fail(Gson gson, Utils utils) {
        this.gson = gson;
        this.utils = utils;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Locale locale = utils.getLocale(request, response);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(gson.toJson(new FailedJson(locale.__("auth_fail").toString())));
    }
}
