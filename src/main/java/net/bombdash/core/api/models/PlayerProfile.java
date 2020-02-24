package net.bombdash.core.api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class PlayerProfile {
    public PlayerProfile() {
        this.name = "Null";
        this.icon = "Bomb";
        this.color = Color.RED.getRGB();
        this.highlight = Color.YELLOW.getRGB();
        this.global = false;
        this.character = "neoSpaz";
    }

    private String name;
    private String icon;
    private int color;
    private int highlight;
    private boolean global;
    private String character;
    private static List<String> deviceIcons = Arrays.asList("vr", "localAccount", "googlePlusLogo", "alibabaLogo", "gameCircleLogo", "gameCenterLogo");

    public boolean needIcon() {
        return (isDevice() || isGlobal()) && icon != null;
    }

    public boolean isDevice() {
        return deviceIcons.contains(icon);
    }
}
