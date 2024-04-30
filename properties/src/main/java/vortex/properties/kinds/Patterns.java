package vortex.properties.kinds;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Patterns {

	private static Patterns instance;
	private List<Element> patterns;

	private Patterns() {
		patterns = new ArrayList<>();
	}
	public static Patterns getInstance() {
		if (instance == null) {
			synchronized (Patterns.class) {
				instance = new Patterns();
			}

		}

		return instance;
	}

	public Element getPattern(String name) {
		Element pattern = null;

		for (Element element : patterns) {

			if (pattern != null) {
				continue;
			}
			if ((element.isRecursive()
					&& name.toLowerCase(Locale.getDefault()).contains(element.getName()))
					|| element.getName().equals(name.toLowerCase(Locale.getDefault()))) {
				pattern = element;
			}
		}

		return pattern;
	}

	public void addPattern(Element element) {
		addPattern(element.getName(), element.getPattern().pattern(),
				element.isRecursive());

	}
	public void addPattern(String name, String pattern, boolean recursive) {
		int compared;
		var exists = false;
		int oldValueIndex = -1;
		var newElement = new Element(name, pattern, recursive);
		for (Element element : patterns) {
			if (exists) {
				continue;
			}
			compared = element.compareTo(newElement);
			if (compared == 0 || compared == 1) {
				exists = true;
			}
			if (compared == 2) {
				oldValueIndex = patterns.indexOf(element);
			}
		}
		if (oldValueIndex != -1) {
			patterns.remove(oldValueIndex);
		}
		if (!exists) {
			patterns.add(newElement);
		}
	}

}
