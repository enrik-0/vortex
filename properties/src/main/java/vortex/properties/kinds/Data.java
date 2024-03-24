package vortex.properties.kinds;

import vortex.properties.filemanager.PropertyParser;
import vortex.utils.StringUtils;

public class Data {

	public enum Format implements Family {

		DATE;
		private Object value;
		Format() {
			value = PropertyParser.getInstance().get(StringUtils
					.proccessProperty(".", this.getClass(), this.name()));
		}
		@Override
		public Object value() {
			return value;
		}
	}
}
