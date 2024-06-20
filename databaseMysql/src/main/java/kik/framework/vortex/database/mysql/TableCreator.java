package kik.framework.vortex.database.mysql;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import kik.framework.vortex.databasemanager.annotation.Column;
import kik.framework.vortex.databasemanager.annotation.ID;
import kik.framework.vortex.databasemanager.annotation.ManyToMany;
import kik.framework.vortex.databasemanager.annotation.ManyToOne;
import kik.framework.vortex.databasemanager.annotation.Nullable;
import kik.framework.vortex.databasemanager.annotation.OneToMany;
import kik.framework.vortex.databasemanager.annotation.OneToOne;
import kik.framework.vortex.databasemanager.annotation.Table;
import kik.framework.vortex.databasemanager.annotation.Unique;
import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.exception.RelationTypeException;
import kik.framework.vortex.databasemanager.storage.ColumnData;
import kik.framework.vortex.databasemanager.storage.DBTable;
import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import kik.framework.vortex.databasemanager.storage.RecordInfo;
import kik.framework.vortex.databasemanager.storage.RecordParameters;
import kik.framework.vortex.databasemanager.storage.Relation;
import kik.framework.vortex.databasemanager.storage.Type;
import kik.framework.vortex.databasemanager.utils.TableCreatorInterface;
import vortex.annotate.components.Entity;
import vortex.annotate.manager.Storage;
import vortex.utils.MappingUtils;

public class TableCreator implements TableCreatorInterface {

    private static TableCreator creator;
    private static Storage storage;

    private TableCreator() {
    }

    private TableCreator(Storage storage) {
	TableCreator.storage = storage;
    }

    public static TableCreator getInstance(Storage storage) {
	synchronized (TableCreator.class) {
	    if (creator == null) {
		creator = new TableCreator(storage);
	    }

	}
	return creator;
    }

    public static TableCreator getInstance() {
	synchronized (TableCreator.class) {
	    if (creator == null) {
		creator = new TableCreator();
	    }

	}

	return creator;
    }

    @Override
    public DBTable createTable(Class<?> clazz) throws DataTypeException, SQLException, RelationTypeException {
	String name;
	DBTable table;
	boolean id = false;
	boolean nullable = false;
	boolean unique = false;
	boolean relationship = false;
	RecordParameters parameters;
	ColumnData column = null;
	List<Relation> relations = new ArrayList<>();
	List<HashMap<String, Object>> recordWithRelation = new ArrayList<>();
	var tableAnnotation = clazz.getAnnotation(Table.class);

	if (tableAnnotation != null) {
	    tableAnnotation = (Table) tableAnnotation;
	    name = tableAnnotation.value();
	} else {
	    name = clazz.getSimpleName().toLowerCase() + "s";
	}

	table = new DBTable(name, clazz);
	DatabaseStorage.getInstance().addTable(table);
	Class<?> w = clazz.getSuperclass();
	Class<?> father = null;
	try {
	    father = Storage.getInstance().getComponent(Entity.class).stream().filter(c -> {
		return c.getName().equals(w.getName());
	    }).findFirst().get();
	} catch (NoSuchElementException e) {
	}

	if (father != null) {
	    DBTable fatherTable = DatabaseStorage.getInstance().getTable(father);
	    if (fatherTable == null) {
		fatherTable = createTable(father);
	    }
	    for (RecordInfo ids : fatherTable.id()) {
		table.addRecord(ids);
		table.addRelation(new Relation(ids, fatherTable, ids, true, "inheritance"));
	    }
	}
	for (Field field : clazz.getDeclaredFields()) {
	    for (Annotation annotation : field.getAnnotations()) {
		if (annotation.annotationType().getSimpleName().equals(Column.class.getSimpleName())) {
		    column = getColumnData(annotation);
		} else {
		    if (annotation.annotationType().getSimpleName().equals(ID.class.getSimpleName())) {
			id = true;
		    } else if (annotation.annotationType().getSimpleName().equals(Nullable.class.getSimpleName())) {
			nullable = true;
		    }
		    if (annotation.annotationType().getSimpleName().equals(Unique.class.getSimpleName())) {
			unique = true;
		    }

		    if (isRelationshipAnnotation(annotation)) {
			recordWithRelation.add(createHashMap(field, column, table, annotation));
		    }

		}
	    }
	    /*
	     * DatabaseStorage.getInstance().getTable(relation.destinationTable())
	     * .getRecord(relation.destination()); if
	     * (relation.type().equals(OneToOne.class.getSimpleName()) ||
	     * relation.type().equals(ManyToOne.class.getSimpleName())) {
	     * 
	     * table.addRecord(createRecord(relation.origin(), info.data(), id, unique,
	     * nullable, true));
	     * 
	     * } else if (relation.type().equals(OneToMany.class.getSimpleName())) {
	     * table.addRecord(createRecord(relation.origin(), info.data(), id, unique,
	     * nullable, false)); } else if
	     * (relation.type().equals(ManyToMany.class.getSimpleName())) { } } } else {
	     * table.addRecord(createRecord(field, column, id, unique, nullable, true)); }
	     */
	    Type type = DataType.BOOLEAN.parse(field.getType());
	    boolean saved = true;
	    if (type.equals(DataType.LIST)) {
		saved = false;
		nullable = true;
	    }

	    table.addRecord(createRecord(field, column, id, unique, nullable, saved));
	    relations.forEach(table::addRelation);
	    relationship = id = unique = nullable = false;
	    column = null;
	}
	if (!table.hasId()) {
	    createIdRecord(table);
	}

	createRelations(recordWithRelation);
	return table;
    }

