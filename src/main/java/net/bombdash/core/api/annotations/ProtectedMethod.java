package net.bombdash.core.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) /* Аннотация применима только к классам, а не к пакетам,
отдельным методам, переменным и т.д.
*/
@Retention(RetentionPolicy.RUNTIME)
/* Применяется во время выполнения программы.
Если бы нам нужно было применять аннотацию к исходному коду
на этапе компиляции программы, мы бы указали RetentionPolicy.SOURCE.*/
public @interface ProtectedMethod {
}
