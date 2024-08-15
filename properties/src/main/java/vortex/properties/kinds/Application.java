package vortex.properties.kinds;

import vortex.properties.filemanager.PropertyParser;
import vortex.utils.StringUtils;

/**
 * Properties that involve all the application
 */
public
enum Application implements Family{

    /**
     * if debug output is enabled
     */
    DEBUG;

    private Object value;

    Application(){
        value = PropertyParser.getInstance().get(StringUtils.proccessProperty(".", this.getClass(), this.name()));
    }

    @Override
    public
    Object value(){
        return value;
    }

}
