package net.bombdash.core.api.methods.account.verify;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AccountVerifyResponse {
    private AccountVerifyResponseCode response;

    /**
     * Ответы от сервера
     * ok - Регистрация успешна
     * no_code - Ты читать не умеешь ? Название сервера видел тут не играют
     * registered - Чел ну камон, ты уже зареган, чо ты прёшся а
     * many_codes - чел ты ахуел там а, какога хуя у тебя 2 кода регистрации сразу а
     */
    public enum AccountVerifyResponseCode {
        ok, registered, no_code, server_error, many_codes
    }
}
