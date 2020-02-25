package net.bombdash.core.api.models;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class Particle {
    @SerializedName("particle_type")
    private String particleType;
    @SerializedName("emit_type")
    private String emitType;
}
