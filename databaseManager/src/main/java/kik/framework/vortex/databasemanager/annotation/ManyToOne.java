package kik.framework.vortex.databasemanager.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
/**
 *  This annotation represent is used in the element with * multiplicity
 * <h2>Example</h2>
 * Users and tickets
 * a user can have multiple tickets and tickets 1 user so
 * <b> tickets</b> will have this annotation
 * and users {@link OneToMany}
 * <br>
 * Must be used with {@link OneToMany} 
 * @see OneToMany
 * @see ManyToMany
 * @see OneToOne
 */
@Retention(RUNTIME)
@Target(FIELD)

public @interface ManyToOne {
}
