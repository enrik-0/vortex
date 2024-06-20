package kik.framework.vortex.databasemanager;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.exception.RelationShipNotExistsException;
import kik.framework.vortex.databasemanager.exception.RepositoryNotExistsException;
import kik.framework.vortex.databasemanager.storage.QueryStorage;
import vortex.annotate.manager.Storage;

public interface Repository<T, Id> {
    /**
     * This function save entity and all its dependencies
     * 
     * @param entity
     * @return the entity given
     * @throws SQLException
     * @throws DataTypeException
     * @throws RepositoryNotExistsException
     * @throws RelationShipNotExistsException
     */
    T save(T entity)
	    throws SQLException, DataTypeException, RepositoryNotExistsException, RelationShipNotExistsException;

    /**
     * This function is used to control the entities found in a save Query It is not
     * intendeed to be used for final users.
     * 
     * @param entity
     * @param storage
     * @return entity
     * @throws RepositoryNotExistsException
     * @throws SQLException
     * @throws DataTypeException
     * @throws RelationShipNotExistsException
     */
    T save(T entity, QueryStorage storage)
	    throws RepositoryNotExistsException, SQLException, DataTypeException, RelationShipNotExistsException;

    /**
     * saves each entity given.
     * 
     * @param entities
     * @return the Collection given
     * @throws SQLException
     */
    Collection<T> saveAll(List<T> entities) throws SQLException;

    /**
     * This function is used to control the entities found in the Query It is not
     * intendeed to be used for final users.
     * 
     * @param id
     * @return the searched entity
     * @throws SQLException
     * @throws RelationShipNotExistsException
     * @throws RepositoryNotExistsException
     */
    T findById(Id id) throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException;

    /**
     * This function is used to control the entities found in the Query It is not
     * intendeed to be used for final users.
     * 
     * @param id
     * @param storage
     * @return the entity found
     * @throws SQLException
     * @throws RelationShipNotExistsException
     * @throws RepositoryNotExistsException
     */
    T findById(Id id, QueryStorage storage)
	    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException;

    /**
     * finds entities that match de columnName -> value in the Map
     * 
     * @param object
     * @return the entities found
     * @throws SQLException
     * @throws RelationShipNotExistsException
     * @throws RepositoryNotExistsException
     */
    Collection<T> findBy(Map<String, Object> object)
	    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException;

    /**
     * * This  is an auxiliar function  It is not
     * intendeed to be used for final users.
     * 
     * @param map
     * @param storage
     * @return entity found
     * @throws SQLException
     * @throws RelationShipNotExistsException
     * @throws RepositoryNotExistsException
     */
    T find(Map<String, Object> map, QueryStorage storage)
	    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException;

    /**
     * This function helps abstracting the ids creation when needed.
     * @param entity
     * @return map with the ids of the entity in map format
     * @throws NoSuchFieldException
     * @throws RepositoryNotExistsException
     */
    public Map<String, Object> generateId(T entity) throws NoSuchFieldException, RepositoryNotExistsException;

    /**
     * deletes the entity and depedending and  if cascade is enabled will delete the dependencies.
     * @param entity
     * @throws SQLException
     * @throws RepositoryNotExistsException
     * @throws RelationShipNotExistsException
     */
    void delete(T entity) throws SQLException, RepositoryNotExistsException, RelationShipNotExistsException;

    /**
     * Updates an entity in the database
     * if dependencies are Many to many or many to one it tries to save all the entities. 
     * @param entity
     * @return entity
     * @throws SQLException
     * @throws RepositoryNotExistsException
     */
    T update(T entity) throws SQLException, RepositoryNotExistsException;

    /**
     * gives the statements that would be executed if is deleted this entity
     * @param entity
     * @return all the delete statements that would be done
     * @throws SQLException
     * @throws RepositoryNotExistsException
     */
    List<String> deleteSQL(T entity) throws SQLException, RepositoryNotExistsException;

    /**
     * 
     * @return all the entities of the database of the entity type
     * @throws SQLException
     * @throws RelationShipNotExistsException
     * @throws RepositoryNotExistsException
     */
    List<T> findAll() throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException;

    /**
     * This function is used to control the entities found in the Query It is not
     * intendeed to be used for final users.
     * @param object
     * @param storage
     * @return the entities found
     * @throws SQLException
     * @throws RelationShipNotExistsException
     * @throws RepositoryNotExistsException
     */
    Collection<T> findBy(Object object, QueryStorage storage)
	    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException;

    /**
     * this function is used in case of saving a entity using its father repository.
     * @param entity
     * @return entity
     * @throws SQLException
     * @throws DataTypeException
     * @throws RepositoryNotExistsException
     * @throws RelationShipNotExistsException
     */
    T saveInheriance(Object entity)
	    throws SQLException, DataTypeException, RepositoryNotExistsException, RelationShipNotExistsException;

}
