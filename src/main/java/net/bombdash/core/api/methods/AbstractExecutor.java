package net.bombdash.core.api.methods;

import com.google.gson.*;
import net.bombdash.core.api.MethodExecuteException;
import net.bombdash.core.api.annotations.ProtectedMethod;
import net.bombdash.core.api.json.IntegerReplacerDeserializer;
import net.bombdash.core.api.methods.stats.update.PlayerProfileDeserializer;
import net.bombdash.core.database.PreparedQueries;
import net.bombdash.core.site.auth.BombDashUser;
import net.bombdash.core.site.auth.Status;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;

public abstract class AbstractExecutor<Request, Response> {
    @Autowired
    private PreparedQueries queries;


    protected PreparedQueries getQueries() {
        return queries;
    }
    public Response execute(Request json) throws MethodExecuteException {
        return execute(json,null);
    }

    public Object checkToken(JsonObject json, BombDashUser user) throws MethodExecuteException {
        Request parsed;
        try {
            parsed = getJson(json);
        } catch (Exception ex) {
            return ex;
        }
        if (getClass().isAnnotationPresent(ProtectedMethod.class)) {
            if (user.getStatus().equals(Status.admin)) {
                return execute(parsed, user);
            } else {
                throw new MethodExecuteException(0, "У сука где права");
            }
        } else
            return execute(parsed, user);
    }

    public abstract Response execute(Request json, BombDashUser user) throws MethodExecuteException;

    public String getMethodName() {
        String fullName = getClass().getPackage().getName().toLowerCase();

        return (fullName.substring(index));
    }

    @SuppressWarnings("unchecked")
    private Request getJson(JsonElement json) {
        Class<Request> clazz = (Class<Request>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        return gson.fromJson(json, clazz);
    }

    private final static int index = "net.bombdash.core.api.methods.".length();

    @Autowired
    private Gson gson;


}
