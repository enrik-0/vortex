package vortex.properties.kinds;

import vortex.properties.filemanager.PropertyParser;
import vortex.utils.StringUtils;

public enum Application implements Family {

    DEBUG;

  private Object value;

    Application() {
	value = PropertyParser.getInstance().get(StringUtils.proccessProperty(".", this.getClass(), this.name()));
    }

    @Override
    public Object value() {
	return value;
    }

}
