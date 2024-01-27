package vortex.http.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class MappingUtils {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	private MappingUtils() {

	}

	public static Object map(Object body, Class<?> expected) {
		return objectMapper.convertValue(body, expected);
	}

	public static Object map(InputStream body, Class<?> expected)
			throws IOException {
		return objectMapper.readValue(body, expected);
	}
	public static Object map(byte[] body, Class<?> expected)
			throws IOException {
		return objectMapper.readValue(body, expected);
	}
	public static byte[] writeValueAsBytes(Object body) throws JsonProcessingException {
		
		return objectMapper.writeValueAsBytes(body);
	}
	public static boolean isPrimitive(Object body) {
		return isPrimitive(body.getClass(), Integer.class, Double.class,
				Boolean.class, Character.class, Byte.class, Short.class,
				Long.class, Float.class, String.class) || body.getClass().isPrimitive();
	}
	public static String getInputContent(InputStream stream) {
		String line;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(stream));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (Exception e) {
		}
		return builder.toString();
	}

	private static boolean isPrimitive(Class<?> bodyClass,
			Class<?>... classes) {
		for (Class<?> clazz : classes) {
			if (bodyClass == clazz) {
				return true;
			}
		}
		return false;
	}

}
