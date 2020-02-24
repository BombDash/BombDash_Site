package net.bombdash.core.api.methods.account.prefix;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountPrefixRequest {
    /**
     * Текст префикса
     */
    public String text;
    /**
     * Скорость префикса
     */
    public int speed;
    /**
     * Цвета префикса(логично)
     */
    private int[] colors;
}
