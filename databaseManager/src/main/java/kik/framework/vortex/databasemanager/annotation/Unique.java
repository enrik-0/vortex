package kik.framework.vortex.databasemanager.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Used to identify a registry that must be unique
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Unique {
}
