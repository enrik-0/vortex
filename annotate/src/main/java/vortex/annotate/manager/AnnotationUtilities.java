package vortex.annotate.manager;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.reflections.Reflections;

public class AnnotationUtilities {

    public static void seek(Annotation annotation, Consumer<Object> function) {
	Set<Class<?>> classes = getAnnotatedClasses(annotation);

    }

    public static Object seek(Annotation type, Function<Object, Object> function) {

	return null;
    }
    public static Object isAnnotated(Class<? extends Annotation> expected, Annotation given, Function<Object, Object> function) {

	return null;
	
    }
    public static void isAnnotated(Class<? extends Annotation>expected, Annotation given, Consumer<Object> function) {
	if(given.annotationType().isAssignableFrom(expected)) {
	    
	    function.accept(null);
	}
	
    }

    private  static  Set<Class<?>> getAnnotatedClasses(Annotation annotation) {
	var reflections = new Reflections("");
	return reflections.getTypesAnnotatedWith(annotation);
    }

}
