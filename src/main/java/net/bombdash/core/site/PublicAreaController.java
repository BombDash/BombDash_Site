package net.bombdash.core.site;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.methods.player.ban.get.PlayerBanGet;
import net.bombdash.core.api.methods.player.ban.get.PlayerBanGetRequest;
import net.bombdash.core.api.methods.player.get.PlayerGet;
import net.bombdash.core.api.methods.player.get.PlayerGetRequest;
import net.bombdash.core.api.methods.player.get.PlayerGetResponse;
import net.bombdash.core.api.methods.player.online.PlayerOnline;
import net.bombdash.core.api.methods.player.search.PlayerSearch;
import net.bombdash.core.api.methods.player.search.PlayerSearchRequest;
import net.bombdash.core.api.methods.stats.get.StatsGet;
import net.bombdash.core.api.methods.stats.get.StatsGetRequest;
import net.bombdash.core.api.methods.stats.place.StatsPlace;
import net.bombdash.core.api.methods.stats.place.StatsPlaceRequest;
import net.bombdash.core.api.methods.stats.types.StatsTypes;
import net.bombdash.core.api.models.BanInfo;
import net.bombdash.core.api.models.PlayerList;
import net.bombdash.core.api.models.PlayerProfile;
import net.bombdash.core.site.utils.Utils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class PublicAreaController {
    private final ApplicationContext context;

    public PublicAreaController(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    @GetMapping("/online")
    public String onOnlineRequest(ModelMap requestMap) {
        Map<String, PlayerList<Void>> map = context.getBean(PlayerOnline.class).execute(null, null);
        map = new TreeMap<>(map);
        requestMap.addAttribute("map", map);
        return "/public/online";
    }

    @GetMapping("/placeRedirect/{global}/{type}/{id}")
    public void onPlaceRedirectRequest(@PathVariable String type, @PathVariable String global, @PathVariable String id,HttpServletResponse response) throws MethodExecuteException, IOException {
        boolean bGlobal = Boolean.parseBoolean(global);
        int place = context.getBean(StatsPlace.class).execute(new StatsPlaceRequest(id, bGlobal, type)).getPlace();
        String g = bGlobal ? "top" : "month";
        response.sendRedirect("/" + g + "/" + type + "?page=" + ((int)Math.ceil(place / 100D)) + "&glow=" + id);
    }

    @GetMapping("/search")
    public String onSearchRequest(
            ModelMap map,
            @RequestParam(required = false, defaultValue = "") String q
    ) throws MethodExecuteException {
        map.put("nick", q);
        if (q.trim().length() > 3) {
            PlayerList<List<PlayerProfile>> list =
                    context
                            .getBean(PlayerSearch.class)
                            .execute(new PlayerSearchRequest(q));
            map.put("list", list);
        }
        return "/public/search";
    }

    @GetMapping("/banlist")
    public String onBanListRequest(ModelMap map, @RequestParam(required = false, defaultValue = "1") String page) throws MethodExecuteException {
        int iPage;
        try {
            iPage = Integer.parseInt(page);
        } catch (NumberFormatException ex) {
            iPage = 0;
        }
        PlayerList<BanInfo> list = context.getBean(PlayerBanGet.class).execute(new PlayerBanGetRequest(iPage));
        map.put("banlist", list);
        return "/public/banlist";
    }

    @GetMapping("/profile/{id}")
    public String onProfileRequest(@PathVariable String id, ModelMap map, HttpServletResponse httpResponse) {
        try {
            PlayerGetRequest request = new PlayerGetRequest(id, PlayerGetRequest.PlayerGetField.values());
            PlayerGetResponse response = context.getBean(PlayerGet.class).execute(request);
            map.put("id", id);
            map.put("response", response);
        } catch (MethodExecuteException e) {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return "/public/profile";
    }

    @GetMapping("/top")
    public String onTopRequest() {
        return "redirect:/top/all";
    }

    @GetMapping("/top/{type}")
    public String onTopRequest
            (
                    @PathVariable String type,
                    ModelMap map,
                    @RequestParam(required = false, name = "page", defaultValue = "1") String page)
            throws MethodExecuteException {
        int iPage;
        try {
            iPage = Integer.parseInt(page);
        } catch (NumberFormatException ex) {
            iPage = 1;
        }
        PlayerList<StatsGet.StatsGetData> response = context.getBean(StatsGet.class).execute(new StatsGetRequest(type, iPage, true));
        fillTop(true, map, type, response);
        return "/public/top";
    }

    private void fillTop(boolean global, ModelMap map, String type, PlayerList<StatsGet.StatsGetData> response) throws MethodExecuteException {
        List<String> types = context.getBean(StatsTypes.class).execute(null);
        map.put("top", response);
        map.put("type", type);
        map.put("global", global);
        map.put("types", types);
    }

    @GetMapping("/month")
    public String onMonthRequest() {
        return "redirect:/month/all";
    }

    @GetMapping("/login")
    public String onLogin() {
        return "/public/login";
    }

    @GetMapping("/month/{type}")
    public String onMonthRequest(
            @PathVariable String type,
            ModelMap map,
            @RequestParam(required = false, name = "page", defaultValue = "1") String page) throws MethodExecuteException {
        int iPage;
        try {
            iPage = Integer.parseInt(page);
        } catch (NumberFormatException ex) {
            iPage = 1;
        }
        PlayerList<StatsGet.StatsGetData> response = context.getBean(StatsGet.class).execute(new StatsGetRequest(type, iPage, false));
        fillTop(false, map, type, response);
        return "/public/top";
    }

    @GetMapping
    public String indexPage() {
        return "/public/index";
    }

/*    @ModelAttribute
    public void fillModel(Model model, HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal BombDashUser user) {
        utils.fillModel(model, request, response,user);
    }*/

    @GetMapping(
            value = "/*",
            produces = MediaType.TEXT_HTML_VALUE
    )
    public String on404() {
        return "/other/404";
    }
}
