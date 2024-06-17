package kik.framework.vortex.databasemanager.storage;

import java.sql.SQLException;

import kik.framework.vortex.databasemanager.exception.RelationTypeException;
import vortex.annotate.manager.Storage;

public abstract class StorageManager {


public abstract void initialize(Storage storage) throws SQLException, RelationTypeException;
}
