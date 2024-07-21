package kik.framework.vortex.databasemanager;

import static org.junit.jupiter.api.Assertions.*;
import static kik.framework.vortex.databasemanager.assests.DataType.IntType;
import static kik.framework.vortex.databasemanager.assests.DataType.StringType;

import kik.framework.vortex.databasemanager.storage.DBTable;
import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import kik.framework.vortex.databasemanager.storage.RecordInfo;
import kik.framework.vortex.databasemanager.storage.RecordParameters;
import kik.framework.vortex.databasemanager.utils.SQLWriter;
import kik.framework.vortex.databasemanager.assests.Person;
import kik.framework.vortex.databasemanager.exception.DataTypeException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class SQLWriterTest {

    private DBTable table;
    private RecordInfo recordInfo;

    @BeforeEach
    public void setUp() {
	recordInfo = new RecordInfo("id", "id", true, true, false, true, new RecordParameters(IntType, Integer.class));
	List<RecordInfo> records = new ArrayList<>();
	records.add(recordInfo);
	records.add(new RecordInfo("value", "id", true, true, false, true,
		new RecordParameters(StringType, Integer.class)));
	records.add(new RecordInfo("notSaved", "id", false, true, false, false,
		new RecordParameters(StringType, Integer.class)));
	table = new DBTable("TestTable", Person.class, true, records, new ArrayList<>());
	DatabaseStorage.getInstance().addTable(table);
    }

    @Test
    public void testInsert() throws DataTypeException {
	Map<String, Object> values = new HashMap<>();
	values.put("id", 1);

	String sql = SQLWriter.insert(table, values);
	assertEquals("insert into TestTable (id) values (1);", sql);
    }


    @Test
    public void testInsertWithNullValue() throws DataTypeException {
	recordInfo = new RecordInfo("id", "id", false, true, true, true, new RecordParameters(IntType, Integer.class));
	table = new DBTable("TestTable", Person.class, true, Arrays.asList(recordInfo), new ArrayList<>());

	Map<String, Object> values = new HashMap<>();
	values.put("id", null);

	String sql = SQLWriter.insert(table, values);
	assertEquals("insert into TestTable (id) values (null);", sql);
    }

    @Test
    public void testInsertWithString() throws DataTypeException {
	recordInfo = new RecordInfo("name", "name", false, true, true, true,
		new RecordParameters(StringType, String.class));
	table = new DBTable("TestTable", Person.class, true, Arrays.asList(recordInfo), new ArrayList<>());

	Map<String, Object> values = new HashMap<>();
	values.put("name", "test");

	String sql = SQLWriter.insert(table, values);
	assertEquals("insert into TestTable (name) values (\"test\");", sql);
    }

    @Test
    public void testInsertWithMultipleFields() throws DataTypeException {
	RecordInfo nameInfo = new RecordInfo("name", "name", false, true, true, true,
		new RecordParameters(StringType, String.class));
	table = new DBTable("TestTable", Person.class, true, Arrays.asList(recordInfo, nameInfo), new ArrayList<>());

	Map<String, Object> values = new HashMap<>();
	values.put("id", 1);
	values.put("name", "test");

	String sql = SQLWriter.insert(table, values);
	assertEquals("insert into TestTable (name,id) values (\"test\",1);", sql);
    }

    @Test
    public void testInsertWithNonNullableField() {
	recordInfo = new RecordInfo("id", "id", false, true, false, true, new RecordParameters(IntType, Integer.class));
	table = new DBTable("TestTable", Person.class, true, Arrays.asList(recordInfo), new ArrayList<>());

	Map<String, Object> values = new HashMap<>();
	values.put("id", null);

	assertThrows(DataTypeException.class, () -> {
	    SQLWriter.insert(table, values);
	});
    }

    @Test
    public void testInsertWithNullableIdentifier() {
	recordInfo = new RecordInfo("id", "id", true, true, false, true, new RecordParameters(IntType, Integer.class));
	table = new DBTable("TestTable", Person.class, true, Arrays.asList(recordInfo), new ArrayList<>());

	Map<String, Object> values = new HashMap<>();
	values.put("id", null);

	assertThrows(DataTypeException.class, () -> {
	    SQLWriter.insert(table, values);
	});
    }

    @Test
    public void testFindAll() {
	String sql = SQLWriter.findAll(table);
	assertEquals("select id,value from TestTable;", sql);
    }

    @Test
    public void testUpdate() {
	Map<String, Object> values = new HashMap<>();
	values.put("id", 1);
	values.put("value", "Manolo");

	String sql = SQLWriter.update(table, values);
	assertEquals("update TestTable set id = 1,value = \'Manolo\' where id = 1 and value = \'Manolo\'", sql);
    }

    @Test
    public void testUpdateWithMultipleFields() {
	RecordInfo nameInfo = new RecordInfo("name", "name", false, true, true, true,
		new RecordParameters(StringType, String.class));
	table = new DBTable("TestTable", Person.class, true, Arrays.asList(recordInfo, nameInfo), new ArrayList<>());

	Map<String, Object> values = new HashMap<>();
	values.put("id", 1);
	values.put("name", "updatedTest");

	String sql = SQLWriter.update(table, values);
	assertEquals("update TestTable set id = 1,name = \'updatedTest\' where id = 1", sql);
    }

    @Test
    public void testDelete() {
	Map<String, Object> where = new HashMap<>();
	where.put("id", 1);

	String sql = SQLWriter.delete(table, where);
	assertEquals("delete from TestTable where id = 1 ", sql);
    }

    @Test
    public void testDeleteWithMultipleConditions() {
	RecordInfo nameInfo = new RecordInfo("name", "name", false, true, true, true,
		new RecordParameters(StringType, String.class));
	table = new DBTable("TestTable", Person.class, true, Arrays.asList(recordInfo, nameInfo), new ArrayList<>());

	Map<String, Object> where = new HashMap<>();
	where.put("id", 1);
	where.put("name", "test");

	String sql = SQLWriter.delete(table, where);
	assertEquals("delete from TestTable where name = \'test\'  and id = 1 ", sql);
    }

    @Test
    public void testDeleteWithoutConditions() {
	Map<String, Object> where = new HashMap<>();

	String sql = SQLWriter.delete(table, where);
	assertNull(sql);
    }

    @Test
    public void testInsertAll() throws DataTypeException {
	RecordInfo nameInfo = new RecordInfo("name", "name", false, true, true, true,
		new RecordParameters(StringType, String.class));
	table = new DBTable("TestTable", Person.class, true, Arrays.asList(recordInfo, nameInfo), new ArrayList<>());

	Map<String, Object> values1 = new HashMap<>();
	values1.put("id", 1);
	values1.put("name", "test1");

	Map<String, Object> values2 = new HashMap<>();
	values2.put("id", 2);
	values2.put("name", "test2");

	List<Map<String, Object>> valuesList = Arrays.asList(values1, values2);

	String sql = SQLWriter.insertAll(table.clazz(), valuesList);

	assertEquals("insert into TestTable (name,id) values(\"test1\",1),(\"test2\",2);", sql);
    }

    @Test
    public void testFind() {
	Map<String, Object> where = new HashMap<>();

	where.put("id", 1);
	where.put("value", "Manuel");

	String sql = SQLWriter.find(where, table);
	assertEquals("select id as id,value as value from TestTable where id = 1 and value = \'Manuel\';", sql);
    }
}
