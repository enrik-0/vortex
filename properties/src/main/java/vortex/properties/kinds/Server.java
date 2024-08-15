package vortex.properties.kinds;

import vortex.properties.filemanager.PropertyParser;
import vortex.utils.StringUtils;

/**
 * properties that modify something of the server
 */
public
enum Server implements Family{

    /**
     * Defines the listening port of the application <br>
     * default port is <b> 80</b>
     */
    PORT,
    /**
     * Defines the base URL for the application <br>
     * default path is <b> / </b>
     */
    CONTEXT_PATH,


    /**
     * defines the max number of threads
     */
    THREAD_NUMBER;
    private Object value;

    Server(){

        value = PropertyParser.getInstance().get(StringUtils.proccessProperty(".", this.getClass(), this.name()));
    }

    @Override
    public
    Object value(){
        return value;
    }
}
