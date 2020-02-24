package net.bombdash.core.api.methods.stats.ping;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.Value;

import java.util.Arrays;
import java.util.List;

@Value
public class StatsPingRequest {
    public StatsPingRequest(String serverName, String... ids) {
        this(serverName, Arrays.asList(ids));
    }

    public StatsPingRequest(String serverName, List<String> ids) {
        this.ids = ids;
        this.serverName = serverName;
    }

    private List<String> ids;
    @SerializedName("server_name")
    private String serverName;
}
