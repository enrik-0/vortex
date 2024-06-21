package kik.framework.vortex.databasemanager.utils;

import java.sql.SQLException;

import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.exception.RelationTypeException;
import kik.framework.vortex.databasemanager.storage.DBTable;

public interface TableCreatorInterface {

    DBTable createTable(Class<?> clazz) throws DataTypeException, SQLException, RelationTypeException;
    
}
