package kik.framework.vortex.database.mysql.connector;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import com.fasterxml.jackson.databind.deser.ValueInstantiator.Gettable;

import java.sql.Statement;
import edu.emory.mathcs.backport.java.util.Collections;
import kik.framework.vortex.database.mysql.DataType;
import kik.framework.vortex.databasemanager.Repository;
import kik.framework.vortex.databasemanager.annotation.ManyToMany;
import kik.framework.vortex.databasemanager.annotation.ManyToOne;
import kik.framework.vortex.databasemanager.annotation.OneToMany;
import kik.framework.vortex.databasemanager.annotation.OneToOne;
import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.exception.RelationShipNotExistsException;
import kik.framework.vortex.databasemanager.exception.RepositoryNotExistsException;
import kik.framework.vortex.databasemanager.storage.DBTable;
import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import kik.framework.vortex.databasemanager.storage.QueryStorage;
import kik.framework.vortex.databasemanager.storage.RecordInfo;
import kik.framework.vortex.databasemanager.storage.Relation;
import kik.framework.vortex.databasemanager.utils.JPAUtils;
import kik.framework.vortex.databasemanager.utils.SQLWriter;
import vortex.annotate.manager.Storage;
import vortex.utils.Asserttions;

/**
 * 
 * @param <T>
 * @param <Id> If the table has more than 1 id it must be used Map<String,
 *             Object> use the function generateId to create it.
 */
public class JPARepository<T, Id> implements Repository<T, Id> {
    public JPARepository() {
    }

    @Override
    public T save(T entity)
	    throws SQLException, DataTypeException, RepositoryNotExistsException, RelationShipNotExistsException {
	return save(entity, new QueryStorage());
    }

