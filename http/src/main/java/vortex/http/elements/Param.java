package vortex.http.elements;

import vortex.properties.exception.FormatPatternException;
import vortex.properties.kinds.Element;
import vortex.properties.kinds.Patterns;
import vortex.utils.MappingUtils;

public class Param {
	private String name;
	private Object value;

	public Param(String name, Object value) throws FormatPatternException {
		this.name = name;
		setValue(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) throws FormatPatternException {
		Element pattern = Patterns.getInstance().getPattern(name);

		if (pattern == null) {

			this.value = value;

		} else {

			if (value.getClass().equals(String.class)) {

				if (pattern.matches(
					(String) MappingUtils.map(value, String.class))) {

					this.value = value;

				} else {
					throw new FormatPatternException(name, pattern);
				}
			}
		}
	}
}
