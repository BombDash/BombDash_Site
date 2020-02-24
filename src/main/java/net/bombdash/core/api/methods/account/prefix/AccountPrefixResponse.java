package net.bombdash.core.api.methods.account.prefix;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AccountPrefixResponse {
    private Response response;

    enum Response {
        OK,UTF_8,FORBIDDEN_WORD
    }
}
