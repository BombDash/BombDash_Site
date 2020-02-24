package net.bombdash.core.other;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.bombdash.core.site.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class OtherController {
    @Autowired
    private Utils utils;
    @Autowired
    private ApplicationContext context;

    @GetMapping(
            value = "/profile_photo/{character}/{size}/{color}/{highlight}",
            headers = "Accept=image/jpeg, image/jpg, image/png, image/gif"
    )
    @ResponseBody
    public byte[] onProfilePhoto(
            HttpServletResponse response,
            @PathVariable String character,
            @PathVariable String color,
            @PathVariable String highlight,
            @PathVariable String size) {
        utils.enableCache(response);
        return context.getBean(ProfilePhotoGenerator.class).generateImageByte(size, character, color, highlight);
    }

    @GetMapping(
            value = "/profile_window/{nick}",
            headers = "Accept=image/jpeg, image/jpg, image/png, image/gif"
    )
    @ResponseBody
    public byte[] onProfilePhoto(@PathVariable String nick, HttpServletResponse response) throws Exception {
        utils.enableCache(response);
        return context.getBean(ProfileWindowGenerator.class).generateProfileWindow(nick);
    }

    @Autowired
    private Configuration freemarker;

    @GetMapping("/mail/{code}")
    public void onEmailRequest(HttpServletResponse response, @PathVariable String code) throws IOException, TemplateException {
        response.setContentType(MediaType.TEXT_HTML_VALUE);
        Template t = freemarker.getTemplate("/other/mail.ftl");
        ModelMap map = new ModelMap();
        map.addAttribute("code", code);
        t.process(map, response.getWriter());
    }
}
