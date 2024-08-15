package vortex.properties.kinds;

import vortex.properties.filemanager.PropertyParser;
import vortex.utils.StringUtils;

/**
 * properties that handle database manner
 */
public
enum Database implements Family{
    ;
    private Object value;

    Database(){
        value = PropertyParser.getInstance().get(StringUtils.proccessProperty(".", this.getClass(), this.name()));
    }

    @Override
    public
    Object value(){
        return value;

    }

    /**
     * handles credentials of the database
     */
    public
    enum Credentials implements Family{
        /**
         * Connection URL of the database
         */
        URL,
        /**
         * database username
         */
        USERNAME,
        /**
         * database pwd
         */
        PWD;
        private Object value;

        Credentials(){
            value = PropertyParser.getInstance().get(StringUtils.proccessProperty(".", this.getClass(), this.name()));
        }

        @Override
        public
        Object value(){
            return value;

        }
    }
}
