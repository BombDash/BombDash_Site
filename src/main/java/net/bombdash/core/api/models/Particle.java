package net.bombdash.core.api.models;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Value
public class Particle {
    private static final Set<String> allowedParticleType = Stream.of(
            "spark",
            "splinter",
            "sweat",
            "rock",
            "slime",
            "metal",
            "ice"
    ).collect(Collectors.toSet());
    private static final Set<String> allowedEmitType = Stream.of(
            "off",
            "body",
            "legs",
            "around",
            "underfoot"
    ).collect(Collectors.toSet());

    @SerializedName("particle_type")
    private String particleType;
    @SerializedName("emit_type")
    private String emitType;

    public String getParticleType() {
        if (allowedParticleType.contains(particleType))
            return particleType;
        else
            return "spark";
    }

    public String getEmitType() {
        return emitType;
    }


}
