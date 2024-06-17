package kik.framework.vortex.databasemanager.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Collections;
import kik.framework.vortex.databasemanager.annotation.Column;
import kik.framework.vortex.databasemanager.storage.DBTable;
import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import kik.framework.vortex.databasemanager.storage.RecordInfo;
import kik.framework.vortex.databasemanager.storage.Relation;
import vortex.utils.MappingUtils;

public final class JPAUtils {

    public static Map<String, Object> getValues(Object entity) throws IllegalAccessException, IllegalArgumentException {

	Map<String, Object> map = new HashMap<>();
	Field[] entityFields = entity.getClass().getDeclaredFields();

	Collection<Field> fields = getFields(entity.getClass());
	DBTable table = DatabaseStorage.getInstance().getTable(entity.getClass());
	for (Field field : fields) {
	    field.setAccessible(true);
	    String fieldName = field.getName().toLowerCase();
	    for (Annotation annotation : field.getAnnotations()) {
		if (annotation instanceof Column) {
		    fieldName = ((Column) annotation).name();
		}
	    }
	    map.put(fieldName, field.get(entity));
	    field.setAccessible(false);
	}
	List<String> names = new ArrayList<>();
	for (RecordInfo r : table.records()) {
	    names.add(r.name());
	}
	for (String name : names) {
	    if (!map.keySet().contains(name)) {
		System.out.println("a");
		Relation relation = table.getRelation(name);

	    }
	}

	return map;
    }

    private static Collection<Field> getFields(Class<?> entity) {
	Collection<Field> fields = new ArrayList<>();
	Collections.addAll(fields, entity.getDeclaredFields());
	if (!entity.getSuperclass().equals(Object.class)) {
	    fields.addAll(getFields(entity.getSuperclass()));
	}
	return fields;

    }

}
