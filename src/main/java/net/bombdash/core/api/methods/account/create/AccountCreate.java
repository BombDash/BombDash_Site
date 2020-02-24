package net.bombdash.core.api.methods.account.create;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.mail.EmailSender;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.database.Extractors;
import net.bombdash.core.other.Utils;
import net.bombdash.core.site.auth.BombDashUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.io.IOException;
import java.io.StringWriter;

import static net.bombdash.core.api.methods.account.create.AccountCreateResponse.AccountCreateResponseType.*;

@Component
public class AccountCreate extends AbstractExecutor<AccountCreateRequest, AccountCreateResponse> {
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private Configuration freemarker;
    @Autowired
    private EmailSender sender;

    @Override
    public AccountCreateResponse execute(AccountCreateRequest json, BombDashUser user) throws MethodExecuteException {
        String mail = json.getEmail();
        if (mail == null)
            throw new MethodExecuteException(3, "Email null");
        if (!Utils.isValidEmailAddress(mail)) {
            return new AccountCreateResponse(wrong_mail);
        }
        NamedParameterJdbcTemplate template = getQueries().getTemplate();
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("email", mail);
        Integer mailCount = template.query("SELECT count(*) from account where email = :email", source, Extractors.firstIntExtractor);
        if (mailCount == null)
            throw new MethodExecuteException(228, "Null");
        if (mailCount > 0) {
            return new AccountCreateResponse(email_used);
        }
        Integer mailRegCount = template.query("SELECT count(*) from registration where email = :email", source, Extractors.firstIntExtractor);
        if (mailRegCount == null)
            throw new MethodExecuteException(228, "Null");
        if (mailRegCount > 0) {
            return new AccountCreateResponse(not_verify);
        }

        String code = Utils.getRandomString(5);
        try {
            Template t = freemarker.getTemplate("/other/mail.ftl");
            ModelMap map = new ModelMap();
            map.addAttribute("code", code);
            StringWriter writer = new StringWriter();
            t.process(map, writer);
            sender.sendEmail(mail, "Активация аккаунта BombDash", writer.toString());
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            return new AccountCreateResponse(error);
        }

        source.addValue("password", encoder.encode(json.getPassword()));
        source.addValue("code", code);
        template.update(
                "INSERT INTO registration (email,password,verification_code)" +
                        " VALUES (:email,:password,:code)", source);


        return new AccountCreateResponse(ok);
    }
}
