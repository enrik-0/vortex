package vortex.properties.kinds;

import vortex.properties.filemanager.PropertyParser;
import vortex.utils.StringUtils;

public enum Database  implements Family{
    	;
	private Object value;
	Database() {
		value = PropertyParser.getInstance().get(StringUtils
				.proccessProperty(".", this.getClass(), this.name()));
	}

	@Override
	public Object value() {
		return value;

	} 
    public enum Credentials implements Family{
	URL,
	USERNAME,
	PWD;
	private Object value;
	Credentials() {
		value = PropertyParser.getInstance().get(StringUtils
				.proccessProperty(".", this.getClass(), this.name()));
	}

	@Override
	public Object value() {
		return value;

	}	
    }
}
