package kik.framework.vortex;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeAll;

import kik.framework.vortex.manager.AnnotationManager;

import org.junit.jupiter.api.Test;

public class AnnotationTest {

	private AnnotationManager manager;

	@BeforeAll
	void obtenerToken() throws Exception {
		manager = AnnotationManager.getInstance();
	}

	@Test
	void testGet() {
		String method = manager.checkType("/logged");
		assertEquals("GET", method);
		method = manager.checkType("/status");
		assertEquals("GET", method);
	}

	@Test
	void testPost() {
		String method = manager.checkType("/analyze");
		assertEquals("POST", method);
		method = manager.checkType("/login");
		assertEquals("POST", method);
	}

	@Test
	void testPut() {
		String method = manager.checkType("/execute");

		assertEquals("PUT", method);
		method = manager.checkType("/register");

		assertEquals("PUT", method);
	}

	@Test
	void testDelete() {
		String method = manager.checkType("/cleanup");
		assertEquals("DELETE", method);
		method = manager.checkType("/erease");
		assertEquals("DELETE", method);
	}
}
