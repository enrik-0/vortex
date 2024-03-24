package vortex.properties.exception;

import vortex.properties.kinds.Element;

public class FormatPatternException extends Exception {

	public FormatPatternException(String elementWithoutThePattern,
			Element pattern) {
		super(String.format(
				"The parameter %s, dosent comply the given regex named %s with value %s",
				elementWithoutThePattern, pattern.getName(),
				pattern.getPattern().pattern()));
	}

}
