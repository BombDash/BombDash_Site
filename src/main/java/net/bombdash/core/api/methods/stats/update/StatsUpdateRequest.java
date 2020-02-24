package net.bombdash.core.api.methods.stats.update;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class StatsUpdateRequest {
    @Expose(serialize = false, deserialize = false)


    public String server;
    public List<PlayerTop> players;

    @Data
    static class PlayerTop {
        public String id;
        public String device;
        public Map<String, Integer> scores;
        @SerializedName("last_profile")
        public String lastProfile;
        public Map<String, RawPlayerProfile> profiles;
    }
    @Data
    public static class RawPlayerProfile {
        public String icon;
        public double[] color;
        public double[] highlight;
        public boolean global;
        public String character;
    }
}