    private void createRelations(List<HashMap<String, Object>> recordWithRelation) {
	for (var map : recordWithRelation) {
	    try {
		createRelation((Field) map.get("field"), (ColumnData) map.get("column"), (DBTable) map.get("table"),
			(Annotation) map.get("annotation"));
	    } catch (DataTypeException e1) {
		e1.printStackTrace();
	    } catch (SQLException e1) {
		e1.printStackTrace();
	    } catch (RelationTypeException e1) {
		e1.printStackTrace();
	    }
	}
    }

    private HashMap<String, Object> createHashMap(Field field, ColumnData column, DBTable table,
	    Annotation annotation) {
	HashMap<String, Object> map = new HashMap<>();
	map.put("field", field);
	map.put("table", table);
	map.put("annotation", annotation);
	if (column == null) {

	    map.put("column", null);
	} else {
	    map.put("column", column.copy());
	}
	return map;
    }

    private String createForeignKey(DBTable table) {
	boolean cascade = false;
	StringJoiner foreignJoiner = new StringJoiner(",");
	var map = table.mapRelations();
	for (String referencedTable : map.keySet()) {
	    StringJoiner records = new StringJoiner(",");
	    StringJoiner referencedRecords = new StringJoiner(",");
	    for (Relation relation : map.get(referencedTable)) {
		if (relation.type().equals(ManyToOne.class.getSimpleName())) {
		    for (Relation r : DatabaseStorage.getInstance().getTable(relation.destinationTable())
			    .getAllRelations()) {
			if (table.name().equals(r.destinationTable())) {
			    records.add(relation.origin());
			    referencedRecords.add(relation.destination());
			    cascade = r.cascade();
			}
		    }
		}
		if ( relation.type().equals("inheritance")|| relation.type().equals(OneToOne.class.getSimpleName()) || table.created()) {
		    records.add(relation.origin());
		    referencedRecords.add(relation.destination());
		    cascade = relation.cascade();
		}
	    }
	    String format = "foreign key (%s) references %s(%s)";
	    if (cascade) {
		format += " on delete cascade";
	    }
	    if (records.length() != 0) {
		foreignJoiner
			.add(String.format(format, records.toString(), referencedTable, referencedRecords.toString()));
	    }

	}
	return foreignJoiner.toString();
    }

