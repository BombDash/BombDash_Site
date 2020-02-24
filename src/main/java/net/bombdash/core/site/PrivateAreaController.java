package net.bombdash.core.site;

import net.bombdash.core.site.auth.BombDashUser;
import net.bombdash.core.site.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/lk")
    public String onLkRequest(@AuthenticationPrincipal BombDashUser user, ModelMap map) {
        
        return "/private/lk";
    }




//TODO Написать
/*    @GetMapping("/friends")
    public String onFriendsRequest(ModelMap map) {
        return "/private/frieds";
    }*/


}
