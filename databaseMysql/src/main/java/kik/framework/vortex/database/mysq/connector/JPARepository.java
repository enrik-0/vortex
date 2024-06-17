package kik.framework.vortex.database.mysq.connector;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public T save(T entity) throws SQLException, DataTypeException {
	var clazz = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	Map<String, Object> values = null;
	Map<String, Object> id = null;
	boolean search = true;
	if (entity != null) {
	    entity.getClass();
	}
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
				System.out.println();
				values.put(relation.origin(), r.get(relation.destination()));
			    }
			} else if (relation.origin().equals(key)) {
			    if (Asserttions.isList(value)) {
				for (Object object : (List) value) {
				    map = temp(relation, object, id);
				    System.err.println();
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
	return entity;
    }

    private Map<String, Object> temp(Relation relation, Object object, Map<String, Object> ids)
	    throws DataTypeException, SQLException {
	Map<String, Object> map = new HashMap<String, Object>();
	DBTable relatedTable = DatabaseStorage.getInstance().getTable(relation.destinationTable());
	Map<String, Object> objectIds = null;
	Repository repository = null;
	if (relation.type().equals(ManyToMany.class.getSimpleName())) {
	    try {
		objectIds = ((JPARepository) DatabaseStorage.getInstance().getRepository(relatedTable.clazz()))
			.generateId(object);
	    } catch (NoSuchFieldException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
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
	    System.err.println();
	    String sql = SQLWriter.insert(relationTable, map);
	    try {
		Connector.getInstance().sendRequest(sql);

	    } catch (SQLIntegrityConstraintViolationException e) {
	    }
	    System.err.println();

	} else {
	    repository = (JPARepository) DatabaseStorage.getInstance().getRepository(relatedTable.clazz());
	}

	try {
	    if (repository != null && !Asserttions.isList(object)) {

		map = ((JPARepository) repository).generateId(object);
	    }
	} catch (NoSuchFieldException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
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
	if (entities != null && !entities.isEmpty()) {
	    List<Map<String, Object>> values = new ArrayList<>();
	    entities.stream().forEach(e -> {
		try {
		    save(e);
		} catch (SQLException | DataTypeException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
	    });
	}
	return entities;

    }

    @Override
    public T findById(Id id) throws SQLException, RelationShipNotExistsException {
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

    public T findById(Id id, QueryStorage storage) throws SQLException, RelationShipNotExistsException {
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

    private T findById(Map<String, Object> id, DBTable table) throws SQLException, RelationShipNotExistsException {
	return findById(id, table, new QueryStorage());

    }

    private T findById(Map<String, Object> id, DBTable table, QueryStorage storage)
	    throws SQLException, RelationShipNotExistsException {
	String sql = SQLWriter.find(id, table);
	T entity = instantiateEntity();
	entity = storage.getEntity(getTable().clazz(), id, entity);
	if(entity != null) {
	    return entity;
	}
	var statement = Connector.getInstance().getConnection().createStatement();
	var result = statement.executeQuery(sql);
	boolean response = result.next();
	if (response)
	    entity = populateEntitya(result, statement, sql, id, storage);
	statement.close();
	return entity;

    }

    @Override
    public Collection<T> findBy(Object object) throws SQLException, RelationShipNotExistsException {
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
	    T entity = populateEntitya(result, statement, sql, (Map<String, Object>) object, storage);
	    entities.add(entity);
	}
	statement.close();
	return entities;
    }

    @Override
    public Collection<T> findBy(Object object, QueryStorage storage)
	    throws SQLException, RelationShipNotExistsException {
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
	    T entity = populateEntitya(result, statement, sql, (Map<String, Object>) object, storage);
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
	    // value = 2;
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
					System.out.println();
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
			System.out.println(f);

		    }
		}
	    } else {
		newMap.put(key, value);

	    }

	}

	object = newMap;
	return newMap;
    }

    public T find(Map<String, Object> map, QueryStorage storage) throws SQLException, RelationShipNotExistsException {
	var clazz = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	String sql = SQLWriter.find((Map<String, Object>) map,
		DatabaseStorage.getInstance().getTable((Class<?>) clazz));
	var statement = Connector.getInstance().getInstance().sendResultRequest(sql);
	statement.executeQuery(sql);
	var r = statement.getResultSet();
	T entity = populateEntitya(r, statement, sql, map, storage);
	statement.close();
	return entity;
    }

    @Override
    public List<T> findAll() throws SQLException, RelationShipNotExistsException {
	List<T> entities = new ArrayList<>();
	String sql = SQLWriter.findAll(getTable());
	Statement statement = Connector.getInstance().sendResultRequest(sql);
	statement.executeQuery(sql);
	ResultSet result = statement.getResultSet();
	QueryStorage storage = new QueryStorage();
	T entity = instantiateEntity();
	Map<String, Object> map = null;
	try {
	    map = generateId(entity);
	} catch (NoSuchFieldException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	System.out.println();
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
    public T update(T entity) throws SQLException {
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

    private void saveChildren(T entity) throws SQLException {
	Map<String, Object> values = new HashMap<>();
	Map<String, Object> ids = new HashMap<>();
	try {
	    values = JPAUtils.getValues(entity);
	    ids = generateId(entity);
	} catch (IllegalAccessException | IllegalArgumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (NoSuchFieldException e) {
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

    public List<String> deleteSQL(T entity) throws SQLException {
	DBTable table = getTable();
	var relationMap = table.mapRelations();
	List<String> statements = new ArrayList<>();
	List<String> fatherStatements = new ArrayList<>();
	Map<String, Object> ids = null;
	Map<String, Object> values = null;
	try {
	    ids = generateId(entity);
	    values = JPAUtils.getValues(entity);
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
    public void delete(T entity) throws SQLException {
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
		System.out.println("JPARepository.delete()");
	    } catch (SQLException e1) {
		update(entity);
	    }
	}
    }

    public Map<String, Object> generateId(T entity) throws NoSuchFieldException {
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
			    var valuesMap = DatabaseStorage.getInstance()
				    .getRepository(
					    DatabaseStorage.getInstance().getTable(relation.destinationTable()).clazz())
				    .generateId(value);
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
	    // cosas
	}

	return map;
    }

    private T populateEntity(Statement originalStatement, String originalSQL, Map<String, Object> ids,
	    QueryStorage storage) throws SQLException, RelationShipNotExistsException {
	T entity = instantiateEntity();
	var table = getTable();
	ResultSet result = originalStatement.executeQuery(originalSQL);
	storage.addEntity(table.clazz(), ids, entity);
	List<Object> fatherIds = new ArrayList<>();
	result.next();
	for (RecordInfo recor : table.records().stream().filter(RecordInfo::saved).toList()) {

	    Object value = result.getObject(recor.name(), recor.data().data().parseToJava());
	    if (table.relations().stream().filter(r -> {
		return r.origin().equals(recor.name()) && r.type().equals("inheritance");
	    }).count() > 0) {
		fatherIds.add(value);
	    }
	    var fields = entity.getClass().getDeclaredFields();
	    int i = 0;
	    for (Field field : fields) {
		if (field.getName().equals(recor.fieldName())) {
		    field.setAccessible(true);
		    try {
			field.set(entity, value);
		    } catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    field.setAccessible(false);
		}
	    }
	}
	originalStatement.getConnection().close();
	storage.addVisited(table.clazz(), ids);
	for (RecordInfo recor : table.records().stream().filter(r -> !r.saved()).toList()) {
	    if (recor.data().data().equals(DataType.LIST)) {
		Relation relation = table.getRelation(recor.name());
		if (relation == null)
		    throw new RelationShipNotExistsException(
			    String.format("The field %s of the class %s must define the relation annotation",
				    recor.fieldName(), table.clazz().getSimpleName()));

		DBTable relatedTable = DatabaseStorage.getInstance().getTable(relation.destinationTable());
		List<Relation> relations = relatedTable.relations().stream().filter(r -> {
		    return r.destinationTable().equals(table.name());
		}).toList();
		for (Relation r : relations) {
		    if (r.type().equals(ManyToMany.class.getSimpleName())) {
			DBTable relationTable = DatabaseStorage.getInstance().getRelationTable(table, relatedTable);
			Map<String, Object> filter = new HashMap<String, Object>();
			List<String> recorNames = new ArrayList<>();
			for (String key : ids.keySet()) {
			    for (Relation rel : relationTable.relations()) {
				DBTable destination = DatabaseStorage.getInstance().getTable(rel.destinationTable());
				if (destination.clazz().equals(table.clazz())) {
				    filter.put(rel.origin(), ids.get(key));
				}
				recorNames.add(rel.origin());
			    }
			}
			String sql2 = SQLWriter.find(filter, relationTable);
			var statemnt = Connector.getInstance().sendResultRequest(sql2);
			ResultSet results = statemnt.executeQuery(sql2);
			List<Object> elements = new ArrayList<>();
			RecordInfo reco;
			List<Map<String, Object>> relationTableIds = new ArrayList<>();
			while (results.next()) {
			    for (String name : recorNames) {
				Relation rel = relationTable.getRelation(name);
				if (rel.destinationTable().equals(relatedTable.name())) {
				    reco = relationTable.getRecord(name);
				    Object o = results.getObject(reco.name(), reco.data().data().parseToJava());
				    Map<String, Object> relatedIds = new HashMap<>();
				    relatedIds.put(rel.destination(), o);
				    relationTableIds.add(relatedIds);
				}

			    }
			}
			statemnt.close();
			for (var relatedIds : relationTableIds) {

			    Object e;
			    if (storage.isVisited(relatedTable.clazz(), relatedIds)) {
				T temp = null;
				e = storage.getEntity(relatedTable.clazz(), relatedIds, temp);
			    } else {
				e = DatabaseStorage.getInstance().getRepository(relatedTable.clazz()).find(relatedIds,
					storage);
			    }
			    elements.add(e);
			}
			List<Field> fields = new ArrayList<>();
			if (!table.clazz().getSuperclass().equals(Object.class)) {
			    Collections.addAll(fields, table.clazz().getSuperclass().getDeclaredFields());
			}
			Collections.addAll(fields, table.clazz().getDeclaredFields());

			for (Field field : fields) {
			    for (RecordInfo rec : table.records()) {
				if (field.getName().equals(rec.fieldName())
					&& rec.data().data().equals(DataType.LIST)) {
				    field.setAccessible(true);
				    try {
					field.set(entity, elements);
				    } catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				    }
				    field.setAccessible(false);

				}

			    }
			}

			Map<String, Object> temp = new HashMap<>();
			Map<String, Object> map = new HashMap<>();
			try {
			    // Map<String, Object> temp;
			    temp = generateId(entity);
			    for (Relation rela : relatedTable.relations()) {
				for (String key : temp.keySet()) {
				    if (key.equals(rela.destination())) {
					map.put(rela.origin(), temp.get(key));
				    }
				}

			    }

			} catch (NoSuchFieldException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			System.err.println("");
		    }
		}

	    }
	}

	if (!table.clazz().getSuperclass().equals(Object.class)) {
	    Class<?> fatherClass = table.clazz().getSuperclass();
	    var repo = DatabaseStorage.getInstance().getRepository(fatherClass);
	    var father = repo.findById(fatherIds.get(0));
	    for (Field field : father.getClass().getDeclaredFields()) {
		field.setAccessible(true);
		try {
		    Object fatherValue = field.get(father);
		    field.set(entity, fatherValue);
		} catch (IllegalArgumentException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		field.setAccessible(false);

	    }
	}

	Map<String, Object> id = new HashMap<>();
	for (String key : id.keySet()) {
	}
	Map<String, Object> map = new HashMap<>();
	return entity;

    }

    private T populateEntitya(ResultSet result, Statement originalStatement, String originalSQL,
	    Map<String, Object> ids, QueryStorage storage) throws SQLException, RelationShipNotExistsException {
	T entity = instantiateEntity();
	DBTable table = getTable();
	storage.addEntity(table.clazz(), ids, entity);
	// ResultSet result = executeQuery(originalStatement, originalSQL);
	List<Object> fatherIds = processResultSet(result, entity, table, storage);
	// originalStatement.getConnection().close();
	storage.addVisited(table.clazz(), ids);

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
	    throws SQLException {
	List<Object> fatherIds = new ArrayList<>();
	for (RecordInfo record : getSavedRecords(table)) {
	    try {

		Object value = result.getObject(record.name(), record.data().data().parseToJava());
		handleInheritance(table, fatherIds, record, value);
		setFieldValue(entity, record, value, storage);
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

    private void setFieldValue(T entity, RecordInfo record, Object value, QueryStorage storage) {
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
	    throws SQLException, RelationShipNotExistsException {
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
	    RecordInfo record) throws SQLException, RelationShipNotExistsException {
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
		if (!relatedEntities.isEmpty())
		    /*
		    for (Field field : getAllFields(relatedEntities.get(0).getClass())) {
			RecordInfo recor = relatedTable.getRecord(r.origin());
			if (field.getName().equals(recor.name())) {
			    for (var relatedEntity : relatedEntities) {

				field.setAccessible(true);
				try {
				    field.set(relatedEntity, entity);

				} catch (IllegalArgumentException | IllegalAccessException e) {
				}

				field.setAccessible(false);
			    }
			}
		    }*/
		for (Field field : getAllFields(entity.getClass())) {
		    for (Relation rel : table.getAllRelations()) {
			RecordInfo recor = table.getRecord(rel.origin());
			if (recor.fieldName().equals(field.getName())) {
			    field.setAccessible(true);
			    try {
				var list = field.get(entity);
				if (list == null) {
				    list = new ArrayList<>();
				}
				((List) list).addAll(relatedEntities);
				field.set(entity, list);

			    } catch (IllegalArgumentException | IllegalAccessException e) {
			    }

			    field.setAccessible(false);
			}
		    }
		}
		System.out.println();
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
	    DBTable relatedTable, Relation relation) throws SQLException {
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
	    QueryStorage storage) throws SQLException {
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
	    for (String name : getRecordNames(relationTable)) {
		Relation rel = relationTable.getRelation(name);
		if (rel.destinationTable().equals(relatedTable.name())) {
		    RecordInfo record = relationTable.getRecord(name);
		    Object value = results.getObject(record.name(), record.data().data().parseToJava());
		    Map<String, Object> relatedIds = new HashMap<>();
		    relatedIds.put(rel.destination(), value);
		    relationTableIds.add(relatedIds);
		}
	    }
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
	    throws SQLException, RelationShipNotExistsException {
	if (storage.isVisited(relatedTable.clazz(), relatedIds)) {
	    T temp = null;
	    return storage.getEntity(relatedTable.clazz(), relatedIds, temp);
	} else {
	    return DatabaseStorage.getInstance().getRepository(relatedTable.clazz()).find(relatedIds, storage);
	}
    }

    private void setListField(T entity, DBTable table, List<Object> elements) {
	for (Field field : getAllFields(table.clazz())) {
	    for (RecordInfo record : table.records()) {
		if (field.getName().equals(record.fieldName()) && record.data().data().equals(DataType.LIST)) {
		    field.setAccessible(true);
		    try {
			field.set(entity, elements);
		    } catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		    } finally {
			field.setAccessible(false);
		    }
		}
	    }
	}
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

    private void populateSuperclassFields(T entity, DBTable table, List<Object> fatherIds) {
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

    private Object getSuperclassEntity(DBTable table, List<Object> fatherIds) {
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
