package net.bombdash.core.form;

import com.google.gson.Gson;
import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.form.json.SuccessJson;
import net.bombdash.core.site.auth.BombDashUser;
import net.bombdash.core.form.json.FailedJson;
import net.bombdash.core.site.lang.Locale;
import net.bombdash.core.site.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractForm {
    @Autowired
    private Utils utils;
    @Autowired
    private Gson gson;

    public abstract void execute(
            HttpServletRequest request,
            HttpServletResponse response,
            BombDashUser user,
            Locale locale) throws MethodExecuteException;

    protected Locale getLocale(HttpServletRequest request, HttpServletResponse response) {
        return utils.getLocale(request, response);
    }

    protected void setJsonResponse(HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public String getFormName() {
        String fullName = getClass().getPackage().getName().toLowerCase();
        String path = (fullName.substring(index));
        return path.replace('.', '_');
    }

    protected void returnError(String error, HttpServletResponse response) {
        setJsonResponse(response);
        try {
            response.getWriter().write(gson.toJson(new FailedJson(error)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void returnSuccess(String success, HttpServletResponse response) {
        setJsonResponse(response);
        try {
            response.getWriter().write(gson.toJson(new SuccessJson(success)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final static int index = "net.bombdash.core.form.forms.".length();

    public void preExecute(HttpServletRequest request, HttpServletResponse response, BombDashUser user) throws MethodExecuteException {
        Locale locale = getLocale(request, response);
        execute(request, response, user, locale);
    }
}
