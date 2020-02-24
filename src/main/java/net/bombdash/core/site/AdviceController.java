package net.bombdash.core.site;

import net.bombdash.core.site.auth.BombDashUser;
import net.bombdash.core.site.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(
        basePackageClasses = {PrivateAreaController.class, PublicAreaController.class}
)
public class AdviceController {

    @Autowired
    private Utils utils;

    @ModelAttribute
    public void fillModel(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response,
            @AuthenticationPrincipal BombDashUser user
    ) {
        utils.fillModel(model, request, response, user);
    }
}
