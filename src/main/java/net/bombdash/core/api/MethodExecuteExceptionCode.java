package net.bombdash.core.api;

public enum MethodExecuteExceptionCode {
    ACCESS_DENIED(0),
    PLAYER_NOT_FOUND(1),
    WRONG_PAGE(2),
    FIELD_MISSING(3),
    WRONG_SORT(4),
    JSON_ERROR(5),
    AUTHORIZE_REQUIRED(6),
    UNKNOWN_ERROR(228);

    private int number;

    MethodExecuteExceptionCode(int number) {
        this.number = number;
    }

    public static MethodExecuteExceptionCode fromNum(int code) {
        for (MethodExecuteExceptionCode value : values()) {
            if (value.getNumber() == code) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }

    public int getNumber() {
        return number;
    }
}