    public String createStatement(DBTable table) throws DataTypeException, SQLException {
	String sql;
	var joiner = new StringJoiner(",");
	sql = String.format("create table %s (", table.name());
	for (RecordInfo recor : table.records()) {
	    if (recor.saved() && recor.data().data() != null && !recor.data().data().equals(DataType.LIST)) {
		if (recor.identifier() && recor.nullable()) {
		    throw new DataTypeException(
			    "ID and Nullable are not allowed if you want to have nulls use unique instead");
		}
		if (!recor.data().data().equals(DataType.OBJECT) && !recor.data().data().equals(DataType.LIST)) {
		    var lineJoiner = new StringJoiner(" ");
		    if (recor.data().data().equals(DataType.VARCHAR)) {
			lineJoiner.add(String.format("%s %s(%d)", recor.name(), DataType.VARCHAR.name().toLowerCase(),
				recor.data().length() == -1 ? 255 : recor.data().length()));
		    } else if (recor.data().data().equals(DataType.INT)) {
			lineJoiner.add(String.format("%s %s", recor.name(), DataType.INT.name().toLowerCase()));
		    } else if (recor.data().data().equals(DataType.BIGINT)) {
			lineJoiner.add(String.format("%s %s", recor.name(), DataType.BIGINT.name().toLowerCase()));
		    } else if (recor.data().data().equals(DataType.BOOLEAN)) {
			lineJoiner.add(String.format("%s %s", recor.name(), DataType.BOOLEAN.name().toLowerCase()));
		    } else if (recor.data().data().equals(DataType.DOUBLE)) {
			lineJoiner.add(String.format("%s %s", recor.name(), DataType.DOUBLE.name().toLowerCase()));
		    } else if (recor.data().data().equals(DataType.SMALLINT)) {
			lineJoiner.add(String.format("%s %s", recor.name(), DataType.SMALLINT.name().toLowerCase()));
		    } else if (recor.data().data().equals(DataType.TINYINT)) {
			lineJoiner.add(String.format("%s %s", recor.name(), DataType.TINYINT.name().toLowerCase()));
		    }
		    if (recor.unique()) {
			lineJoiner.add("unique");
		    }
		    if (recor.nullable()) {
			lineJoiner.add("null");
		    } else {
			lineJoiner.add("not null");

		    }
		    if (recor.identifier() && recor.data().autoIncrement()) {
			lineJoiner.add("auto_increment");
		    }
		    joiner.add(lineJoiner.toString());
		}
	    }

	}

	StringJoiner idJoiner = new StringJoiner(",");
	for (RecordInfo recor : table.id()) {
	    idJoiner.add(recor.name());
	}
	joiner.add(String.format("primary key (%s)", idJoiner.toString()));
	String foreignKey = createForeignKey(table);
	if (!foreignKey.isBlank() && !foreignKey.isEmpty()) {
	    joiner.add(foreignKey);

	}
	System.out.println(sql + joiner.toString() + ");");
	sql += joiner.toString() + ");";

	return sql;
    }

    private RecordParameters getParameters(ColumnData column, Class<?> fieldType) throws DataTypeException {
	if (column != null) {
	    return new RecordParameters(DataType.BIGINT.parse(fieldType), fieldType, column.length(),
		    column.precision(), column.scale(), column.autoIncrement());
	}
	return new RecordParameters(DataType.BIGINT.parse(fieldType), fieldType, 255, -1, -1, false);

    }

    private RecordInfo createRecord(Field field, ColumnData column, boolean id, boolean unique, boolean nullable,
	    boolean saved) throws DataTypeException {
	String name = field.getName();
	if (column != null && (!column.name().isBlank() || !column.name().isEmpty())) {
	    name = column.name();
	}
	return createRecord(name, field.getName(), field.getType(), getParameters(column, field.getType()), id, unique,
		nullable, saved);

    }

    private RecordInfo createRecord(String name, String fieldName, Class<?> clazz, RecordParameters parameters,
	    boolean id, boolean unique, boolean nullable, boolean saved) throws DataTypeException {
	return new RecordInfo(name, fieldName, id, unique, nullable, saved, parameters);

    }

