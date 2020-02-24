package net.bombdash.core.site;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.site.auth.BombDashUser;
import net.bombdash.core.site.utils.Utils;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class MyErrorController implements ErrorController {
    public MyErrorController(Utils utils) {
        this.utils = utils;
    }

    private final Utils utils;

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException() {
        return "/other/404";
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public MethodExecuteException onPost() {
        return new MethodExecuteException(0, "Ты как из палаты выбрался");
    }

/*    @ModelAttribute
    public void fillModel(Model model, HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal BombDashUser user) {
        utils.fillModel(model, request, response, user);
    }*/

    @Override
    public String getErrorPath() {
        return "/error";
    }
}