package vortex.http;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import vortex.http.exchange.Response;
import vortex.http.status.HttpStatus;
import vortex.http.utils.MappingUtils;
import vortex.test.Mock;
import vortex.test.RequestBuilder;
import vortex.test.exception.AmbiguousMethodException;

/**
 * Unit test for simple App.
 */
@ExtendWith(MockInitialize.class)
class RequestTest {

	private static final String HOST = "http://localhost:8080";
/*
	@BeforeAll
	static void setUp()
			throws IOException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		Mock.getInstance();
	}
	@AfterAll
	static void shutdown() {
		Mock.stop();
	}
	*/

	@Test
	void testUriNonExists() throws IOException, AmbiguousMethodException {
		for(HttpStatus s : HttpStatus.values()) {
			System.out.println(s.name());
		}
		Response response = new RequestBuilder().post(HOST + "/buenas").perform();
		assertEquals(HttpStatus.NOT_FOUND, response.getStatus());

	}

	@Test
	void testUriExits() throws IOException, AmbiguousMethodException {
		Map<String, Object>body = fillMix(23);
		Response response = new RequestBuilder().put(HOST + "/users/register")
				.setBody(body).perform();
		assertEquals(body, response.getBody());
	}

	@Test
	void testMapResponseInteger() throws IOException, AmbiguousMethodException {
		Response response;
		int i;
		Random random = new Random();
		int j = random.nextInt(20);
		j = j < 0 ? j * -1 : j + 1;
		for (i = 0; i < j; i++) {
			var map = fillInteger((new Random()).nextInt(30));
			response = post(map, "/map/integer");
			assertEquals(map,  response.getBody());
			response = post(map, "/map/integerResponse");
			assertEquals(map, response.getBody());
		}

	}

	@Test
	void testMapResponseString() throws IOException, AmbiguousMethodException {
		Response response;
		int i;
		Random random = new Random();
		int j = random.nextInt(20);
		j = j < 0 ? j * -1 : j + 1;

		for (i = 0; i < j; i++) {
			var map = fillString((new Random()).nextInt(30));
			response = post(map, "/map/string");
			assertEquals(map, response.getBody());
			response = post(map, "/map/stringResponse");
			assertEquals(map, response.getBody());
		}

	}

	@Test
	void testMapResponseBoolean() throws IOException, AmbiguousMethodException {
		Response response;
		int i;
		Random random = new Random();
		int j = random.nextInt(20);
		j = j < 0 ? j * -1 : j + 1;

		for (i = 0; i < j; i++) {
			var map = fillBoolean((new Random()).nextInt(30));
			response = post(map, "/map/boolean");
			assertEquals(map, response.getBody());
			response = post(map, "/map/booleanResponse");
			assertEquals(map, response.getBody());
		}

	}

	@Test
	void testMapResponseMix() throws IOException, AmbiguousMethodException {
		Response response;
		int i;
		Random random = new Random();
		int j = random.nextInt(20);
		j = j < 0 ? j * -1 : j + 1;

		for (i = 0; i < j; i++) {
			var map = fillMix((new Random()).nextInt(30));
			response = post(map, "/map/mix");
			assertEquals(map, response.getBody());
			response = post(map, "/map/mixResponse");
			assertEquals(map, response.getBody());
		}
		System.out.println(j);

	}
	@Test
	void testMapResponseMaps() throws IOException, AmbiguousMethodException {
		Response response;
		int i;
		Random random = new Random();
		int j = random.nextInt(20);
		j = j < 0 ? j * -1 : j + 1;

		for (i = 0; i < j; i++) {
			var map = fillMaps((new Random()).nextInt(30));
			response = post(map, "/map/maps");
			assertEquals(map, response.getBody());
			response = post(map, "/map/mapsResponse");
			assertEquals(map, response.getBody());
		}
		System.out.println(j);
	}

	private Response post(Object body, String uri)
			throws IOException, AmbiguousMethodException {
		return new RequestBuilder().setBody(body).post(HOST + uri).perform();
	}

	private Map<String, Integer> fillInteger(int ammount) {

		ammount = ammount < 0 ? ammount * -1 : ammount + 1;
		var integers = new HashMap<String, Integer>();
		var random = new Random();
		for (int i = 0; i < ammount; i++) {
			integers.put(String.format("num %d", i), random.nextInt());

		}

		return integers;

	}
	private Map<String, String> fillString(int ammount) {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		var map = new HashMap<String, String>();
		var builder = new StringBuilder();
		var random = new Random();
		var length = 0;

		for (int i = 0; i < ammount; i++) {
			builder = new StringBuilder();

			length = random.nextInt(100);
			length = length < 0 ? length * -1 : length + 1;

			for (int j = 0; j < length; j++) {

			int chosen = random.nextInt(chars.length());
			builder.append(chars.charAt(chosen));

		}

		map.put("string" + i, builder.toString());
		}
		return map;
	}
	private Map<String, Boolean> fillBoolean(int ammount) {
		var map = new HashMap<String, Boolean>();
		var random = new Random();

		for (int i = 0; i < ammount; i++) {
			map.put("bool" + i, random.nextBoolean());
		}
		return map;
	}

	private Map<String, Object> fillMix(int ammount) {
		var map = new HashMap<String, Object>();
		var random = new Random();
		for (int i = 0; i < ammount; i++) {
			var selector = random.nextInt();
			switch (selector % 3) {
				case 0 :
					map.put("integer " + i, random.nextInt());
					break;
				case 1 :
					String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
					var length = random.nextInt(100);
					var builder = new StringBuilder();
					length = length < 0 ? length * -1 : length + 1;
					for (int j = 0; j < length; j++) {

						int chosen = random.nextInt(chars.length());
						builder.append(chars.charAt(chosen));

					}
					map.put("string" + i, builder.toString());
					break;
				case 2 :
					map.put("boolean " + i, random.nextBoolean());
					break;
			}
		}

		return map;
	}
	HashMap<String, HashMap<String, Object>> fillMaps(int ammount){
		HashMap<String, HashMap<String, Object>> map = new HashMap<>();
		for (int i = 0; i < ammount; i++) {
			map.put("map" + i, (HashMap<String, Object>) fillMix(ammount));
		}
		
		return map;
		
	}
}
