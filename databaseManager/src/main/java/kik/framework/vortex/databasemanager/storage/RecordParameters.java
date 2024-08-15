package kik.framework.vortex.databasemanager.storage;

/**
 * Data structure that contains how the data must be saved
 *
 * @param data          {@link Type} of the data
 * @param originalClass the java type
 * @param length        max length of the registry
 * @param autoIncrement tells if the registry must autoincrement
 * @see RecordInfo
 */
public
record RecordParameters(Type data, Class<?> originalClass, int length, int precision, int scale, boolean autoIncrement){


    public
    RecordParameters(Type data, Class<?> original){
        this(data, original, 255, -1, -1, false);

    }

    public
    RecordParameters(Type data, Class<?> original, boolean autoIncrement){
        this(data, original, 255, -1, -1, autoIncrement);

    }
}
