package vortex.properties.filemanager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PropertyParser {

	private static PropertyParser instance;
	private HashMap<String, Object> properties;
	private PropertyParser() {
		properties = new HashMap<>();
	}

	public static PropertyParser getInstance() {
		if (instance == null) {
			instance = new PropertyParser();
		}

		return instance;

	}

	public Object get(String name) {

		Object value = properties.get(name);
		if(value == null) {
			return properties.computeIfAbsent(name,
					(Object k) -> {
			 try {
				 return FileReader.searchInDefault(name);
				} catch (IOException e) {
				 e.printStackTrace();
				 return null;
					}
			  }); 
			
		}
		return value;
	}

	public void setProperties(Map<String, Object> properties) {
		properties.forEach(this.properties::put);

	}
}
