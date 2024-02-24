package vortex.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import vortex.http.exchange.Response;
import vortex.http.status.HttpStatus;
import vortex.test.RequestBuilder;
import vortex.test.exception.AmbiguousMethodException;

@ExtendWith(MockInitialize.class)
class HttpStatusTest {

	private static final String HOST = "http://localhost:8080";
	@Test
	void testHttpStatus() throws IOException, AmbiguousMethodException {

		for (HttpStatus status : HttpStatus.values()) {

			try {
				Response response = new RequestBuilder()
						.get(String.format("%s/%s", HOST, status.name()))
						.perform();

				assertEquals(status, response.getStatus());
				if(status.isError()) {
					assertEquals(status.getMeaning(), (String) response.getBody());
				}
			} catch (Exception e) {
				System.out.println(status.name());
			}
		}

	}

}
