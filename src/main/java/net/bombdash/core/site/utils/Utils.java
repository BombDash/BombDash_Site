package net.bombdash.core.site.utils;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.TemplateHashModel;
import net.bombdash.core.site.auth.BombDashUser;
import net.bombdash.core.site.lang.Locale;
import net.bombdash.core.site.lang.LocaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
public class Utils {
    @Autowired
    LocaleService localeService;

    public Locale getLocale(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String userLang = null;
        if (cookies != null)
            userLang = Arrays
                    .stream(cookies)
                    .filter(c -> c.getName().equals("locale"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        if (userLang == null) {
            userLang = request.getLocale().getLanguage();
            Cookie langCookie = new Cookie("locale", userLang);
            langCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(langCookie);
        }
        return localeService.getLang(userLang);
    }

    private TemplateHashModel statics = new BeansWrapper(Configuration.VERSION_2_3_29).getStaticModels();

    public void fillModel(Model model, HttpServletRequest request, HttpServletResponse response, BombDashUser user) {

        Locale locale = getLocale(request, response);

        model.addAttribute("locale", locale);
        model.addAttribute("request", request);
        model.addAttribute("statics", statics);
        model.addAttribute("bombDashUser", user);
        model.addAttribute("title", "BombDash");
    }

    public void enableCache(HttpServletResponse response) {
        response.setHeader(
                HttpHeaders.CACHE_CONTROL,
                CacheControl
                        .maxAge(365, TimeUnit.DAYS)
                        .cachePublic()
                        .getHeaderValue()
        );
    }


}
