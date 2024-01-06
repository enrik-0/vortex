
package kik.framework.vortex.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import java.util.Map;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kik.framework.vortex.annotations.Controller;
import kik.framework.vortex.annotations.Entity;
import kik.framework.vortex.annotations.Service;

/**
 * @Author: Enrique Javier Villar Cea
 * @Date: 04/01/2024
 * @Purpose: Brief description of the purpose of this class
 */
public final class AnnotationManager {

	private final Logger logger = LoggerFactory.getLogger(AnnotationManager.class);
	private static AnnotationManager manager;
	private HashMap<String, String> urls = new HashMap<>();
	private HashMap<String, ArrayList<Class<?>>> classes = new HashMap<>();

	private AnnotationManager() {
		initialize();

	}

	public static AnnotationManager getInstance() {

		synchronized (AnnotationManager.class) {
			if (manager == null) {
				manager = new AnnotationManager();
			}

		}

		return manager;
	}

	private void initialize() {
		seekClasses();
		ArrayList<Class<?>>  annotatedClasses = classes.get(Controller.class.getName());
		for (Class<?> annotatedClass : annotatedClasses) {
			Method[] w = annotatedClass.getDeclaredMethods(); 
			for(Method method : annotatedClass.getDeclaredMethods()) {
				Annotation[] e = method.getAnnotations();
				for ( Annotation a : method.getAnnotations()) {

					logger.error(a.getClass().toString());
				}

				
			}
		}
	}

	private void setClasses(Class<? extends Annotation> annotation) {
		classes.put(annotation.getName(), new ArrayList<Class<?>>());
		Reflections reflections = new Reflections("");
		Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(annotation);
		for (Class<?> annotatedClass : annotatedClasses) {
			classes.get(annotation.getName()).add(annotatedClass);
		}

	}

	private void seekClasses() {
		setClasses(Controller.class);
		setClasses(Service.class);
		setClasses(Entity.class);
		classes.forEach((key, value) -> {
			logger.debug("type " + key);
			value.stream().forEach(c -> logger.debug("class " + c.getName()));
		});
	}

}
