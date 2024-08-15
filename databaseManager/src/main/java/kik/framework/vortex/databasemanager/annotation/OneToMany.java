package kik.framework.vortex.databasemanager.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 *  This annotation represent is used in the element with 1 multiplicity
 * <h2>Example</h2>
 * Users and tickets
 * a user can have multiple tickets and tickets 1 user so
 * <b> users</b> will have this annotation
 * and tickets {@link ManyToOne}
 * <br>
 * Must be used with {@link ManyToOne} 
 * @see ManyToOne
 * @see ManyToMany
 * @see OneToOne
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface OneToMany {
    /**
     * if delete actions must be with cascade active
     */
    boolean cascade() default false;
}
