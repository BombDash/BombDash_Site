package net.bombdash.core.api.methods.account.create;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AccountCreateRequest {
    private String email;
    private String password;
}
