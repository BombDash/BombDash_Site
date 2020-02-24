package net.bombdash.core.api.methods.account.prefix;

import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.methods.AbstractExecutor;
import net.bombdash.core.site.auth.BombDashUser;

public class AccountPrefix extends AbstractExecutor<AccountPrefixRequest,AccountPrefixResponse> {
    @Override
    public AccountPrefixResponse execute(AccountPrefixRequest json, BombDashUser user) throws MethodExecuteException {
        return null;
    }
}
