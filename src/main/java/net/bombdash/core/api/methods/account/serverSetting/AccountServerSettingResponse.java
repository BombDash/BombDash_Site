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
        * Быстрая/Медленная скорость
        */
       WRONG_SPEED,
       /**
        * Большая длинна
        */
       LENGTH,
       /**
        * Запрещённое слово (ADMIN,MODER)
        */
       FORBIDDEN_WORD
   }
}
