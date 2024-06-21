package kik.framework.vortex.databasemanager.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.emory.mathcs.backport.java.util.Collections;
import kik.framework.vortex.databasemanager.annotation.Column;
import kik.framework.vortex.databasemanager.storage.DBTable;
import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import kik.framework.vortex.databasemanager.storage.RecordInfo;
import kik.framework.vortex.databasemanager.storage.Relation;

public final class JPAUtils {

    public static Map<String, Object> getValues(Object entity) throws IllegalAccessException {
        Map<String, Object> valuesMap = new HashMap<>();
        Collection<Field> fields = getAllFields(entity.getClass());
        DBTable table = DatabaseStorage.getInstance().getTable(entity.getClass());

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = getFieldName(field);
            valuesMap.put(fieldName, field.get(entity));
            field.setAccessible(false);
        }

        List<String> tableNames = table.records().stream()
                                          .map(RecordInfo::name)
                                          .collect(Collectors.toList());

        for (String name : tableNames) {
            if (!valuesMap.containsKey(name.toLowerCase())) {
                Relation relation = table.getRelation(name);
            }
        }

        return valuesMap;
    }

    private static String getFieldName(Field field) {
        for (Annotation annotation : field.getAnnotations()) {
            if (annotation instanceof Column) {
                return ((Column) annotation).name();
            }
        }
        return field.getName().toLowerCase();
    }

    private static Collection<Field> getAllFields(Class<?> clazz) {
        Collection<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

}