    private ColumnData getColumnData(Annotation annotation) {
	ColumnData data;
	String name = null;
	int length = 255;
	int scale;
	int precision = scale = -1;
	boolean autoIncrement = false;
	Class<?> clazz = annotation.annotationType();
	try {

	    for (Method m : clazz.getDeclaredMethods()) {
		switch (m.getName()) {
		case "name":
		    name = (String) m.invoke(annotation);
		    break;
		case "length":
		    length = (int) m.invoke(annotation);
		    break;
		case "precision":
		    precision = (int) m.invoke(annotation);
		    break;
		case "scale":
		    scale = (int) m.invoke(annotation);
		    break;
		case "autoIncrement":
		    autoIncrement = (boolean) m.invoke(annotation);
		    break;
		}
	    }
	} catch (Exception e) {

	}

	if (name == null) {
	    return null;
	}

	return new ColumnData(name, length, precision, scale, autoIncrement);

    }

    private void createIdRecord(DBTable table) {
	boolean exists = false;
	RecordInfo newRecord = null;
	ArrayList<RecordInfo> toDelete = new ArrayList<>();

	int count = table.records().size();
	for (RecordInfo r : table.records()) {
	    if (!r.nullable()
		    && (r.name().toLowerCase().contains("id")
			    && r.name().toLowerCase().contains(table.clazz().getSimpleName().toLowerCase()))
		    || r.name().toLowerCase().equals("id")) {
		toDelete.add(r);
		newRecord = new RecordInfo(r.name(), r.fieldName(), true, r.unique(), false, r.data());
	    }
	}

	if (!toDelete.isEmpty()) {
	    toDelete.forEach(table::removeRecord);
	}

	if (table.records().size() == count) {
	    newRecord = new RecordInfo("id", "", true, false, false,
		    new RecordParameters(DataType.BIGINT, Long.class, true));
	    table.addRecord(newRecord);
	}
	if (table.records().size() < count) {
	    table.addRecord(newRecord);
	}

    }

    private List<Relation> createRelation(Field field, ColumnData data, DBTable table, Annotation annotation)
	    throws DataTypeException, SQLException, RelationTypeException {
	String annotationName = annotation.annotationType().getSimpleName();
	if (!isRelationshipAnnotation(annotation)) {

	    throw new RelationTypeException();
	}
	boolean cascade = false;
	List<Relation> relations = new ArrayList<>();
	String name;
	try {
	    var s = annotation.annotationType().getMethod("cascade", null);
	    cascade = (boolean) annotation.annotationType().getMethod("cascade", null).invoke(annotation, null);

	} catch (NoSuchMethodException | SecurityException e) {
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
	} catch (NullPointerException e) {

	}
	if (data == null) {
	    name = field.getName();
	} else {
	    name = data.name();
	}
	var s = Storage.getInstance().getComponent(Entity.class);
	Class<?> clazz = Storage.getInstance().getComponent(Entity.class).stream().filter(c -> {
	    String fieldType = field.getType().getName();
	    if (annotationName.equals(OneToMany.class.getSimpleName())
		    || annotationName.equals(ManyToMany.class.getSimpleName())) {

		fieldType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName();
	    }
	    return fieldType.equals(c.getName());

	}).findFirst().get();
	DBTable referencedTable = DatabaseStorage.getInstance().getTable(clazz);

	if (referencedTable == null) {
	    referencedTable = createTable(clazz);
	}
	if (annotationName.equals(ManyToMany.class.getSimpleName())) {
	    DBTable newTable = createRelationTable(table, referencedTable);
	    DatabaseStorage.getInstance().addTable(newTable);

	}

	var identifiers = referencedTable.id();

	for (RecordInfo id : identifiers) {
	    relations.add(new Relation(name, referencedTable.name(), id.name(), cascade, annotation));
	}
	for (Relation relation : relations) {
	    if (relation.type().equals(OneToMany.class.getSimpleName())) {
		table.addRelation(relation);
	    } else
		for (RecordInfo r : referencedTable.id()) {
		    if (relation.type().equals(ManyToMany.class.getSimpleName())
			    && table.getRecord(relation.origin()).data().data().equals(DataType.LIST)) {
			table.addRelation(relation);
		    } else {
			RecordInfo actual = table.getRecord(relation.origin());
			if (actual == null) {
			    actual = createRecord(field, data, cascade, cascade, cascade, cascade);
			    table.addRecord(actual);
			}
			RecordInfo origin = r.merge(actual);
			table.removeRecord(table.getRecord(relation.origin()));
			if (relation.type().equals(ManyToOne.class.getSimpleName())) {

			    table.addRecord(new RecordInfo(origin.name(), field.getName(), actual.identifier(),
				    actual.unique(), actual.nullable(), actual.saved(), origin.data()));
			    table.addRelation(new Relation(origin.name(), referencedTable.name(), origin.fieldName(),
				    true, ManyToOne.class.getSimpleName()));
			} else if (relation.type().equals(OneToOne.class.getSimpleName())) {
			    for (RecordInfo recor : referencedTable.id()) {
				for (RecordInfo id : referencedTable.id()) {
				    String recordName = String.format("%s_%s", referencedTable.name(), id.name());

				    table.addRecord(new RecordInfo(recordName, field.getName(), actual.identifier(),
					    id.unique(), id.nullable(), id.saved(), id.data()));
				    table.addRelation(new Relation(recordName, referencedTable.name(), id.name(), true,
					    OneToOne.class.getSimpleName()));
				}
			    }

			} else {

			    table.addRecord(origin);
			    table.addRelation(new Relation(origin, referencedTable, r, relation.cascade(), annotation));
			}
		    }

		}

	    // table.addRecord(createRecord(relation.origin(), info.data(), id, unique,
	    // nullable, true));
	    /*
	     * RecordInfo info =
	     * DatabaseStorage.getInstance().getTable(relation.destinationTable())
	     * .getRecord(relation.destination()); if
	     * (relation.type().equals(OneToOne.class.getSimpleName()) ||
	     * relation.type().equals(ManyToOne.class.getSimpleName())) {
	     * table.addRecord(createRecord(relation.origin(), info.data(), id, unique,
	     * nullable, true)); } else if
	     * (relation.type().equals(OneToMany.class.getSimpleName())) {
	     * table.addRecord(createRecord(relation.origin(), info.data(), id, unique,
	     * nullable, false)); } else if
	     * (relation.type().equals(ManyToMany.class.getSimpleName())) { } else {
	     * table.addRecord(createRecord(field, column, id, unique, nullable, true)); }
	     */
	}
	return relations;
    }

