package vortex.properties.filemanager;

import static java.nio.charset.StandardCharsets.UTF_8;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import vortex.properties.kinds.Element;
import vortex.properties.kinds.Patterns;
import vortex.utils.MappingUtils;
public final class FileReader {

	private static final char COMMENT = '#';
	private static final Pattern FAMILY_DELIMITER = Pattern.compile("\\.",
			Pattern.UNICODE_CHARACTER_CLASS);
	private static final String DELIMITER = "=";
	private static final Pattern PROPERTY_FORMAT = Pattern.compile(
			"^\\w+(?:\\.\\w+)*(?:\\.\\w+)?\\s*=\\s*.+$",
			Pattern.UNICODE_CHARACTER_CLASS);

	private FileReader() {
	}
	public static void readPropertyFile(String fileToRead) throws IOException {
		HashMap<String, Object> lines = new HashMap<>();
		InputStream resource = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(fileToRead);
		if (resource != null) {
			processStream(resource, lines);
		}
		PropertyParser.getInstance().setProperties(lines);
	}
	private static HashMap<String, Object> processLine(String line,
			HashMap<String, Object> map) throws IOException {
		String content;
		var joiner = new StringJoiner(DELIMITER);
		String[] splitted = line.split(DELIMITER);
		for (var i = 1; i < splitted.length; i++) {
			joiner.add(splitted[i]);
		}
		content = joiner.toString().strip();
		Object value = MappingUtils.mapToPrimitive(
				content.getBytes(Charset.defaultCharset()), content);

		if (splitted[0].contains("pattern")) {

			processPattern(splitted[0], content);
		} else {
			map.put(splitted[0].strip(), value);
		}

		return map;

	}

	private static void processPattern(String property, Object content) {
		final var RECURSIVE = "recursive";
		String pattern;
		boolean isRecursive;
		String[] parts = property.split(FAMILY_DELIMITER.pattern());
		String patternName = parts.length > 1 ? parts[1] : "";

		Element element = null;
		Element existance = Patterns.getInstance().getPattern(patternName);

		if (!patternName.isEmpty()) {
			if (existance != null) {
				pattern = existance.getPattern() != null
						? existance.getPattern().pattern()
						: "";
				isRecursive = existance.isRecursive();

				if (property.contains(RECURSIVE)) {
					isRecursive = (boolean) MappingUtils.map(content,
							Boolean.class);
				}

				element = new Element(patternName, pattern, isRecursive);
			} else {
				pattern = "";
				isRecursive = false;

				if (property.contains(RECURSIVE)) {
					isRecursive = (boolean) MappingUtils.map(content,
							Boolean.class);
				} else {
					pattern = (String) content;
				}

				element = new Element(patternName, pattern, isRecursive);
			}
		}
		Patterns.getInstance().addPattern(element);
	}
	private static HashMap<String, Object> processStream(InputStream stream,
			HashMap<String, Object> lines) throws IOException {
		String line;
		try (var scanner = new Scanner(stream, UTF_8)) {
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				if (!line.isEmpty() && line.charAt(0) != COMMENT
						&& PROPERTY_FORMAT.matcher(line).matches()) {
					processLine(line.strip(), lines);
				}
			}
		}
		return lines;
	}
	public static Object searchInDefault(String name) throws IOException {
		var next = true;
		String line;
		Object value = null;
		HashMap<String, Object> map = new HashMap<>();
		InputStream resource = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("default-dev.properties");
		try (var scanner = new Scanner(resource, UTF_8)) {
			while (scanner.hasNextLine() && next) {
				line = scanner.nextLine();
				processLine(line, map);
				if (map.get(name) != null) {

					value = map.get(name);
					next = false;
				}
			}
		} catch (IOException e) {
        return value;
		}
		return value;

	}
}
