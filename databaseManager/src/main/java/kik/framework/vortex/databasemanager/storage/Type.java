package kik.framework.vortex.databasemanager.storage;

import kik.framework.vortex.databasemanager.exception.DataTypeException;

public interface Type {
    Type parse(Class<?> clazz) throws DataTypeException;

    boolean isPrimitive();

    Class<?> parseToJava();
    boolean isString();
}
