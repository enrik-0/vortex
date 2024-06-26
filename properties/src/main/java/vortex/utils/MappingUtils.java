package vortex.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class MappingUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MappingUtils() {

	SimpleDateFormat userFormat;
	// userFormat = new SimpleDateFormat(DATA.FORMAT.DATE_FORMAT);
	// ObjectMapper w = new ObjectMapper();

    }

    public static String parseDate(String dateToParse, String datePattern, String parsePattern) {
	SimpleDateFormat userFormat;
	SimpleDateFormat newFormat;
	Date userFormatted;
	try {
	    userFormat = new SimpleDateFormat(datePattern);
	    newFormat = new SimpleDateFormat(parsePattern);

	    userFormatted = userFormat.parse(dateToParse);
	    return newFormat.format(userFormatted);
	} catch (IllegalArgumentException | ParseException e) {
	    System.out.println("date format dosent exists");
	    return null;
	}

    }

    public static String parseDate(String dateToParse, String parsePattern) {
	final String[] formats = { "dd-mm-yyyy", "MM-dd-yyyy", "yyyy-dd-mm", "dd-yyyy-mm", "dd/mm/yyyy", "MM/dd/yyyy",
		"yyyy/dd/mm", "yyyy/mm/dd" };
	String datePattern = null;
	for (String format : formats) {
	    var sdf = new SimpleDateFormat(format);
	    sdf.setLenient(false);
	    try {
		sdf.parse(dateToParse);
		datePattern = format;
		break;
	    } catch (ParseException e) {
	    }
	}

	return parseDate(dateToParse, datePattern, parsePattern);
    }

    public static Object map(Object body, Class<?> expected) {
	return objectMapper.convertValue(body, expected);
    }

    public static Object map(InputStream body, Class<?> expected) throws IOException {
	if (expected == String.class) {
	    return getInputContent(body);
	}
	return objectMapper.readValue(body, expected);
    }

    public static Object map(byte[] body, Class<?> expected) throws IOException {
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

    public static Object mapToPrimitive(byte[] content, String buffer) throws IOException {
	Long temp;
	Object body;
	if (Regex.isBoolean(buffer)) {
	    body = MappingUtils.map(content, Boolean.class);
	} else if (Regex.isIntegerOrLong(buffer)) {
	    temp = (Long) MappingUtils.map(content, Long.class);
	    if (Asserttions.inRange(temp, Byte.MAX_VALUE, Byte.MIN_VALUE)) {
		body = MappingUtils.map(content, byte.class);

	    } else if (Asserttions.inRange(temp, Integer.MAX_VALUE, Integer.MIN_VALUE)) {
		body = MappingUtils.map(buffer, Integer.class);
	    } else {
		body = temp;
	    }
	} else {
	    if (Regex.isFloating(buffer)) {
		body = MappingUtils.map(content, Double.class);
	    } else {
		if (content.length != 0) {
		    body = MappingUtils.map(buffer, String.class);
		} else {
		    body = "";
		}
	    }
	}
	return body;
    }

    public static Object mapToPrimitive(Object content, String buffer){
	Long temp;
	Object body;
	if (Regex.isBoolean(buffer)) {
	    body = MappingUtils.map(content, Boolean.class);
	} else if (Regex.isIntegerOrLong(buffer)) {
	    temp = (Long) MappingUtils.map(content, Long.class);
	    if (Asserttions.inRange(temp, Byte.MAX_VALUE, Byte.MIN_VALUE)) {
		body = MappingUtils.map(content, byte.class);

	    } else if (Asserttions.inRange(temp, Integer.MAX_VALUE, Integer.MIN_VALUE)) {
		body = MappingUtils.map(buffer, Integer.class);
	    } else {
		body = temp;
	    }
	} else {
	    if (Regex.isFloating(buffer)) {
		body = MappingUtils.map(content, Double.class);
	    } else {
		body = MappingUtils.map(buffer, String.class);
	    }
	}
	return body;
    }

    public static Map<String, Object> mapObject(Object object, Map<Object, Map<String, Object>> processed) {
	Map<String, Object> map = new HashMap<>();
	if (!processed.containsKey(object)) {
	    processed.put(object, map);
	    for (Field field : getFields(object.getClass())) {
		field.setAccessible(true);
		Object value = null;
		try {
		    value = field.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
		}

		if (value != null) {

		    if (!Asserttions.isPrimitive(value) && !Asserttions.isMap(value)) {
			if (Asserttions.isList(value)) {
			    String name = field.getName();
			    processed.get(object).put(name, mapListObjects((List) value, processed));
			} else {
			    map.put(field.getName(), mapObject(value, processed));
			}
		    } else {
			map.put(field.getName(), value);
		    }

		} else {
		    map.put(field.getName(), value);
		}

	    }
	} 
	return map;
    }

    private static List<Field> getFields(Class<?> clazz) {
	List<Field> fields = new ArrayList<>();
	if (!clazz.getSuperclass().equals(Object.class)) {
	    fields.addAll(getFields(clazz.getSuperclass()));
	}
	for (Field field : clazz.getDeclaredFields()) {
	    fields.add(field);
	}
	return fields;
    }

    public static List<Map<String, Object>> mapListObjects(List<Object> list,
	    Map<Object, Map<String, Object>> processed) {
	List<Map<String, Object>> mapped = new ArrayList<>();
	Map<String, Object> map;
	for (Object object : list) {
		map = new HashMap<>();
		if (!processed.containsKey(object)) {
		    map = mapObject(object, processed);
		} 
		mapped.add(map);
	}

	return mapped;

    }
}
