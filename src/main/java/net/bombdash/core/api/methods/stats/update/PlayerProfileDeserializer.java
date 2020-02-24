package net.bombdash.core.api.methods.stats.update;

import com.google.gson.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PlayerProfileDeserializer implements JsonDeserializer<StatsUpdateRequest.RawPlayerProfile> {
    private final static Map<Character, String> icons = new HashMap<>();
    private final static Map<String, String> characters = new HashMap<>();

    static {
        try (
                InputStream stream = new ClassPathResource("other/player_profile.json").getInputStream();
                Reader reader = new InputStreamReader(stream)
        ) {
            JsonObject object = new Gson().fromJson(reader, JsonObject.class);
            JsonObject characterJson = object.getAsJsonObject("character");
            for (String key : characterJson.keySet()) {
                String name = characterJson.get(key).getAsString();
                characters.put(key, name);
            }
            JsonObject iconJson = object.getAsJsonObject("icon");
            for (String key : iconJson.keySet()) {
                String iconName = iconJson.get(key).getAsString();
                icons.put(key.charAt(0), iconName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public StatsUpdateRequest.RawPlayerProfile deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        StatsUpdateRequest.RawPlayerProfile profile = new StatsUpdateRequest.RawPlayerProfile();
        JsonObject obj = jsonElement.getAsJsonObject();
        profile.color = getColorArray(obj.getAsJsonArray("color"));
        profile.highlight = getColorArray(obj.getAsJsonArray("highlight"));
        if (obj.has("global")) {
            profile.global = obj.get("global").getAsBoolean();
        } else
            profile.global = false;
        if (obj.has("icon")) {
            String icon = icons.get(obj.get("icon").getAsCharacter());
            if (icon != null)
                profile.icon = icon;
            else
                profile.icon = "bomb";
        } else {
            profile.icon = "bomb";
        }
        profile.character = characters.get(obj.get("character").getAsString());
        if(profile.character==null)
            profile.character = "neoSpaz";
        return profile;
    }
    public static String getIconName(Character character){
        String name = icons.get(character);
        if(name==null)
            name  = "bomb";
        return name;
    }

    private double[] getColorArray(JsonArray array) {
        double[] color = new double[3];
        for (int i = 0; i < array.size(); i++) {
            JsonElement element = array.get(i);
            String colorElement = element.getAsString();
            double colorEl;
            try {
                colorEl = Double.parseDouble(colorElement);
            } catch (Exception ex) {
                colorEl = 0;
            }
            color[i] = colorEl;
        }
        return color;
    }

    public Class<StatsUpdateRequest.RawPlayerProfile> getPlayerProfileClass() {
        return StatsUpdateRequest.RawPlayerProfile.class;
    }
}

