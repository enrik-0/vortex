package kik.framework.vortex.databasemanager;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.exception.RelationShipNotExistsException;
import kik.framework.vortex.databasemanager.storage.QueryStorage;
import vortex.annotate.manager.Storage;

public interface Repository<T, Id> {
    T save(T entity) throws SQLException, DataTypeException;

    Collection<T> saveAll(List<T> entities) throws SQLException;

    T findById(Id id) throws SQLException, RelationShipNotExistsException;
    T findById(Id id, QueryStorage storage) throws SQLException, RelationShipNotExistsException;

    Collection<T> findBy(Object object) throws SQLException, RelationShipNotExistsException;

    T find(Map<String, Object> map, QueryStorage storage) throws SQLException, RelationShipNotExistsException;

    public Map<String, Object> generateId(T entity) throws NoSuchFieldException;

    void delete(T entity) throws SQLException;

    T update(T entity) throws SQLException;

    List<String> deleteSQL(T entity) throws SQLException;

    List<T> findAll() throws SQLException, RelationShipNotExistsException;


    Collection<T> findBy(Object object, QueryStorage storage) throws SQLException, RelationShipNotExistsException;

}
