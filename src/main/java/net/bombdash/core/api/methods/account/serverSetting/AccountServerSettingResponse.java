package net.bombdash.core.api.methods.account.serverSetting;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AccountServerSettingResponse {
    private Response response;

   public enum Response {
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
