package kik.framework.vortex.databasemanager.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vortex.annotate.components.Entity;
import vortex.annotate.manager.Storage;

public class QueryStorage {

    private Map<String, Map<Map<String, Object>, Object>> entities;
    private Map<String, List<Map<String, Object>>> visited;

    public QueryStorage() {
	entities = new HashMap<>();
	visited = new HashMap<>();
	Storage.getInstance().getComponent(Entity.class).forEach(entity -> {
	    entities.put(entity.getSimpleName(), new HashMap<>());
	    visited.put(entity.getSimpleName(), new ArrayList<>());
	});
    }

    public void addVisited(Class<?> clazz, Map<String, Object> ids) {
	List<Map<String, Object>> visitedClass = visited.get(clazz.getSimpleName());
	if(!visitedClass.contains(ids)) {
	    visitedClass.add(ids);
	}
    }

    public <T> void addEntity(Class<?> clazz, Map<String, Object> ids, T entity) {
	entities.get(clazz.getSimpleName()).put(ids, entity);
    }

    public boolean isVisited(Class<?> clazz, Map<String, Object> relatedIds) {
	return visited.get(clazz.getSimpleName()).contains(relatedIds);
    }

    public <T>  T getEntity(Class<?> clazz, Map<String, Object> relatedIds, T entity) {
	return (T) entities.get(clazz.getSimpleName()).get(relatedIds);
    }

}
