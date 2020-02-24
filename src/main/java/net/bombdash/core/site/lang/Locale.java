package net.bombdash.core.site.lang;

import java.util.Map;

public class Locale {
    private final Map<String, Object> map;
    private final String locale;

    public Locale(String name, Map<String, Object> map) {
        this.locale = name;
        this.map = map;
    }

    public String getLocale() {
        return locale;
    }

    public Object __(String path) {
        Object obj = map.get(path);
        if (obj == null)
            return "[" + path + "]";
        else
            return obj;
    }

}
