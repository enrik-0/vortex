package kik.framework.vortex;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeAll;
import kik.framework.vortex.annotations.RequestBody;
import kik.framework.vortex.annotations.RequestParam;
import kik.framework.vortex.manager.AnnotationManager;
import kik.framework.vortex.manager.Storage;
import org.junit.jupiter.api.Test;

class AnnotationTest {

	private static Storage storage;
	private static AnnotationManager manager;

	@BeforeAll
	static void Storage() throws Exception {
		manager = AnnotationManager.getInstance();
		storage = Storage.getInstance();
	}

	@Test
	void testGet() {
		String[] method = storage.checkType("/logged");
		assertEquals("GET", method[0]);
		method = storage.checkType("/status");
		assertEquals("GET", method[0]);
	}

	@Test
	void testPost() {
		String[] method = storage.checkType("/analyze");
		assertEquals("POST",method[0]);
		method = storage.checkType("/login");
		assertEquals("POST",  method[0]);
	}

	@Test
	void testPut() {
		String[] method = storage.checkType("/execute");

		assertEquals("PUT", method[0]);
		method = storage.checkType("/register");

		assertEquals("PUT", method[0]);
	}

	@Test
	void testDelete() {
		String method[] = storage.checkType("/cleanup");
		assertEquals("DELETE", method[0]);
		method = storage.checkType("/erease");
		assertEquals("DELETE", method [0]);
	}
	
	@Test
	void testBody() {
		
		Method method = storage.getMethod("PUT", "/execute");
		Annotation[][] annotations  = method.getParameterAnnotations();
		assertEquals(RequestBody.class.getName(), annotations[0][0].annotationType().getName());
	}
	@Test
	void testParam() {
		Method method = storage.getMethod("GET", "/logged");
		Annotation[][] annotations  = method.getParameterAnnotations();
		assertEquals(RequestParam.class.getName(), annotations[0][0].annotationType().getName());
	}
	
	@Test
	void testParams() {
		
		Method method = storage.getMethod("GET", "/status");
		Annotation[][] annotations  = method.getParameterAnnotations();
		assertEquals(RequestParam.class.getName(), annotations[0][0].annotationType().getName());
		assertEquals(RequestParam.class.getName(), annotations[1][0].annotationType().getName());

	}
	
	@Test
	void testAll() {
		Method method = storage.getMethod("DELETE", "/erease");
		Annotation[][] annotations  = method.getParameterAnnotations();
		assertEquals(RequestParam.class.getName(), annotations[0][0].annotationType().getName());
		assertEquals(RequestBody.class.getName(), annotations[1][0].annotationType().getName());
		assertEquals(RequestParam.class.getName(), annotations[2][0].annotationType().getName());
	}
}