    public T save(T entity, QueryStorage storage)
	    throws RepositoryNotExistsException, SQLException, DataTypeException, RelationShipNotExistsException {
	var clazz = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	var tempEntity = storage.getEntity((Class<?>) clazz, generateId(entity), entity);
	if (tempEntity != null) {
	    return tempEntity;
	}
	Map<String, Object> values = null;
	Map<String, Object> id = null;
	try {
	    id = generateId(entity);
	    DBTable[] father = haveHeriance((Class<?>) clazz);
	    for (DBTable superior : father) {
		Object r = superior.clazz().cast(entity);
		Object c = null;
		try {
		    c = DatabaseStorage.getInstance().getRepository(superior.clazz())
			    .save(superior.clazz().cast(entity));
		} catch (SQLException e) {
		    c = DatabaseStorage.getInstance().getRepository(superior.clazz()).findBy(id);

		}
	    }

	    storage.addEntity((Class<?>) clazz, id, entity);
	    values = JPAUtils.getValues(entity);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	if (values != null) {
	    for (String key : values.keySet()) {
		Object value = values.get(key);
		if (!Asserttions.isPrimitive(value)) {
		    DBTable table = DatabaseStorage.getInstance().getTable((Class<?>) clazz);
		    for (Relation relation : table.getAllRelations()) {
			Map<String, Object> map = null;
			if (relation.type().equals(OneToOne.class.getSimpleName())) {
			    if (table.getRecord(relation.origin()).fieldName().equals(key)) {
				var r = temp(relation, value, id);
				values.put(relation.origin(), r.get(relation.destination()));
			    }
			} else if (relation.origin().equals(key)) {
			    if (Asserttions.isList(value)) {
				for (Object object : (List) value) {
				    var repo = DatabaseStorage.getInstance().getRepository(object.getClass());
				    try {
					repo.save(object, storage);
				    } catch (SQLException e) {
					try {
					    repo.findById(repo.generateId(object));
					} catch (NoSuchFieldException e1) {
					}

				    }
				    map = temp(relation, object, id);
				}
			    } else {
				map = temp(relation, value, id);
			    }

			} else {
			    map = temp(relation, value, id);
			}
			if (map != null)
			    values.put(key, map.get(relation.destination()));
		    }

		}
	    }
	}
	if (values == null) {
	    try {
		values = JPAUtils.getValues(entity);
	    } catch (IllegalAccessException | IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	Map<String, Object> temp = new HashMap<>();
	values.forEach((k, v) -> {
	    if (v == null || Asserttions.isPrimitive(v)) {
		temp.put(k, v);
	    }
	});

	String sql = SQLWriter.insert(getTable().clazz(), temp);
	Connector.getInstance().sendRequest(sql);
	saveChildren(entity);
	try {
	    values = JPAUtils.getValues(entity);
	} catch (IllegalAccessException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	if (values != null) {
	    for (String key : values.keySet()) {
		Object value = values.get(key);
		if (!Asserttions.isPrimitive(value)) {
		    DBTable table = DatabaseStorage.getInstance().getTable((Class<?>) clazz);
		    for (Relation relation : table.getAllRelations()) {
			Map<String, Object> map = null;
			if (relation.type().equals(OneToOne.class.getSimpleName())) {
			    if (table.getRecord(relation.origin()).fieldName().equals(key)) {
				var r = temp(relation, value, id);
				values.put(relation.origin(), r.get(relation.destination()));
			    }
			} else if (relation.origin().equals(key)) {
			    if (Asserttions.isList(value)) {
				for (Object object : (List) value) {
				    map = temp(relation, object, id);
				}
			    } else {
				map = temp(relation, value, id);
			    }

			} else {
			    map = temp(relation, value, id);
			}
			if (map != null)
			    values.put(key, map.get(relation.destination()));
		    }

		}
	    }
	}
	return entity;
    }

    public T saveInheriance(Object object)
	    throws SQLException, DataTypeException, RepositoryNotExistsException, RelationShipNotExistsException {

	DatabaseStorage.getInstance().getRepository(object.getClass()).save(object);
	return null;

    }

    private Map<String, Object> temp(Relation relation, Object object, Map<String, Object> ids)
	    throws DataTypeException, SQLException, RepositoryNotExistsException {
	Map<String, Object> map = new HashMap<String, Object>();
	DBTable relatedTable = DatabaseStorage.getInstance().getTable(relation.destinationTable());
	Map<String, Object> objectIds = null;
	Repository repository = null;
	if (relation.type().equals(ManyToMany.class.getSimpleName())) {
	    var rep = ((JPARepository) DatabaseStorage.getInstance().getRepository(relatedTable.clazz()));
	    objectIds = rep.generateId(object);
	    DBTable relationTable = DatabaseStorage.getInstance().getRelationTable(getTable(), relatedTable);

	    for (Relation rel : relationTable.relations()) {
		if (rel.destinationTable().equals(relatedTable.name())) {
		    for (String key : objectIds.keySet()) {
			if (key.equals(rel.destination())) {
			    map.put(rel.origin(), objectIds.get(key));
			}
		    }
		} else {

		    DBTable origin = getTable();
		    while (origin.isInheritance()) {
			origin = DatabaseStorage.getInstance().getTable(origin.clazz().getSuperclass());

		    }
		    if (rel.destinationTable().equals(origin.name())) {
			for (String key : ids.keySet()) {
			    if (key.equals(rel.destination())) {
				map.put(rel.origin(), ids.get(key));
			    }
			}
		    }
		}
	    }
	    String sql = SQLWriter.insert(relationTable, map);
	    try {
		Connector.getInstance().sendRequest(sql);

	    } catch (SQLIntegrityConstraintViolationException e) {
	    }

	} else {
	    repository = (JPARepository) DatabaseStorage.getInstance().getRepository(relatedTable.clazz());
	}

	if (repository != null && !Asserttions.isList(object)) {

	    map = ((JPARepository) repository).generateId(object);
	}
	return map;
    }

    private DBTable[] haveHeriance(Class<?> entity) {
	DBTable[] result;
	List<DBTable> tables = new ArrayList<>();

	Class<?> father = entity.getSuperclass();
	if (!father.equals(Object.class)) {
	    tables.add(DatabaseStorage.getInstance().getTable(father));
	    Collections.addAll(tables, haveHeriance(father));
	}

	result = new DBTable[tables.size()];
	return tables.toArray(result);

    }

    @Override
    public List<T> saveAll(List<T> entities) throws SQLException {
	QueryStorage storage = new QueryStorage();
	if (entities != null && !entities.isEmpty()) {
	    List<Map<String, Object>> values = new ArrayList<>();
	    entities.stream().forEach(e -> {
		try {
		    save(e, storage);
		} catch (SQLException | DataTypeException | RepositoryNotExistsException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		} catch (RelationShipNotExistsException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    });
	}
	return entities;

    }

    @Override
    public T findById(Id id) throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
	T entity;
	var clazz = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	DBTable table = DatabaseStorage.getInstance().getTable((Class<?>) clazz);
	if (id.getClass().getSimpleName().contains("Map")) {
	    entity = findById((Map<String, Object>) id, table);
	} else {
	    Map<String, Object> ids = new HashMap<>();
	    if (table.id().size() != 1) {
		throw new IllegalArgumentException(
			"The table have more than 1 identifier \n if you want to filter by that id use findBy method instead");
	    }
	    ids.put(table.id().get(0).name(), id);

	    entity = findById(ids, table);
	}
	return entity;
    }

    public T findById(Id id, QueryStorage storage)
	    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
	T entity;
	var clazz = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	DBTable table = DatabaseStorage.getInstance().getTable((Class<?>) clazz);
	if (id.getClass().getSimpleName().contains("Map")) {
	    entity = findById((Map<String, Object>) id, table, storage);
	} else {
	    Map<String, Object> ids = new HashMap<>();
	    if (table.id().size() != 1) {
		throw new IllegalArgumentException(
			"The table have more than 1 identifier \n if you want to filter by that id use findBy method instead");
	    }
	    ids.put(table.id().get(0).name(), id);
	    entity = findById(ids, table, storage);
	}
	return entity;
    }

    private T findById(Map<String, Object> id, DBTable table)
	    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
	return findById(id, table, new QueryStorage());

    }

    private T findById(Map<String, Object> id, DBTable table, QueryStorage storage)
	    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
	String sql = SQLWriter.find(id, table);
	T entity = instantiateEntity();
	entity = storage.getEntity(getTable().clazz(), id, entity);
	if (entity != null) {
	    return entity;
	}
	var statement = Connector.getInstance().getConnection().createStatement();
	var result = statement.executeQuery(sql);
	boolean response = result.next();
	if (response)
	    entity = populateEntity(result, statement, sql, id, storage);
	statement.close();
	return entity;

    }

    @Override
    public Collection<T> findBy(Map<String, Object> object)
	    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
	var clazz = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	try {
	    object = checkObject((Map<String, Object>) object, (Class<?>) clazz);
	} catch (DataTypeException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	String sql = SQLWriter.find((Map<String, Object>) object,
		DatabaseStorage.getInstance().getTable((Class<?>) clazz));
	var statement = Connector.getInstance().getInstance().sendResultRequest(sql);
	statement.executeQuery(sql);
	var result = statement.getResultSet();
	List<T> entities = new ArrayList<>();
	QueryStorage storage = new QueryStorage();
	while (result.next()) {
	    T entity = populateEntity(result, statement, sql, (Map<String, Object>) object, storage);
	    entities.add(entity);
	}
	statement.close();
	return entities;
    }

    @Override
    public Collection<T> findBy(Object object, QueryStorage storage)
	    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
	var clazz = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	try {
	    object = checkObject((Map<String, Object>) object, (Class<?>) clazz);
	} catch (DataTypeException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	String sql = SQLWriter.find((Map<String, Object>) object,
		DatabaseStorage.getInstance().getTable((Class<?>) clazz));
	var statement = Connector.getInstance().getInstance().sendResultRequest(sql);
	statement.executeQuery(sql);
	var result = statement.getResultSet();
	List<T> entities = new ArrayList<>();
	while (result.next()) {
	    T entity = populateEntity(result, statement, sql, (Map<String, Object>) object, storage);
	    entities.add(entity);
	}
	statement.close();
	return entities;
    }

    private Map<String, Object> checkObject(Map<String, Object> object, Class<?> clazz) throws DataTypeException {
	DBTable table = DatabaseStorage.getInstance().getTable(clazz);
	Map<String, Object> newMap = new HashMap<>();
	for (String key : object.keySet()) {
	    RecordInfo recor = table.getRecord(key);
	    Object value = object.get(key);
	    if (!Asserttions.isPrimitive(value)) {
		for (Relation relation : table.relations()) {
		    if (key.equals(relation.origin())) {
			List<Field> f = null;
			var r = DatabaseStorage.getInstance().getTable(relation.destinationTable());
			if (value.getClass().equals(r.clazz())) {
			    try {
				f = getAllFields(r.clazz());
				for (Field field : f) {

				    if (field.getName().equals(r.getRecord(relation.destination()).fieldName())) {
					field.setAccessible(true);
					newMap.put(key, field.get(value));

					field.setAccessible(false);
				    }
				}
			    } catch (SecurityException e) {
				e.printStackTrace();
			    } catch (IllegalArgumentException e) {
				e.printStackTrace();
			    } catch (IllegalAccessException e) {
				e.printStackTrace();
			    }

			}

		    }
		}
	    } else {
		newMap.put(key, value);

	    }

	}

	object = newMap;
	return newMap;
    }

    public T find(Map<String, Object> map, QueryStorage storage)
	    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
	var clazz = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	T entity = null;
	if (storage.isVisited((Class<?>) clazz, map)) {
	    return storage.getEntity((Class<?>) clazz, map, entity);
	}
	String sql = SQLWriter.find((Map<String, Object>) map,
		DatabaseStorage.getInstance().getTable((Class<?>) clazz));
	var statement = Connector.getInstance().getInstance().sendResultRequest(sql);
	statement.executeQuery(sql);
	var r = statement.getResultSet();
	entity = populateEntity(r, statement, sql, map, storage);
	statement.close();
	return entity;
    }

