package net.bombdash.core.form.forms.account.create;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.methods.account.create.AccountCreate;
import net.bombdash.core.api.methods.account.create.AccountCreateRequest;
import net.bombdash.core.api.methods.account.create.AccountCreateResponse;
import net.bombdash.core.form.AbstractForm;
import net.bombdash.core.site.auth.BombDashUser;
import net.bombdash.core.site.lang.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AccountCreateForm extends AbstractForm {
    @Autowired
    private AccountCreate accountCreate;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, BombDashUser user, Locale locale) throws MethodExecuteException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if (email == null || password == null) {
            returnError(locale.__("form_null_value").toString(), response);
            return;
        }
        AccountCreateResponse responseObj = accountCreate.execute(new AccountCreateRequest(email, password));
        AccountCreateResponse.AccountCreateResponseType objectResponse = responseObj.getResponse();
        if (objectResponse.isSuccess()) {
            returnSuccess(locale.__(getFormName() + "_" + objectResponse.name()).toString(), response);
        } else
            returnError(locale.__(getFormName() + "_" + objectResponse.name()).toString(), response);
    }

}
