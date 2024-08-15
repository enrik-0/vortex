package kik.framework.vortex.databasemanager.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
/**
 * Identifies a registry that must be in the <b> Primary key </b> of the table
 * to modify for parameters of the registry use {@link Column}
 * @see Nullable
 * @see  Unique
 */
public @interface ID {

}
