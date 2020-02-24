package net.bombdash.core.site.auth.handlers;

import com.google.gson.Gson;
import net.bombdash.core.site.auth.json.RedirectJson;
import net.bombdash.core.site.lang.Locale;
import net.bombdash.core.site.utils.Utils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Success implements AuthenticationSuccessHandler {
    private final Utils utils;
    private final Gson gson;


    public Success(Gson gson, Utils utils) {
        this.gson = gson;
        this.utils = utils;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Locale locale = utils.getLocale(request, response);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(gson.toJson(new RedirectJson("/lk")));

    }


}