    @Override
    public List<T> findAll() throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
	List<T> entities = new ArrayList<>();
	String sql = SQLWriter.findAll(getTable());
	Statement statement = Connector.getInstance().sendResultRequest(sql);
	statement.executeQuery(sql);
	ResultSet result = statement.getResultSet();
	QueryStorage storage = new QueryStorage();
	T entity = instantiateEntity();
	Map<String, Object> map = null;
	map = generateId(entity);
	while (result.next()) {
	    if (map.keySet().size() == 1) {
		for (String key : map.keySet()) {
		    entity = findById((Id) result.getObject(key), storage);
		}
	    } else
		for (String key : map.keySet()) {
		    map.put(key, result.getObject(key));
		}
	    if (entity == null) {
		entity = findById((Id) map, storage);
	    }
	    if (entity != null) {
		entities.add(entity);
		entity = null;
	    }
	}
	statement.close();
	return entities;

    }

    @Override
    public T update(T entity) throws SQLException, RepositoryNotExistsException {
	Map<String, Object> values = new HashMap<>();

	try {
	    values = JPAUtils.getValues(entity);
	} catch (IllegalAccessException | IllegalArgumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	String sql = SQLWriter.update(getTable(), values);
	Connector.getInstance().sendRequest(sql);
	try {
	    saveChildren(entity);
	} catch (SQLException e) {
	}
	return entity;
    }

    private void saveChildren(T entity) throws SQLException, RepositoryNotExistsException {
	Map<String, Object> values = new HashMap<>();
	Map<String, Object> ids = new HashMap<>();
	try {
	    values = JPAUtils.getValues(entity);
	    ids = generateId(entity);
	} catch (IllegalAccessException | IllegalArgumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	for (Relation relation : getTable().getAllRelations()) {
	    for (String key : values.keySet()) {
		if (relation.type().equals(OneToMany.class.getSimpleName()) && relation.origin().equals(key)) {
		    Object v = values.get(key);
		    if (!((List) v).isEmpty()) {
			var repository = DatabaseStorage.getInstance().getRepository(((List) v).get(0).getClass());
			repository.saveAll((List) v);

		    }
		}
	    }
	}

    }

    public List<String> deleteSQL(T entity) throws SQLException, RepositoryNotExistsException {
	DBTable table = getTable();
	var relationMap = table.mapRelations();
	List<String> statements = new ArrayList<>();
	List<String> fatherStatements = new ArrayList<>();
	Map<String, Object> ids = null;
	Map<String, Object> values = null;
	try {
	    ids = generateId(entity);
	    values = JPAUtils.getValues(entity);
	    Map<String, Object> map = new HashMap<>();
	    for (String key : values.keySet()) {
		for (RecordInfo r : table.getAllRecords()) {
		    if (key.equals(r.fieldName())) {
			Object value = values.get(key);
			if (Asserttions.isPrimitive(value)) {
			    map.put(r.name(), value);
			} else if (!Asserttions.isList(value)) {
			    Repository repository = DatabaseStorage.getInstance().getRepository(value.getClass());
			    if (repository != null) {
				var temp = repository.generateId(value);
				Relation relation = table.getRelation(r.name());
				map.put(r.name(), temp.get(relation.destination()));

			    }
			}

		    }
		}
	    }
	    if (map.isEmpty()) {
		map = ids;
	    }
	    String sql = SQLWriter.find(map, table);
	    Statement statement = Connector.getInstance().sendResultRequest(sql);
	    statement.execute(sql);
	    var result = statement.getResultSet();
	    for (String key : ids.keySet()) {
		if (result.next()) {
		    var optional = table.getAllRecords().stream().filter(r -> {
			return r.name().equals(key);
		    }).findFirst();
		    if (optional.isPresent()) {
			Object value = result.getObject(key, optional.get().data().data().parseToJava());
			ids.put(key, value);
		    }
		}
	    }
	} catch (NoSuchFieldException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	for (String tableName : relationMap.keySet()) {
	    List<Relation> relations = relationMap.get(tableName);
	    for (Relation relation : relations) {
		if (relation.cascade())
		    for (String valueName : values.keySet()) {
			if (relation.origin().equals(valueName)) {
			    Object value = values.get(valueName);
			    if (Asserttions.isList(value)) {
				List list = (List) value;
				if (!list.isEmpty()) {
				    if (relation.type().equals(ManyToMany.class.getSimpleName())) {
					DBTable related = DatabaseStorage.getInstance().getTable(tableName);
					DBTable relationTable = DatabaseStorage.getInstance().getRelationTable(table,
						related);
					Map<String, Object> where = new HashMap<String, Object>();
					for (Relation id : relationTable.relations()) {
					    for (String idKey : ids.keySet()) {
						if (id.destination().equals(idKey)
							&& table.name().equals(id.destinationTable())) {
						    where.put(id.origin(), ids.get(idKey));
						}
					    }

					}
					String sql = SQLWriter.delete(relationTable, where);
					if (sql != null)
					    statements.add(sql);
				    }
				}
			    } else if (relation.type().equals("inheritance")) {
				fatherStatements = DatabaseStorage.getInstance()
					.getRepository(DatabaseStorage.getInstance().getTable(tableName).clazz())
					.deleteSQL(entity);
			    }
			}

		    }
	    }

	}
	statements.add(SQLWriter.delete(getTable(), ids));
	statements.addAll(fatherStatements);
	return statements;
    }

    @Override
    public void delete(T entity) throws SQLException, RepositoryNotExistsException, RelationShipNotExistsException {
	List<String> statements = deleteSQL(entity);

	try {

	    for (String statement : statements) {
		Connector.getInstance().sendRequest(statement);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    try {
		save(entity);
	    } catch (DataTypeException e1) {
	    } catch (SQLException e1) {
		update(entity);
	    }
	}
    }

    public Map<String, Object> generateId(T entity) throws RepositoryNotExistsException {
	Map<String, Object> map = new HashMap<String, Object>();
	DBTable table = DatabaseStorage.getInstance().getTable(entity.getClass());
	Collection<Field> fields = new ArrayList<>();
	Collections.addAll(fields, entity.getClass().getDeclaredFields());
	for (RecordInfo recor : table.id()) {
	    for (Relation relation : table.relations()) {
		if (recor.name().equals(relation.origin())) {
		    if (relation.type().equals("inheritance")) {

			DBTable relatedTable = DatabaseStorage.getInstance().getTable(relation.destinationTable());
			var relatedTableFields = relatedTable.clazz().getDeclaredFields();
			Collections.addAll(fields, relatedTableFields);
		    }
		}
		try {
		    Object value;
		    var optional = fields.stream().filter(f -> {
			return f.getName().equals(recor.fieldName());
		    }).findFirst();

		    if (optional.isPresent()) {
			var field = optional.get();
			field.setAccessible(true);
			value = field.get(entity);
			field.setAccessible(false);

			if (value != null && !Asserttions.isPrimitive(value)) {
			    Map valuesMap = null;
			    try {
				valuesMap = DatabaseStorage.getInstance().getRepository(
					DatabaseStorage.getInstance().getTable(relation.destinationTable()).clazz())
					.generateId(value);
			    } catch (NoSuchFieldException | RepositoryNotExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			    if (valuesMap == null)
				valuesMap = new HashMap<String, Object>();
			    valuesMap.values().stream().forEach(v -> {
				map.put(recor.name(), v);
			    });
			}
		    } else {
			value = null;
		    }
		    if (value == null || Asserttions.isPrimitive(value))
			map.put(recor.name(), value);
		} catch (SecurityException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IllegalArgumentException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

	    }
	    if (table.relations().isEmpty()) {
		Object value = null;
		var optional = fields.stream().filter(f -> {
		    return f.getName().equals(recor.fieldName());
		}).findFirst();

		if (optional.isPresent()) {
		    var field = optional.get();
		    field.setAccessible(true);
		    try {
			value = field.get(entity);
		    } catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    field.setAccessible(false);
		    map.put(recor.name(), value);
		}
	    }
	}

	return map;
    }

    private T populateEntity(ResultSet result, Statement originalStatement, String originalSQL, Map<String, Object> ids,
	    QueryStorage storage) throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
	T entity = instantiateEntity();
	DBTable table = getTable();
	var realIds = generateId(entity);
	boolean saved = false;
	/*
	if (realIds.keySet().equals(ids.keySet())) {
	    storage.addEntity(table.clazz(), ids, entity);
	    saved = true;

	}*/
	// ResultSet result = executeQuery(originalStatement, originalSQL);
	List<Object> fatherIds = processResultSet(result, entity, table, storage);
	storage.addVisited(table.clazz(), ids);
	// originalStatement.getConnection().close();

	handleUnsavedRecords(entity, table, ids, storage);

	if (hasSuperclass(table)) {
	    populateSuperclassFields(entity, table, fatherIds);
	}

	return entity;
    }

    private T instantiateEntity() {
	ObjenesisStd objenesis = new ObjenesisStd();
	var clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	ObjectInstantiator<T> instantiator = objenesis.getInstantiatorOf(clazz);
	return instantiator.newInstance();
    }

    private DBTable getTable() {
	var clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	return DatabaseStorage.getInstance().getTable(clazz);
    }

    private ResultSet executeQuery(Statement statement, String sql) throws SQLException {
	return statement.executeQuery(sql);
    }

    private List<Object> processResultSet(ResultSet result, T entity, DBTable table, QueryStorage storage)
	    throws SQLException, RepositoryNotExistsException {
	List<Object> fatherIds = new ArrayList<>();
	for (RecordInfo record : getSavedRecords(table)) {
	    try {

		Object value = result.getObject(record.name(), record.data().data().parseToJava());
		handleInheritance(table, fatherIds, record, value);
		setFieldValue(entity, record, value, storage);
		var ts = generateId(entity);
		List<Boolean> validity = new ArrayList<>();
		ts.forEach((k, v) -> {
		    RecordInfo r = table.getRecord(k);
		    if (k.equals(r.name()) && v != null) {
			validity.add(true);
		    } else {
			validity.add(false);
		    }
		});
		if(!validity.contains(false)) {
		    storage.addVisited(entity.getClass(), ts);
		    storage.addEntity(entity.getClass(), ts, entity);
		}
		
	    } catch (SQLException e) {
		result.next();
		fatherIds = processResultSet(result, entity, table, storage);
	    }
	}

	return fatherIds;
    }

    private List<RecordInfo> getSavedRecords(DBTable table) {
	return table.records().stream().filter(RecordInfo::saved).toList();
    }

    private void handleInheritance(DBTable table, List<Object> fatherIds, RecordInfo record, Object value) {
	if (isInheritanceRecord(table, record)) {
	    fatherIds.add(value);
	}
    }

    private boolean isInheritanceRecord(DBTable table, RecordInfo record) {
	return table.relations().stream()
		.anyMatch(r -> r.origin().equals(record.name()) && r.type().equals("inheritance"));
    }

    private void setFieldValue(T entity, RecordInfo record, Object value, QueryStorage storage)
	    throws RepositoryNotExistsException {
	for (Field field : entity.getClass().getDeclaredFields()) {
	    if (field.getName().equals(record.fieldName())) {
		field.setAccessible(true);
		Relation relation = DatabaseStorage.getInstance().getTable(entity.getClass())
			.getRelation(record.name());
		try {
		    if (relation != null && (relation.type().equals(OneToOne.class.getSimpleName())
			    || relation.type().equals(ManyToOne.class.getSimpleName()))) {
			Map<String, Object> map = new HashMap<>();
			map.put(relation.destination(), value);
			var repo = DatabaseStorage.getInstance().getRepository(
				DatabaseStorage.getInstance().getTable(relation.destinationTable()).clazz());
			;
			value = repo.findById(map, storage);

		    }
		    field.set(entity, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
		    e.printStackTrace();
		} catch (SQLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (RelationShipNotExistsException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} finally {
		    field.setAccessible(false);
		}
	    }
	}
    }

    private void handleUnsavedRecords(T entity, DBTable table, Map<String, Object> ids, QueryStorage storage)
	    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
	for (RecordInfo record : getUnsavedRecords(table)) {
	    if (record.data().data().equals(DataType.LIST)) {
		handleManyToManyRelation(entity, table, ids, storage, record);
	    }
	}
    }

    private List<RecordInfo> getUnsavedRecords(DBTable table) {
	return table.records().stream().filter(r -> !r.saved()).toList();
    }

    private void handleManyToManyRelation(T entity, DBTable table, Map<String, Object> ids, QueryStorage storage,
	    RecordInfo record) throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
	Relation relation = table.getRelation(record.name());
	if (relation == null) {
	    throw new RelationShipNotExistsException(
		    String.format("The field %s of the class %s must define the relation annotation",
			    record.fieldName(), table.clazz().getSimpleName()));
	}

	DBTable relatedTable = DatabaseStorage.getInstance().getTable(relation.destinationTable());
	List<Relation> relations = getRelationsToTable(relatedTable, table);

	for (Relation r : relations) {
	    if (r.type().equals(ManyToMany.class.getSimpleName())) {
		handleManyToMany(entity, table, ids, storage, relatedTable, r);
	    }
	    if (r.type().equals(ManyToOne.class.getSimpleName())) {
		Map<String, Object> temp = new HashMap<String, Object>();
		ids.forEach(((k, v) -> {
		    if (k.equals(r.destination())) {
			temp.put(r.origin(), v);
		    }
		}));
		List relatedEntities = (List) DatabaseStorage.getInstance().getRepository(relatedTable.clazz())
			.findBy(temp, storage);
		boolean valid = false;
		for (Field field : getAllFields(entity.getClass())) {
		    for (Relation rel : table.getAllRelations()) {
			RecordInfo recor = table.getRecord(rel.origin());
			if (recor.fieldName().equals(field.getName())) {
			    if (field.getGenericType() instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
				Type[] fieldArgTypes = parameterizedType.getActualTypeArguments();
				if (!relatedEntities.isEmpty())
				    valid = fieldArgTypes[0].equals(relatedEntities.get(0).getClass());
			    }

			    field.setAccessible(true);
			    if (valid) {
				try {
				    var list = field.get(entity);
				    if (list == null) {
					list = new ArrayList<>();
				    }
				    ((List) list).addAll(relatedEntities);
				    field.set(entity, list);

				} catch (IllegalArgumentException | IllegalAccessException e) {
				}

			    } else {
				Object list = null;
				try {
				    list = field.get(entity);
				    if (list == null) {
					list = new ArrayList<>();
				    }
				    field.set(entity, list);
				} catch (IllegalArgumentException | IllegalAccessException e) {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
			    }
			    field.setAccessible(false);
			}
		    }
		}
	    }
	    if (r.type().equals(OneToMany.class.getSimpleName())) {
		Map<String, Object> temp = new HashMap<String, Object>();
		ids.forEach(((k, v) -> {
		    if (k.equals(r.destination())) {
			temp.put(r.origin(), v);
		    }
		}));
		var relatedEntity = DatabaseStorage.getInstance().getRepository(relatedTable.clazz()).find(temp,
			storage);
		for (Field field : getAllFields(relatedEntity.getClass())) {
		    RecordInfo recor = relatedTable.getRecord(r.origin());
		    if (field.getName().equals(recor.name())) {
			field.setAccessible(true);
			try {
			    var list = field.get(relatedEntity);
			    if (list == null) {
				list = new ArrayList<>();
			    }
			    ((List) list).add(entity);
			    field.set(entity, list);

			} catch (IllegalArgumentException | IllegalAccessException e) {
			}

			field.setAccessible(false);
		    }
		}
	    }
	}
    }

    private List<Relation> getRelationsToTable(DBTable relatedTable, DBTable table) {
	return relatedTable.relations().stream().filter(r -> r.destinationTable().equals(table.name())).toList();
    }

    private void handleManyToMany(T entity, DBTable table, Map<String, Object> ids, QueryStorage storage,
	    DBTable relatedTable, Relation relation) throws SQLException, RepositoryNotExistsException {
	DBTable relationTable = DatabaseStorage.getInstance().getRelationTable(table, relatedTable);
	Map<String, Object> filter = createFilter(ids, entity, relationTable, table);
	String sql = SQLWriter.find(filter, relationTable);
	var statement = Connector.getInstance().sendResultRequest(sql);
	ResultSet results = statement.executeQuery(sql);
	List<Object> elements = getRelatedElements(results, relationTable, relatedTable, storage);

	setListField(entity, table, elements);
    }

    private Map<String, Object> createFilter(Map<String, Object> ids, T entity, DBTable relationTable, DBTable table) {
	Map<String, Object> filter = new HashMap<>();
	Map<String, Object> filter2 = new HashMap<>();
	for (String key : ids.keySet()) {
	    for (Relation rel : relationTable.relations()) {
		DBTable destination = DatabaseStorage.getInstance().getTable(rel.destinationTable());
		if (destination.clazz().equals(table.clazz())) {
		    Object value = ids.get(key);
		    if (key != rel.destination()) {
			for (Field field : getAllFields(entity.getClass())) {
			    for (RecordInfo r : table.id()) {
				if (field.getName().equals(r.fieldName())) {

				    field.setAccessible(true);
				    try {
					value = field.get(entity);
				    } catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				    }
				    field.setAccessible(false);
				}
			    }
			}

		    }

		    filter2.put(rel.origin(), value);
		}
	    }
	}
	return filter2;
    }

    private List<Object> getRelatedElements(ResultSet results, DBTable relationTable, DBTable relatedTable,
	    QueryStorage storage) throws SQLException, RepositoryNotExistsException {
	List<Object> elements = new ArrayList<>();
	List<Map<String, Object>> relationTableIds = extractRelationTableIds(results, relationTable, relatedTable);
	for (var relatedIds : relationTableIds) {
	    Object e = null;
	    try {
		e = getRelatedEntity(storage, relatedTable, relatedIds);
	    } catch (SQLException r) {
		// TODO Auto-generated catch block
		r.printStackTrace();
	    } catch (RelationShipNotExistsException r) {
		// TODO Auto-generated catch block
		r.printStackTrace();
	    }
	    elements.add(e);
	}
	return elements;
    }

    private List<Map<String, Object>> extractRelationTableIds(ResultSet results, DBTable relationTable,
	    DBTable relatedTable) throws SQLException {
	List<Map<String, Object>> relationTableIds = new ArrayList<>();
	while (results.next()) {
	    Map<String, Object> relatedIds = new HashMap<>();
	    for(RecordInfo record : relatedTable.id())
	    for (String name : getRecordNames(relationTable)) {
		Relation rel = relationTable.getRelation(name);
		if (rel.destinationTable().equals(relatedTable.name()) && rel.destination().equals(record.name())) {
		    RecordInfo recor = relationTable.getRecord(name);
		    Object value = results.getObject(recor.name(), recor.data().data().parseToJava());
		    relatedIds.put(rel.destination(), value);
		}
	    }
	    relationTableIds.add(relatedIds);
	}
	return relationTableIds;
    }

    private List<String> getRecordNames(DBTable relationTable) {
	List<String> recordNames = new ArrayList<>();
	for (Relation rel : relationTable.relations()) {
	    recordNames.add(rel.origin());
	}
	return recordNames;
    }

    private Object getRelatedEntity(QueryStorage storage, DBTable relatedTable, Map<String, Object> relatedIds)
	    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
	if (storage.isVisited(relatedTable.clazz(), relatedIds)) {
	    T temp = null;
	    return storage.getEntity(relatedTable.clazz(), relatedIds, temp);
	} else {
	    return DatabaseStorage.getInstance().getRepository(relatedTable.clazz()).find(relatedIds, storage);
	}
    }

    private void setListField(T entity, DBTable table, List<Object> elements) throws RepositoryNotExistsException {
	for (Field field : getAllFields(table.clazz())) {
	    for (RecordInfo record : table.records()) {
		if (field.getName().equals(record.fieldName()) && record.data().data().equals(DataType.LIST)) {
		    field.setAccessible(true);
		    try {
			field.set(entity, removeDuplicates(elements));
		    } catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		    } finally {
			field.setAccessible(false);
		    }
		}
	    }
	}
    }

    private List<Object> removeDuplicates(List<Object> list)
	    throws IllegalAccessException, RepositoryNotExistsException {
	HashSet<String> seen = new HashSet<>();
	Iterator<Object> iterator = list.iterator();
	Repository repository = null;
	if (!list.isEmpty()) {
	    int index = -1;
	    for(int i = 0; i < list.size(); i ++) {
		if(list.get(i) != null) {
		    index = i;
		    break;
		}
	    }
	    if(index != -1)
	    repository = DatabaseStorage.getInstance().getRepository(list.get(index).getClass());
	}
	List<Object> result = new ArrayList<>();
	Map<String, Object> map = null;
	while (iterator.hasNext()) {
	    Object item = iterator.next();
	    if(item != null) {
	    try {
		if (seen.add(repository.generateId(item).toString())) {
		    result.add(item);
		    iterator.remove();
		}
	    } catch (NoSuchFieldException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (RepositoryNotExistsException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}else {
	    iterator.remove();
	}}
	return result;
    }

    private String getObjectFieldsString(Object obj) throws IllegalAccessException {
	StringBuilder sb = new StringBuilder();
	Class<?> objClass = obj.getClass();

	for (Field field : getAllFields(objClass)) {
	    field.setAccessible(true);
	    Object value = field.get(obj);
	    sb.append(value != null ? value.toString() : "null").append(",");
	}

	return sb.toString();
    }

    private List<Field> getAllFields(Class<?> clazz) {
	List<Field> fields = new ArrayList<>();
	if (!clazz.getSuperclass().equals(Object.class)) {
	    Collections.addAll(fields, clazz.getSuperclass().getDeclaredFields());
	}
	Collections.addAll(fields, clazz.getDeclaredFields());
	return fields;
    }

    private boolean hasSuperclass(DBTable table) {
	return !table.clazz().getSuperclass().equals(Object.class);
    }

    private void populateSuperclassFields(T entity, DBTable table, List<Object> fatherIds)
	    throws RepositoryNotExistsException {
	try {
	    Object father = getSuperclassEntity(table, fatherIds);
	    for (Field field : father.getClass().getDeclaredFields()) {
		field.setAccessible(true);
		field.set(entity, field.get(father));
		field.setAccessible(false);
	    }
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    e.printStackTrace();
	}
    }

    private Object getSuperclassEntity(DBTable table, List<Object> fatherIds) throws RepositoryNotExistsException {
	Class<?> fatherClass = table.clazz().getSuperclass();
	var repo = DatabaseStorage.getInstance().getRepository(fatherClass);
	try {
	    return repo.findById(fatherIds.get(0));
	} catch (SQLException | RelationShipNotExistsException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return null;
    }

}
