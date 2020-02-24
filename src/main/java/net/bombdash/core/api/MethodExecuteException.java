package net.bombdash.core.api;

import lombok.Data;

/**
 * Коды ошибок
 * 0 - Где права
 * 1 - Игрок не найден
 * 2 - Страница меньше 1
 * 3 - Отсутсвует одно из полей
 * 4 - Выбранный критерий сортировки не существует
 * 5 - Невозможно спарсить json
 * 228 - неизвестная ошибка
 */

@Data
public class MethodExecuteException extends Exception {
    public MethodExecuteException(int code, String error, String... other) {
        this.code = code;
        this.error = error;
        this.other = other;
    }

    private int code;
    private String error;
    private String[] other;
}
