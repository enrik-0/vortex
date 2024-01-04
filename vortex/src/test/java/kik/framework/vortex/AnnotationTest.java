package kik.framework.vortex;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.Test;

public class AnnotationTest {

	private AnnotationManager manager;

	@BeforeAll
	void obtenerToken() throws Exception {
		manager = AnnotationManager.getManager();
	}

	@Test
	public void GetTest() {
		String method = manager.checkType("/logged");
		assertEquals(method, "GET");
		method = manager.checkType("/status");
		assertEquals(method, "GET");
	}

	@Test
	public void PostTest() {
		String method = manager.checkType("/analyze");
		assertEquals(method, "POST");
		method = manager.checkType("/login");
		assertEquals(method, "POST");
	}

	@Test
	public void PutTest() {
		String method = manager.checkType("/execute");
		assertEquals(method, "PUT");
		method = manager.checkType("/register");
		assertEquals(method, "PUT");
	}

	@Test
	public void DeleteTest() {
		String method = manager.checkType("/cleanup");
		assertEquals(method, "DELETE");
		method = manager.checkType("/erease");
		assertEquals(method, "DELETE");
	}
}
