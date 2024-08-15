package kik.framework.vortex.databasemanager.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
@Retention(RUNTIME)
@Target(FIELD)
/**
 * This annotation duty is too add parameters to the registry 
 * @see #name()
 * @see #length()
 * @see #autoIncrement()
 */
public @interface Column {
    
    /**
     * name in the database
     */
     String name();
     
     /**
      * max length in the database
      */
     int length() default 255;
     /**
      * TODO future implementation
      */
     int precision() default -1;
     /**
      * TODO future implementation
      */
     int scale() default -1;
     /**
      * set if the value is autoincremental or not
      */
     boolean autoIncrement() default false;
}

