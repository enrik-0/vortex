package kik.framework.vortex.databasemanager.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * annotation to change the name of the table
 * <b> by default is the class name with an s and lowercase </b>  <br>
 * <b>for example class User will be users</b>
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Table {

    /**
     * name of the table
     */
    String value();
}
