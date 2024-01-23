package indi.mofan.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mofan
 * @date 2024/1/23 21:31
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface Header {
    String name();

    String value();
}
