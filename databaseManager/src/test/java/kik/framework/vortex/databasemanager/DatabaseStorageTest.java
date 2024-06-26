package kik.framework.vortex.databasemanager;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.emory.mathcs.backport.java.util.Collections;
import kik.framework.vortex.databasemanager.annotation.ManyToMany;
import kik.framework.vortex.databasemanager.annotation.ManyToOne;
import kik.framework.vortex.databasemanager.annotation.OneToOne;
import kik.framework.vortex.databasemanager.annotation.Table;
import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.exception.RelationShipNotExistsException;
import kik.framework.vortex.databasemanager.exception.RepositoryNotExistsException;
import kik.framework.vortex.databasemanager.storage.DBTable;
import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import kik.framework.vortex.databasemanager.storage.QueryStorage;
import kik.framework.vortex.databasemanager.storage.Relation;
import vortex.annotate.manager.Storage;

class DatabaseStorageTest {

    private DatabaseStorage databaseStorage;

    @BeforeEach
    void setUp() {
	databaseStorage = DatabaseStorage.getInstance();
	clearTables();
    }

    private void clearTables() {
	for (DBTable table : DatabaseStorage.getInstance().getAllTables()
		.toArray(new DBTable[DatabaseStorage.getInstance().getAllTables().size()])) {
	    databaseStorage.removeTable(table);
	}

    }

    @Test
    void testSingletonInstance() {
	assertNotNull(databaseStorage);
	DatabaseStorage anotherInstance = DatabaseStorage.getInstance();
	assertSame(databaseStorage, anotherInstance, "Instances should be the same (singleton pattern).");
    }

    @Test
    void testGetTableByClass() {
	class TestTable {
	}

	DBTable table = databaseStorage.getTable(TestTable.class);
	assertNull(table, "The table should be null if not found");
	table = new DBTable("testtables", TestTable.class);
	databaseStorage.addTable(table);
	DBTable retrievedTable = databaseStorage.getTable(TestTable.class);
	assertNotNull(retrievedTable, "Table should not be null.");
	assertEquals("testtables", retrievedTable.name(), "Table name should match the annotation value.");
    }

    @Test
    void testGetTableByClassWithTable() {
	@Table("table")
	class TestTable {
	}

	DBTable table = databaseStorage.getTable(TestTable.class);
	assertNull(table, "The table should be null if not found");
	table = new DBTable("table", TestTable.class);
	databaseStorage.addTable(table);
	DBTable retrievedTable = databaseStorage.getTable(TestTable.class);
	assertNotNull(retrievedTable, "Table should not be null.");
	assertEquals("table", retrievedTable.name(), "Table name should match the annotation value.");
    }

    @Test
    void testGetTableByName() {
	DBTable table = new DBTable("test_table", null);
	databaseStorage.addTable(table);
	DBTable retrievedTable = databaseStorage.getTable("test_table");
	assertNotNull(retrievedTable, "Table should not be null.");
	assertEquals("test_table", retrievedTable.name(), "Table name should match the provided name.");
    }

    @Test
    void testGetTableByNameFromAnnotation() {
	@Table("testing")
	class Test {
	}
	DBTable table = new DBTable("testing", Test.class);
	databaseStorage.addTable(table);
	DBTable retrievedTable = databaseStorage.getTable("testing");
	assertNotNull(retrievedTable, "Table should not be null.");
	assertEquals("testing", retrievedTable.name(), "Table name should match the provided name.");
    }

    @Test
    void testGetRelationTableNoInHeritance() {
	class TempClass {
	}
	class Temp2Class {
	}
	DBTable table1 = new DBTable("table1", TempClass.class);
	DBTable table2 = new DBTable("table2", Temp2Class.class);
	databaseStorage.addTable(table1);
	databaseStorage.addTable(table2);
	DBTable relation = new DBTable("table2_table1", Relation.class);
	databaseStorage.addTable(relation);

	DBTable relationTable = databaseStorage.getRelationTable(table1, table2);
	assertNotNull(relationTable, "Relation table should not be null");
	assertSame(relation, relationTable);
    }

