package kik.framework.vortex.databasemanager.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vortex.annotate.components.Entity;
import vortex.annotate.manager.Storage;
import kik.framework.vortex.databasemanager.exception.RepositoryNotExistsException;


/**
 * Storage used to save entities  of complex query's <br>
 * each complex query creates one
 */
public
class QueryStorage{

    private Map<String, Map<Map<String, Object>, Object>> entities;
    /**
     * the <b>keys</b> are the name of each entity defined <br>
     * value is a {@link List} of a {@link Map} with de {@link  kik.framework.vortex.databasemanager.annotation.ID ID
     * } of the saved object
     */
    private Map<String, List<Map<String, Object>>> visited;

    public
    QueryStorage(){
        entities = new HashMap<>();
        visited = new HashMap<>();
        Storage.getInstance().getComponent(Entity.class).forEach(entity -> {
            entities.put(entity.getSimpleName(), new HashMap<>());
            visited.put(entity.getSimpleName(), new ArrayList<>());
        });
    }

    /**
     * set an ID as visited
     *
     * @param clazz class that is going to be saved
     * @param ids   {@link kik.framework.vortex.databasemanager.annotation.ID ID} of the entity that was visited
     */
    public
    void addVisited(Class<?> clazz, Map<String, Object> ids){
        List<Map<String, Object>> visitedClass = visited.get(clazz.getSimpleName());
        if(!visitedClass.contains(ids)){
            visitedClass.add(ids);
        }
    }

    /**
     * @param entity The entity that is going to be registered
     * @param <T>    an entity class
     */
    public
    <T> void addEntity(T entity) throws RepositoryNotExistsException, NoSuchFieldException{
        Class<?> clazz = entity.getClass();
        Map<String, Object> id = DatabaseStorage.getInstance().getRepository(clazz).generateId(entity);
        addEntity(clazz, id, entity);
    }

    /**
     * @param clazz  the class assigned
     * @param ids    the {@link kik.framework.vortex.databasemanager.annotation.ID ID} of the given entity
     * @param entity The entity that is going to be registered
     * @param <T>    an entity class
     */
    public
    <T> void addEntity(Class<?> clazz, Map<String, Object> ids, T entity){
        entities.get(clazz.getSimpleName()).put(ids, entity);
    }

    /**
     * checks if current {@link kik.framework.vortex.databasemanager.annotation.ID ID} is visited
     *
     * @param clazz
     * @param relatedIds
     * @return if the id is visited
     */
    public
    boolean isVisited(Class<?> clazz, Map<String, Object> relatedIds){
        return visited.get(clazz.getSimpleName()).contains(relatedIds);
    }

    /**
     * @param clazz      what entity are we seeking
     * @param id         ID of the entity we want
     * @param deprecated entity
     * @param <T>
     * @return the entity saved
     */
    public
    <T> T getEntity(Class<?> clazz, Map<String, Object> id, T entity){
        return (T) entities.get(clazz.getSimpleName()).get(id);
    }

}
