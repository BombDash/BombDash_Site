package net.bombdash.core.site.lang;

import lombok.SneakyThrows;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class LocaleService {

    private Set<Locale> locales = new HashSet<>();
    private Locale ruLocale;

    @SneakyThrows
    @PostConstruct
    public void onInit() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(getClass().getClassLoader());
        Resource[] files = resolver.getResources("classpath:/locale/*");
        Yaml yaml = new Yaml();
        for (Resource resource : files) {
            String filename = resource.getFilename();
            if (filename == null)
                continue;
            InputStream stream;
            stream = resource.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            Map<String, Object> map = yaml.load(reader);
            String locale = filename.substring(0, filename.lastIndexOf('.'));
            Locale lang = new Locale(locale, map);
            locales.add(lang);
            if (locale.equals("ru")) {
                ruLocale = lang;
            }
        }
    }

    public Locale getLang(String locale) {
        return locales
                .stream()
                .filter(l -> l.getLocale().equals(locale))
                .findFirst()
                .orElse(ruLocale);
    }
}
