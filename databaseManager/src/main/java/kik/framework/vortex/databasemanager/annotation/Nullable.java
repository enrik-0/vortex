package kik.framework.vortex.databasemanager.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation is used to identify a registry that can be null <br>As by default all <b> all registry are not null </b>
 * @see Unique
 * @see Column
 * @see ID
 * @see Table
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface Nullable {

}
