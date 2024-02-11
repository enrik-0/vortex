package vortex.http;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import vortex.http.elements.Response;
import vortex.test.RequestBuilder;
import vortex.test.exception.AmbiguousMethodException;

@ExtendWith(MockInitialize.class)
class ResponseStatusTest {

	private static final String HOST = "http://localhost:8080";

	@Test
	void testAddHeader() throws IOException, AmbiguousMethodException {
		Response response = new RequestBuilder()
				.get(HOST + "/responseStatus/addHeader").perform();
		
		assertNotNull(response);
		assertNotNull(response.getHeaders().get("Headertest"));
		assertEquals("test1", response.getHeaders().get("Headertest").get(0));

	}
}
