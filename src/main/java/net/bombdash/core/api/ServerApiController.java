package net.bombdash.core.api;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;
import java.util.logging.Logger;

@Controller
@RequestMapping("/api")
public class ServerApiController {
    private final Set<AbstractExecutor<?, ?>> methods;

    public ServerApiController(Set<AbstractExecutor<?, ?>> methods) {
        this.methods = methods;
    }

    @ResponseBody
    @ExceptionHandler(value = {
            MalformedJsonException.class,
            JsonParseException.class,
            JsonSyntaxException.class,
            JsonMappingException.class
    }
    )
    public Object onParseException(Exception ex) {
        return new MethodExecuteException(5, "Can't parse json " + ex.getLocalizedMessage());
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @PostMapping("/self")
    public Object self(@AuthenticationPrincipal BombDashUser user) {
        return user;
    }

    @Autowired
    private Gson gson;

    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @PostMapping(
            value = "/{method}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Object onPost(@PathVariable String method, @AuthenticationPrincipal BombDashUser user, @RequestBody JsonObject json) {
        long a = System.currentTimeMillis();
        Logger logger = Logger.getGlobal();
        logger.info("====================" + method + "====================");
        logger.info("Request: " + json.toString());
        logger.info("User: " + user);
        AbstractExecutor<?, ?> methodObj = methods
                .stream()
                .filter(m -> m.getMethodName().equalsIgnoreCase(method))
                .findFirst()
                .orElse(null);
        if (methodObj != null) {
            try {

                Object response = methodObj.checkToken(json, user);
                logger.info("Response: " + gson.toJson(response));
                logger.info("Time:" + (System.currentTimeMillis() - a));
                return response;
            } catch (MethodExecuteException e) {
                return e;
            } catch (Exception ex) {
                ex.printStackTrace();
                return ex;
            }
        } else {
            return new MethodExecuteException(0, "Method " + method + " not found");
        }
    }

    @GetMapping(value = {"*", "", "/*"})
    public ModelAndView onEmptyOrGet() {
        return new ModelAndView("/other/holodilnik");
    }
}
 