    @Test
    void testGetRelationTableFirstTableInheritance() {
	class Father {
	}
	class TempClass extends Father {
	}
	class Temp2Class {
	}
	DBTable father = new DBTable("fathers", Father.class);
	DBTable table1 = new DBTable("table1", TempClass.class);
	DBTable table2 = new DBTable("table2", Temp2Class.class);
	databaseStorage.addTable(table1);
	databaseStorage.addTable(father);
	databaseStorage.addTable(table2);
	DBTable relation = new DBTable("fathers_table2", Relation.class);
	databaseStorage.addTable(relation);

	DBTable relationTable = databaseStorage.getRelationTable(table1, table2);
	assertNotNull(relationTable, "Relation table should not be null");
	assertSame(relation, relationTable);
    }

    @Test
    void testGetRelationTableSecondTableInheritance() {
	class Father {
	}
	class TempClass {
	}
	class Temp2Class extends Father {
	}
	DBTable father = new DBTable("fathers", Father.class);
	DBTable table1 = new DBTable("tempclasss", TempClass.class);
	DBTable table2 = new DBTable("temp2classs", Temp2Class.class);
	databaseStorage.addTable(table1);
	databaseStorage.addTable(father);
	databaseStorage.addTable(table2);
	DBTable relation = new DBTable("tempclasss_fathers", Relation.class);
	databaseStorage.addTable(relation);

	DBTable relationTable = databaseStorage.getRelationTable(table1, table2);
	assertNotNull(relationTable, "Relation table should not be null");
	assertSame(relation, relationTable);
    }

    @Test
    void testGetRelationTableBothTableInheritance() {
	class Father {
	}
	class Father2 {
	}
	class TempClass extends Father {
	}
	class Temp2Class extends Father2 {
	}
	DBTable father = new DBTable("fathers", Father.class);
	DBTable father2 = new DBTable("father2s", Father.class);
	DBTable table1 = new DBTable("tempclasss", TempClass.class);
	DBTable table2 = new DBTable("temp2classs", Temp2Class.class);
	databaseStorage.addTable(table1);
	databaseStorage.addTable(father);
	databaseStorage.addTable(father2);
	databaseStorage.addTable(table2);
	DBTable relation = new DBTable("father2s_fathers", Relation.class);
	databaseStorage.addTable(relation);

	DBTable relationTable = databaseStorage.getRelationTable(table1, table2);
	assertNotNull(relationTable, "Relation table should not be null");
	assertSame(relation, relationTable);
    }

    @Test
    void testGetRelationTableNoRelation() {
	class TempClass {
	}
	class Temp2Class {
	}
	DBTable table1 = new DBTable("tempclasss", TempClass.class);
	DBTable table2 = new DBTable("temp2classs", Temp2Class.class);
	databaseStorage.addTable(table1);
	databaseStorage.addTable(table2);
	DBTable relationTable = databaseStorage.getRelationTable(table1, table2);
	assertNull(relationTable, "Relation table should not be null");

    }

    @Test
    void testGetRepositoryWithInheritance() throws RepositoryNotExistsException {
	class Father {
	}
	class Person extends Father {
	}

	class Person1 {
	}
	class TestRepositoryImp<T, ID> implements Repository<T, ID> {

	    @Override
	    public T save(T entity) throws SQLException, DataTypeException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public Collection<T> saveAll(List<T> entities) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public T findById(ID id) throws SQLException, RelationShipNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public T findById(ID id, QueryStorage storage) throws SQLException, RelationShipNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public T find(Map<String, Object> map, QueryStorage storage)
		    throws SQLException, RelationShipNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public Map<String, Object> generateId(T entity) throws NoSuchFieldException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public void delete(T entity) throws SQLException {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public T update(T entity) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public List<String> deleteSQL(T entity) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public List<T> findAll() throws SQLException, RelationShipNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public Collection<T> findBy(Object object, QueryStorage storage)
		    throws SQLException, RelationShipNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public T save(Object entity, QueryStorage storage) throws RepositoryNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public T saveInheriance(Object entity) throws SQLException, DataTypeException, RepositoryNotExistsException,
		    RelationShipNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public Collection<T> findBy(Map<String, Object> object)
		    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }
	}
	class FatherRepository extends TestRepositoryImp<Father, Person1> {
	}
	class ChildRepository extends TestRepositoryImp<Person, Person1> {
	}
	Storage.getInstance().addAnnotationType(vortex.annotate.components.Repository.class.getName());
	Storage.getInstance().getComponent(vortex.annotate.components.Repository.class).add(FatherRepository.class);
	Storage.getInstance().getComponent(vortex.annotate.components.Repository.class).add(ChildRepository.class);
	Repository repository = databaseStorage.getRepository(Person.class);
	assertNotNull(repository, "Repository should not be null");
	assertEquals(repository.getClass(), ChildRepository.class);

    }

