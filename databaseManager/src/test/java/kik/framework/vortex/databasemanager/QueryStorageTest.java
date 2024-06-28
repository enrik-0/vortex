package kik.framework.vortex.databasemanager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import kik.framework.vortex.databasemanager.assests.Person;
import kik.framework.vortex.databasemanager.storage.QueryStorage;
import vortex.annotate.components.Entity;
import vortex.annotate.manager.Storage;

class QueryStorageTest {
    @BeforeAll
    static void setUp() {
	Storage.getInstance().addAnnotationType(Entity.class.getTypeName());
	Storage.getInstance().addClass(Entity.class.getTypeName(), Person.class);
    }
    @Test
    void testQueryStorage() {
	QueryStorage storage = new QueryStorage();
	assertNotNull(storage);
    }

    @Test
    void testAddVisited() {
	QueryStorage storage = new QueryStorage();
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("id", 2);
	storage.addVisited(Person.class, map);
	var newMap = new HashMap<String, Object>();
	newMap.put("id", 2);
	assertTrue(storage.isVisited(Person.class, newMap));
	storage.addVisited(Person.class, newMap);
    }

    @Test
    void testAddEntity() {
	QueryStorage storage = new QueryStorage();
	Person object = new Person();
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("id", 2);
	storage.addEntity(Person.class, map, object);
	var newMap = new HashMap<String, Object>();
	newMap.put("id", 2);
	Person actual = storage.getEntity(Person.class, newMap, new Person());
	assertEquals(object, actual);
    }


}
