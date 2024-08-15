package kik.framework.vortex.databasemanager.storage;

import kik.framework.vortex.databasemanager.exception.DataTypeException;


/**
 * Contract of a database type
 */
public
interface Type{
    /**
     * from the given class what database type should be
     *
     * @param clazz
     * @return the {@link Type} that is used to saved the given class
     * @throws DataTypeException there no database type for that class
     */
    Type parse(Class<?> clazz) throws DataTypeException;

    /**
     * @return if the type is primitive
     */
    boolean isPrimitive();

    /**
     * @return convert this type into a java type
     */
    Class<?> parseToJava();

    /**
     * @return if the type is string
     */
    boolean isString();
}
