package net.bombdash.core.form;

import com.google.gson.Gson;
import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.form.json.FailedJson;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Используется для url-encode или же form-data в формах
 */
@RestController
@RequestMapping("/form")
public class FormController {
    @Autowired
    private Set<AbstractForm> forms;
    @Autowired
    private Gson gson;

    @RequestMapping(
            value = "/{form}",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public void onFormRequest(
            @PathVariable String form,
            @AuthenticationPrincipal BombDashUser user,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        AbstractForm formObj = forms
                .stream()
                .filter(f -> f.getFormName().equalsIgnoreCase(form))
                .findFirst()
                .orElse(null);
        if (formObj != null) {
            try {
                formObj.preExecute(request, response, user);
            } catch (MethodExecuteException e) {
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);

                response.getWriter().write(gson.toJson(new FailedJson(e.getError())));
            }
        } else
            response.getWriter().write("SUKA_BLYAT");
    }
}
