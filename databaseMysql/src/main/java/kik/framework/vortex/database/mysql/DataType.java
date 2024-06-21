package kik.framework.vortex.database.mysql;

import java.util.List;

import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.storage.Type;

public enum DataType implements Type {
    TINYINT, SMALLINT, INT, BIGINT, DOUBLE, VARCHAR, BOOLEAN, LIST, OBJECT;

    @Override
    public Type parse(Class<?> clazz) throws DataTypeException {
	Type type = null;
	switch (clazz.getSimpleName().toLowerCase()) {

	case "boolean":
	    type = BOOLEAN;
	    break;
	case "string":
	    type = VARCHAR;
	    break;
	case "byte":
	    type = TINYINT;
	    break;
	case "short":
	    type = SMALLINT;
	    break;
	case "int":
	    type = INT;
	    break;
	case "long":
	    type = BIGINT;
	    break;
	case "double", "float":
	    type = DOUBLE;
	    break;
	case "list":
	    type = LIST;
	    break;
	default:
	    type = OBJECT;
	    break;
	}
	return type;

    }

    @Override
    public boolean isPrimitive() {
	return LIST.equals(this) || OBJECT.equals(this);
    }

    @Override
    public Class<?> parseToJava() {
	Class<?> clazz = null;
	clazz = switch (this) {
	case BOOLEAN -> Boolean.class;
	case VARCHAR -> String.class;
	case TINYINT -> Byte.class;
	case SMALLINT -> Short.class;
	case INT -> Integer.class;
	case BIGINT -> Long.class;
	case DOUBLE -> Double.class;
	case LIST -> List.class;
	case OBJECT -> Object.class;
	};
	return clazz;
    }

    @Override
    public boolean isString() {
	// TODO Auto-generated method stub
	return this.equals(VARCHAR);
    }

}
