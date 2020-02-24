package net.bombdash.core.database;

import lombok.SneakyThrows;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.lang.reflect.Method;

public class DirtyShit {
    @SneakyThrows
    public static PreparedStatementCreator getCreator(NamedParameterJdbcTemplate template, String sql, SqlParameterSource source) {
        Method m = template.getClass().getDeclaredMethod("getPreparedStatementCreator", String.class, SqlParameterSource.class);
        m.setAccessible(true);
        return (PreparedStatementCreator) m.invoke(template, sql, source);
    }
}
