package vortex.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.apache.log4j.lf5.util.DateFormatManager;
import org.codehaus.plexus.component.configurator.converters.basic.DateConverter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class MappingUtils {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	private MappingUtils() {
		
		SimpleDateFormat userFormat;
		//userFormat = new SimpleDateFormat(DATA.FORMAT.DATE_FORMAT);
		//ObjectMapper w = new ObjectMapper();


	}
	
	public static String parseDate(String dateToParse, String datePattern, String parsePattern) {
		SimpleDateFormat userFormat;
		SimpleDateFormat newFormat;
		Date userFormatted;
		String newDate;
		try {
			userFormat = new SimpleDateFormat(datePattern);
			newFormat = new SimpleDateFormat(parsePattern);

			userFormatted = userFormat.parse(dateToParse);
			newDate = newFormat.format(userFormatted);
			return newDate;
		}catch(IllegalArgumentException | ParseException e) {
			System.out.println("date format dosent exists");
			return null;
		} 
	
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
	
	public static String getInputContent(InputStream stream) {
		String line;
		var builder = new StringBuilder();
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
	public static Object mapToPrimitive(byte[] content,
			String buffer) throws IOException {
		Long temp;
		Object body;
		if (Regex.isBoolean(buffer)) {
			body = MappingUtils.map(content, Boolean.class);
		} else if (Regex.isIntegerOrLong(buffer)) {
			temp = (Long) MappingUtils.map(content, Long.class);
			if (Asserttions.inrange(temp, Byte.MAX_VALUE, Byte.MIN_VALUE)) {
				body = MappingUtils.map(content, byte.class);

			} else if (Asserttions.inrange(temp, Integer.MAX_VALUE,
					Integer.MIN_VALUE)) {
				body = MappingUtils.map(buffer, Integer.class);
			} else {
				body = temp;
			}
		} else {
			if (Regex.isFloating(buffer)) {
				body = MappingUtils.map(content, Double.class);
			}else {
				if(content.length != 0) {
					body = MappingUtils.map(buffer, String.class);
				}else {
					body = "";
				}
			}
		}
		return body;
	}
	

}
