package net.bombdash.core.api.methods.account.prefix;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AccountPrefixResponse {
    private Response response;

    enum Response {
        /**
         * Префикс сменён
         */
        OK,
        /**
         * UTF-8 в префиксе
         */
        UTF_8,
        WRONG_SPEED,
        /**
         * Запрещённое слово (ADMIN,MODER)
         */
        FORBIDDEN_WORD
    }
}
