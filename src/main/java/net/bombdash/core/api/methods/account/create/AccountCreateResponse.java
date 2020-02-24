package net.bombdash.core.api.methods.account.create;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountCreateResponse {
    private AccountCreateResponseType response;

    public enum AccountCreateResponseType {
        ok(true),
        not_verify(false),
        email_used(false),
        wrong_mail(false),
        error(false);

        private final boolean success;

        AccountCreateResponseType(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }
    }
}
