package net.bombdash.core;

import com.google.gson.Gson;
import net.bombdash.core.site.auth.UserService;
import net.bombdash.core.site.auth.handlers.Fail;
import net.bombdash.core.site.auth.handlers.Success;
import net.bombdash.core.site.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.Arrays;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private Environment environment;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Autowired
    private Utils utils;
    @Autowired
    private Gson gson;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if (Arrays.stream(environment.getActiveProfiles()).noneMatch("dev"::equals)) {
            http
                    .portMapper()
                    .http(80) // http port defined in yml config file
                    .mapsTo(443);// https port defined in yml config file

            http
                    .requiresChannel()
                    .anyRequest()
                    .requiresSecure();
        }
        http
                .authorizeRequests()
                .anyRequest()
                .permitAll();
        http
                .formLogin()
                .loginPage("/login")
                .successHandler(new Success(gson, utils))
                .failureHandler(new Fail(gson, utils))
                .usernameParameter("email")
                .permitAll();
        http
                .rememberMe()
                .tokenRepository(tokenRepository())
                .tokenValiditySeconds(60 * 60 * 24 * 7)
                .key("remember");
        http
                .logout()
                .permitAll();
        http
                .httpBasic();
        http
                .csrf()
                .disable();
    }

    private String[] getUrls(String... urls) {
        String[] urlsResult = new String[urls.length * 2];
        int i = 0;
        for (String current : urls) {
            urlsResult[i++] = "/" + current;
            urlsResult[i++] = "/" + current + "/*";
        }
        return urlsResult;
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        return jdbcTokenRepositoryImpl;
    }
}