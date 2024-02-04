package vortex.http;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import vortex.http.elements.Response;
import vortex.test.Mock;
import vortex.test.RequestBuilder;
import vortex.test.exception.AmbiguousMethodException;
class ResponseStatusTest {

	private static final String HOST = "http://localhost:8080";
	@BeforeAll
	static void setUp() throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, IOException {
		Mock.getInstance();
	}
	@AfterAll
	static void finish() {
		Mock.stop();
	}

	@Test
	void testAddHeader() throws IOException, AmbiguousMethodException {
		Response response = new RequestBuilder()
				.get(HOST + "/responseStatus/addHeader").perform();

		assertNotNull(response.getHeaders().get("headerTest"));
		assertEquals("test1", response.getHeaders().get("headerTest"));

	}
}
