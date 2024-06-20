package kik.framework.vortex.databasemanager.assests;

import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.storage.Type;

public enum DataType implements Type{
    
    StringType, IntType;

    @Override
    public Type parse(Class<?> clazz) throws DataTypeException {
	switch(clazz.getSimpleName()) {
	case "String":
	    return StringType;
	case "int":
	    return IntType;
	}
	return null;
    }

    @Override
    public boolean isPrimitive() {
	return true;
    }

    @Override
    public Class<?> parseToJava() {
	// TODO Auto-generated method stub
	Class<?> clazz = null;
	clazz = switch (this) {
	case StringType -> String.class;
	case IntType -> Integer.class;
	};
	return clazz;
    }

    @Override
    public boolean isString() {
	return this.equals(StringType);
    }
    
    

}
