package Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface ConfigProperty {
    String configFileName() default
            "config.properties";
    String propertyName() default "";
    Class<?> type() default String.class;
}
