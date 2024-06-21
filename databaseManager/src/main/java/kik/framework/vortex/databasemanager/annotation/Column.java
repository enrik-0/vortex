package kik.framework.vortex.databasemanager.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
@Retention(RUNTIME)
@Target(FIELD)
public @interface Column {
    
     String name();
     
     int length() default 255;
     int precision() default -1;
     int scale() default -1;
     boolean autoIncrement() default false;
}

