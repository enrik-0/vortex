package kik.framework.vortex.databasemanager.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * Represents an relation many to many this means we will create a table containg the relation
 * both entities must have a {@link java.util.List} parameter of the other both of them with this
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ManyToMany {

    /**
     * if delete actions must be with cascade active
     */
    boolean cascade() default false;
}
