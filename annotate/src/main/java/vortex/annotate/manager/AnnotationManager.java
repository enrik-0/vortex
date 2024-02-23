package vortex.annotate.manager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vortex.annotate.annotations.VortexApplication;
import vortex.annotate.components.Controller;
import vortex.annotate.components.Entity;
import vortex.annotate.components.Service;
import vortex.annotate.constants.HttpMethod;
import vortex.annotate.controller.RequestMapping;

/**
 * @Author: Enrique Javier Villar Cea
 * @Date: 04/01/2024
 * @Purpose: handle the annotations
 */
public final class AnnotationManager {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AnnotationManager.class);
	private static AnnotationManager manager;
	private Storage data = Storage.getInstance();

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
		ArrayList<Class<?>> annotatedClasses = (ArrayList<Class<?>>) data
				.getComponent(Controller.class.getName());
		for (Class<?> annotatedClass : annotatedClasses) {
			RequestMapping[] controllers = annotatedClass
					.getAnnotationsByType(RequestMapping.class);
			for (Method method : annotatedClass.getDeclaredMethods()) {
				for (Annotation annotation : method.getAnnotations()) {
					filter(controllers[0], annotation, method);
				}
			}
		}
		//getRunnable();
	}

	private void filter(RequestMapping mapping, Annotation annotation,
			Method method) {
		HashMap<String, Object> map = new HashMap<>();

		map.put("uri", mapping.value()
				+ annotation.toString().split("\"")[1].replace("\\", ""));
		map.put("call", method);
		switch (annotation.annotationType().getSimpleName()) {
			case "GetMapping" :
				data.addUrl(HttpMethod.GET, map);
				LOGGER.info(String.format("Found %s with uri %s and method %s",
						HttpMethod.GET.name(), map.get("uri"),
						((Method) map.get("call")).getName()));
				break;
			case "PutMapping" :
				data.addUrl(HttpMethod.PUT, map);
				LOGGER.info(String.format("Found %s with uri %s and method %s",
						HttpMethod.PUT.name(), map.get("uri"),
						((Method) map.get("call")).getName()));
				break;

			case "DeleteMapping" :
				data.addUrl(HttpMethod.DELETE, map);
				LOGGER.info(String.format("%s with uri %s and method %s",
						HttpMethod.DELETE.name(), map.get("uri"),
						((Method) map.get("call")).getName()));
				break;
			case "PostMapping" :
				data.addUrl(HttpMethod.POST, map);
				LOGGER.info(String.format("Found %s with uri %s and method %s",
						HttpMethod.POST.name(), map.get("uri"),
						((Method) map.get("call")).getName()));
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
			if (LOGGER.isDebugEnabled()) {
				LOGGER.info(String.format("class :%s annotated with %s ",
						annotatedClass.getName(), annotation.getName()));
			}
		}

	}

	private void seekClasses() {
		setClasses(Controller.class);
		LOGGER.debug(String.format("number of %s %d", Controller.class.getSimpleName(), data.getComponent(Controller.class.getName()).size()));
		setClasses(Service.class);
		LOGGER.debug(String.format("number of %s %d", Service.class.getSimpleName(), data.getComponent(Service.class.getName()).size()));
		setClasses(Entity.class);
		LOGGER.debug(String.format("number of %s %d", Entity.class.getSimpleName(), data.getComponent(Entity.class.getName()).size()));
	}

	private void getRunnable() {
		data.setRunnable(getClassesAnnotated("", VortexApplication.class));
	}
	public static Set<Class<?>> getClassesAnnotated(String search,
			Class<? extends Annotation> annotation) {
		Reflections var = new Reflections(search);
		return var.getTypesAnnotatedWith(annotation);

	}

}
