package net.bombdash.core.api.methods.stats.get;

import lombok.Value;

@Value
public class StatsGetRequest {
    private String type;
    private Integer page;
    private boolean global;
}
