package net.bombdash.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.json.IntegerReplacerDeserializer;
import net.bombdash.core.api.json.MethodExecuteExceptionSerializer;
import net.bombdash.core.api.methods.stats.update.PlayerProfileDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.GsonFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class Settings {

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.mariadb.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mariadb://bombdash.net:3306/bombdash");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("wL8dDSKTwpte4Kgr");
        return dataSourceBuilder.build();
    }

    @Autowired
    private Environment environment;

    @Value("${bombdash.http}")
    private int httpPort;

    @Bean
    public ServerProperties serverProperties() {
        ServerProperties properties = new ServerProperties();
        if (Arrays.asList(environment.getActiveProfiles()).contains("dev"))
            properties.setPort(80);
        else {
            properties.setPort(httpPort);
        }
        return properties;
    }

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(true);
        resolver.setPrefix("");
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html;charset=UTF-8");
        return resolver;
    }

    @Bean
    public GsonFactoryBean getGson() {
        GsonFactoryBean factoryBean = new GsonFactoryBean() {
            private Gson gson;

            @Override
            public void afterPropertiesSet() {
                PlayerProfileDeserializer deserializer = new PlayerProfileDeserializer();
                Class profileClass = deserializer.getPlayerProfileClass();
                GsonBuilder gsonBuilder = new GsonBuilder();
                gson = gsonBuilder
                        .registerTypeAdapter(MethodExecuteException.class, new MethodExecuteExceptionSerializer())
                        .setPrettyPrinting()
                        .registerTypeAdapter(profileClass, deserializer)
                        .registerTypeAdapter(Integer.class, new IntegerReplacerDeserializer())
                        .create();
            }

            @Override
            public Gson getObject() {
                return gson;
            }
        };
        factoryBean.setSerializeNulls(true);
        factoryBean.setPrettyPrinting(true);
        return factoryBean;
    }

    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> filterRegistrationBean() {
        FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<>();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding("UTF-8");
        registrationBean.setFilter(characterEncodingFilter);
        return registrationBean;
    }
}