    @Test
    void testGetRepository() throws RepositoryNotExistsException {
	class Person {
	}
	class Person1 {
	}
	class TestRepositoryImp<T, ID> implements Repository<T, ID> {

	    @Override
	    public T save(T entity) throws SQLException, DataTypeException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public Collection<T> saveAll(List<T> entities) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public T findById(ID id) throws SQLException, RelationShipNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public T findById(ID id, QueryStorage storage) throws SQLException, RelationShipNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public T find(Map<String, Object> map, QueryStorage storage)
		    throws SQLException, RelationShipNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public Map<String, Object> generateId(T entity) throws NoSuchFieldException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public void delete(T entity) throws SQLException {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public T update(T entity) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public List<String> deleteSQL(T entity) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public List<T> findAll() throws SQLException, RelationShipNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public Collection<T> findBy(Object object, QueryStorage storage)
		    throws SQLException, RelationShipNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public T save(Object entity, QueryStorage storage) throws RepositoryNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public T saveInheriance(Object entity) throws SQLException, DataTypeException, RepositoryNotExistsException,
		    RelationShipNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public Collection<T> findBy(Map<String, Object> object)
		    throws SQLException, RelationShipNotExistsException, RepositoryNotExistsException {
		// TODO Auto-generated method stub
		return null;
	    }
	}
	class TestRepository extends TestRepositoryImp<Person, Person1> {
	}
	Storage.getInstance().addAnnotationType(vortex.annotate.components.Repository.class.getName());
	Storage.getInstance().getComponent(vortex.annotate.components.Repository.class).add(TestRepository.class);
	var tClass = ((ParameterizedType) TestRepository.class.getGenericSuperclass()).getActualTypeArguments()[0];
	Repository repository = databaseStorage.getRepository(Person.class);
	assertNotNull(repository, "Repository should not be null");
    }

    @Test
    public void testFillRelationGraphManyToOne() {
	class Testing {
	}
	class Testing1 {
	}
	class Testing2 {
	}
	DBTable table1 = new DBTable("testings", Testing.class);
	DBTable table2 = new DBTable("testing1s", Testing1.class);
	DBTable table3 = new DBTable("testing2s", Testing2.class);
	Relation relation = new Relation("field", "testing1s", "field2", false, new ManyToOne() {
	    @Override
	    public Class<? extends Annotation> annotationType() {
		return ManyToOne.class;
	    }
	});
	table1.addRelation(relation);
	databaseStorage.addTable(table1);
	databaseStorage.addTable(table2);
	databaseStorage.addTable(table3);
	List<DBTable> expected = new ArrayList<>();
	expected.add(table3);
	expected.add(table2);
	expected.add(table1);

	databaseStorage.fillRelationGraph();
	var result = databaseStorage.orderRelations();
	assertNotNull(result);
	assertEquals(expected, result);
    }

    @Test
    public void testFillRelationGraphNoRelations() {
	class Testing {
	}
	class Testing1 {
	}
	class Testing2 {
	}
	DBTable table1 = new DBTable("testings", Testing.class);
	DBTable table2 = new DBTable("testing1s", Testing1.class);
	DBTable table3 = new DBTable("testing2s", Testing2.class);

	databaseStorage.addTable(table1);
	databaseStorage.addTable(table2);
	databaseStorage.addTable(table3);
	List<DBTable> expected = new ArrayList<>();
	expected.add(table3);
	expected.add(table2);
	expected.add(table1);

	databaseStorage.fillRelationGraph();
	var result = databaseStorage.orderRelations();
	assertNotNull(result);
	assertEquals(expected, result);
    }

    @Test
    public void testFillRelationGraphOneToOne() {
	class Testing {
	}
	class Testing1 {
	}
	class Testing2 {
	}
	DBTable table1 = new DBTable("testings", Testing.class);
	DBTable table2 = new DBTable("testing1s", Testing1.class);
	DBTable table3 = new DBTable("testing2s", Testing2.class);
	Relation relation = new Relation("field", "testing1s", "field2", false, new OneToOne() {
	    @Override
	    public Class<? extends Annotation> annotationType() {
		return OneToOne.class;
	    }

	    @Override
	    public boolean cascade() {
		return false;
	    }
	});
	table3.addRelation(relation);
	databaseStorage.addTable(table2);
	databaseStorage.addTable(table1);
	databaseStorage.addTable(table3);
	List<DBTable> expected = new ArrayList<>();
	expected.add(table3);
	expected.add(table1);
	expected.add(table2);

	databaseStorage.fillRelationGraph();
	var result = databaseStorage.orderRelations();
	assertNotNull(result);
	assertEquals(expected, result);
    }

    @Test
    public void testFillRelationGraphManyToManyNonCreated() {
	class Testing {
	}
	class Testing1 {
	}
	class Testing2 {
	}
	DBTable table1 = new DBTable("testings", Testing.class);
	DBTable table2 = new DBTable("testing1s", Testing1.class);
	DBTable table3 = new DBTable("testing2s", Testing2.class);
	Relation relation = new Relation("field", "testing1s", "field2", false, new ManyToMany() {
	    @Override
	    public Class<? extends Annotation> annotationType() {
		return ManyToMany.class;
	    }

	    @Override
	    public boolean cascade() {
		return false;
	    }
	});
	table3.addRelation(relation);
	databaseStorage.addTable(table1);
	databaseStorage.addTable(table2);
	databaseStorage.addTable(table3);
	List<DBTable> expected = new ArrayList<>();
	expected.add(table3);
	expected.add(table2);
	expected.add(table1);

	databaseStorage.fillRelationGraph();
	var result = databaseStorage.orderRelations();
	assertNotNull(result);
	assertEquals(expected, result);
    }

    @Test
    public void addTableThatAlreadyExists() {

	class Testing {
	}
	DBTable table1 = new DBTable("testings", Testing.class);
	databaseStorage.addTable(table1);
	assertEquals(1, databaseStorage.getAllTables().size());
	databaseStorage.addTable(table1);
	assertEquals(1, databaseStorage.getAllTables().size());

    }

    @Test
    public void testFillRelationGraphManyToManyCreated() {
	class Testing {
	}
	class Testing1 {
	}
	class Testing2 {
	}
	DBTable table1 = new DBTable("testings", Testing.class, true);
	DBTable table2 = new DBTable("testing1s", Testing1.class);
	DBTable table3 = new DBTable("testing2s", Testing2.class);
	Relation relation = new Relation("field", "testing1s", "field2", false, new ManyToMany() {
	    @Override
	    public Class<? extends Annotation> annotationType() {
		return ManyToMany.class;
	    }

	    @Override
	    public boolean cascade() {
		return false;
	    }
	});
	table1.addRelation(relation);
	databaseStorage.addTable(table1);
	databaseStorage.addTable(table2);
	databaseStorage.addTable(table3);
	List<DBTable> expected = new ArrayList<>();
	expected.add(table1);
	expected.add(table3);
	expected.add(table2);

	databaseStorage.fillRelationGraph();
	var result = databaseStorage.orderRelations();
	assertNotNull(result);
	assertEquals(expected, result);
    }

    @Test
    public void testFillRelationGraphAnyRelationCreated() {
	class Testing {
	}
	class Testing1 {
	}
	class Testing2 {
	}
	DBTable table1 = new DBTable("testings", Testing.class, true);
	DBTable table2 = new DBTable("testing1s", Testing1.class);
	DBTable table3 = new DBTable("testing2s", Testing2.class);
	Relation relation = new Relation("field", "testing1s", "field2", false, new OneToOne() {
	    @Override
	    public Class<? extends Annotation> annotationType() {
		return OneToOne.class;
	    }

	    @Override
	    public boolean cascade() {
		return false;
	    }
	});
	table1.addRelation(relation);
	databaseStorage.addTable(table1);
	databaseStorage.addTable(table2);
	databaseStorage.addTable(table3);
	List<DBTable> expected = new ArrayList<>();
	expected.add(table1);
	expected.add(table3);
	expected.add(table2);

	databaseStorage.fillRelationGraph();
	var result = databaseStorage.orderRelations();
	assertNotNull(result);
	assertEquals(expected, result);
    }

    @Test
    public void addMultipleTables() {
	class Testing {
	}
	;
	class Testing1 {
	}
	;
	class Testing2 {
	}
	;
	List<DBTable> tables = new ArrayList<>();
	tables.add(new DBTable("testing1s", Testing1.class));
	tables.add(new DBTable("testing2s", Testing2.class));
	tables.add(new DBTable("testings", Testing.class, true));
	databaseStorage.addTables(tables);
	assertEquals(3, databaseStorage.getAllTables().size());
    }
}
