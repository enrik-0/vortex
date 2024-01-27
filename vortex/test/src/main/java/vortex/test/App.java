package vortex.test;

import java.io.IOException;
import vortex.http.elements.Response;
import vortex.test.exception.AmbiguousMethodException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		try {
			Mock.getInstance();
			Response connection = new RequestBuilder()
					.get("http://localhost:8080/test/int").setTimeout(10000)
					.perform();
			int responseCode = connection.getStatus().value();
			StringBuilder responseBody = new StringBuilder();
			/*
			
			*/

			System.err.println(connection.getBody());

			responseBody.toString();

			Mock.stop();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (AmbiguousMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Mock.stop();
		}
	}
}
