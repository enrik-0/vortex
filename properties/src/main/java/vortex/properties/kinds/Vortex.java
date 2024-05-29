package vortex.properties.kinds;

import vortex.properties.filemanager.PropertyParser;
import vortex.utils.StringUtils;

public enum Vortex implements Family{

    ;
    private Object value;
	@Override
	public Object value() {
		return value;

	}
    public enum Test implements Family {
	
	ENABLED;
	private Object value;
	Test() {
		value = PropertyParser.getInstance().get(StringUtils
				.proccessProperty(".", this.getClass(), this.name()));
	}

	@Override
	public Object value() {
		return value;

	}
    }

}
