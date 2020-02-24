package net.bombdash.core.api.models;

import lombok.Value;

@Value
public class BanInfo {
    public String reason;
    private Long end;
    private String operator;

    public String getTextOperator() {
        return operator != null ? operator : "CONSOLE";
    }
}
