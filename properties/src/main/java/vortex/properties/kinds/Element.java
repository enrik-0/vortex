package vortex.properties.kinds;

import java.util.Locale;
import java.util.regex.Pattern;

public class Element implements Comparable<Element> {

	private String name;
	private Pattern pattern;
	private boolean recursive;

	public Element(String name, String pattern) {
		setName(name);
		this.pattern = Pattern.compile(pattern);
	}

	public Element(String name, String pattern, boolean recursive) {
		setName(name);
		this.pattern = Pattern.compile(pattern);
		this.recursive = recursive;
	}
	public Element(String name, boolean recursive) {
		setName(name);
		this.recursive = recursive;
	}

	private void setName(String name) {
		this.name = name.trim().toLowerCase(Locale.getDefault());
	}
	public String getName() {
		return name;
	}
	public Pattern getPattern() {
		return pattern;
	}

	public boolean isRecursive() {
		return recursive;
	}
	@Override
	public int compareTo(Element other) {
		if (equals(other)) {
			if (other.isRecursive() == recursive) {
				return 1;
			}
			if (other.isRecursive() && !recursive) {
				return 2;
			} else {
				return 0;
			}
		} else {

			return -1;
		}
	}

	public boolean matches(String text) {
		return pattern.matcher(text).matches();

	}
	@Override
	public boolean equals(Object other) {
		if (other != null && other.getClass().equals(this.getClass())) {
			var element = (Element) other;
			return name.equals(element.getName()) && pattern.pattern()
					.equals(element.getPattern().pattern());
		}
		return false;
	}

}