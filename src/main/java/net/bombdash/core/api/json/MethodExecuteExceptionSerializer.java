package net.bombdash.core.api.json;

import com.google.gson.*;
import net.bombdash.core.api.MethodExecuteException;

import java.lang.reflect.Type;

public class MethodExecuteExceptionSerializer implements JsonSerializer<MethodExecuteException> {
    @Override
    public JsonElement serialize(MethodExecuteException e, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.add("error", new JsonPrimitive(e.getError()));
        JsonArray array = new JsonArray();
        for (String s : e.getOther()) {
            array.add(new JsonPrimitive(s));
        }
        object.add("other", array);
        return object;
    }
}