    private DBTable createRelationTable(DBTable table, DBTable referencedTable) {
	String name = String.format("%s_%s", table.name(), referencedTable.name());
	DBTable newTable = DatabaseStorage.getInstance().getRelationTable(table, referencedTable);
	if (newTable == null) {
	    newTable = new DBTable(name, Relation.class, true);
	}
	for (RecordInfo id : referencedTable.id()) {
	    String recordName = String.format("%s_%s", referencedTable.name(), id.name());
	    newTable.addRecord(new RecordInfo(recordName, id.name(), id.identifier(), false, id.nullable(),
		    id.saved(), new RecordParameters(id.data().data(), id.data().originalClass(), id.data().length(), id.data().precision(), id.data().precision(), false)));
	    newTable.addRelation(new Relation(recordName, referencedTable.name(), id.name(), true,
		    ManyToMany.class.getSimpleName()));
	}

	for (RecordInfo id : table.id()) {
	    String recordName = String.format("%s_%s", table.name(), id.name());

	    newTable.addRecord(new RecordInfo(recordName, id.name(), id.identifier(), id.unique(), id.nullable(),
		    id.saved(), id.data()));
	    newTable.addRelation(
		    new Relation(recordName, table.name(), id.name(), true, ManyToMany.class.getSimpleName()));
	}

	return newTable;
    }

    private boolean isRelationshipAnnotation(Annotation annotation) {
	return annotation.annotationType().getSimpleName().equals(OneToOne.class.getSimpleName())
		|| annotation.annotationType().getSimpleName().equals(OneToMany.class.getSimpleName())
		|| annotation.annotationType().getSimpleName().equals(ManyToOne.class.getSimpleName())
		|| annotation.annotationType().getSimpleName().equals(ManyToMany.class.getSimpleName());
    }
}
