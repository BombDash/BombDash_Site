package net.bombdash.core.site;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.methods.player.get.PlayerGet;
import net.bombdash.core.api.methods.player.get.PlayerGetRequest;
import net.bombdash.core.api.methods.player.get.PlayerGetResponse;
import net.bombdash.core.site.auth.BombDashUser;
import net.bombdash.core.site.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@PreAuthorize("isAuthenticated()")
@Controller
public class PrivateAreaController {
    @Autowired
    private Utils utils;
    @Autowired
    private ApplicationContext context;

    @GetMapping("/lk")
    public String onLkRequest(@AuthenticationPrincipal BombDashUser user, ModelMap map) {
        try {
            PlayerGetResponse response = context.getBean(PlayerGet.class).execute(new PlayerGetRequest(user.getId()), null);
            map.put("info", response);
        } catch (MethodExecuteException ex) {
            ex.printStackTrace();
        }
        return "/private/lk";
    }


//TODO Написать
/*    @GetMapping("/friends")
    public String onFriendsRequest(ModelMap map) {
        return "/private/frieds";
    }*/


}
