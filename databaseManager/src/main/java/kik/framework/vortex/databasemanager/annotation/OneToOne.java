package kik.framework.vortex.databasemanager.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
/**
 * represents an OneToOne relation in which one of the entities knows another
 * <h2> Example</h2>
 * trucks and users  a user 1 and only 1 truck
 * so its your decision who have the ID of the other 
 * if you want the id of the user in trucks use this annotation on truck etc.
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface OneToOne {
    /**
     * if delete actions must be with cascade active
     */
    boolean cascade() default false;
}
