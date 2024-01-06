package kik.framework.vortex.manager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kik.framework.vortex.annotations.Controller;
import kik.framework.vortex.annotations.Entity;
import kik.framework.vortex.annotations.Service;

/**
 * @Author: Enrique Javier Villar Cea
 * @Date: 04/01/2024
 * @Purpose: handle the annotations
 */
public final class AnnotationManager {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AnnotationManager.class);
	private static AnnotationManager manager;
	private Storage data = new Storage();

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
		ArrayList<Class<?>> annotatedClasses = data
				.getComponent(Controller.class.getName());
		for (Class<?> annotatedClass : annotatedClasses) {
			for (Method method : annotatedClass.getDeclaredMethods()) {
				Annotation[][] w = method.getParameterAnnotations();
				for (Annotation annotation : method.getAnnotations()) {
					filter(annotation, method);
				}

			}
		}
	}

	private void filter(Annotation annotation, Method method) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("uri", annotation.toString().split("\"")[1].replace("\\", ""));
		map.put("call", method);
		switch (annotation.annotationType().getSimpleName()) {
			case "GetMapping" :
				data.addUrl("GET", map);
				break;
			case "PutMapping" :
				data.addUrl("PUT", map);
				break;

			case "DeleteMapping" :
				data.addUrl("DELETE", map);
				break;
			case "PostMapping" :
				data.addUrl("POST", map);
				break;
			default :
		}
	}

	private void setClasses(Class<? extends Annotation> annotation) {
		data.addAnnotationType(annotation.getName());
		Reflections reflections = new Reflections("");
		Set<Class<?>> annotatedClasses = reflections
				.getTypesAnnotatedWith(annotation);
		for (Class<?> annotatedClass : annotatedClasses) {
			data.addClass(annotation.getName(), annotatedClass);
		}

	}

	private void seekClasses() {
		setClasses(Controller.class);
		setClasses(Service.class);
		setClasses(Entity.class);
	}

}
