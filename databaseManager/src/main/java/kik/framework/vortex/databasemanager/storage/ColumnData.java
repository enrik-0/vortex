package kik.framework.vortex.databasemanager.storage;


/**
 * Record that holds metadata needed to create a registry
 *
 * @param name
 * @param length
 * @param precision
 * @param scale
 * @param autoIncrement
 */
public
record ColumnData(String name, int length, int precision, int scale, boolean autoIncrement){

    ColumnData(String name){
        this(name, 255, -1, -1, false);
    }

    /**
     * @return A copy of the current record
     */
    public
    ColumnData copy(){

        return new ColumnData(name, length, precision, scale, autoIncrement);
    }
}
