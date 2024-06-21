package kik.framework.vortex.database.mysql;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.exception.RelationTypeException;
import kik.framework.vortex.databasemanager.storage.DBTable;
import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import kik.framework.vortex.databasemanager.storage.RecordInfo;
import kik.framework.vortex.databasemanager.storage.Relation;
import vortex.annotate.components.Entity;
import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.manager.AnnotationManager;
import vortex.annotate.manager.Storage;
import vortex.properties.filemanager.FileReader;
import kik.framework.vortex.database.mysq.connector.Connector;
import kik.framework.vortex.databasemanager.annotation.Column;
import kik.framework.vortex.databasemanager.annotation.ID;
import kik.framework.vortex.databasemanager.annotation.ManyToMany;
import kik.framework.vortex.databasemanager.annotation.ManyToOne;
import kik.framework.vortex.databasemanager.annotation.Nullable;
import kik.framework.vortex.databasemanager.annotation.OneToMany;
import kik.framework.vortex.databasemanager.annotation.OneToOne;
import kik.framework.vortex.databasemanager.annotation.Table;
import kik.framework.vortex.databasemanager.annotation.Unique;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

public class TableCreatorTest {

    private TableCreator tableCreator;
    private Storage storage;

    @BeforeEach
    public void setUp() throws UriException, InitiateServerException, IOException, SQLException {
	FileReader.readPropertyFile("application-test.properties");
	AnnotationManager.getInstance();
	storage = Storage.getInstance();
	for (var table : DatabaseStorage.getInstance().getAllTables()
		.toArray(new DBTable[DatabaseStorage.getInstance().getAllTables().size()])) {
	    DatabaseStorage.getInstance().removeTable(table);
	}
	tableCreator = TableCreator.getInstance(storage);
    }

    @Test
    public void testCreateTable() throws DataTypeException, SQLException, RelationTypeException {
	// Clase de ejemplo
	class TestClass {
	    @Column(name = "id", autoIncrement = true)
	    private int id;
	    @Column(name = "name")
	    private String name;
	}

	DBTable table = tableCreator.createTable(TestClass.class);
	assertNotNull(table);
	assertEquals("testclasss", table.name());
	assertNotNull(table.getRecord("id"));
	assertNotNull(table.getRecord("name"));
	String expectedStatement = "create table testclasss (" + "name varchar(255) not null,"
		+ "id int not null auto_increment," + "primary key (id));";
	assertEquals(expectedStatement, tableCreator.createStatement(table));
    }

    @Test
    public void testCreateTableWithRecords()
	    throws DataTypeException, SQLException, RelationTypeException, NoSuchFieldException {
	class TestClass {
	    @Column(name = "id", autoIncrement = true)
	    private int id;
	    @Column(name = "name")
	    private boolean name;
	}

	DBTable table = tableCreator.createTable(TestClass.class);
	assertNotNull(table);
	assertEquals("testclasss", table.name());

	// Verificar que los registros se han creado correctamente
	Field field = TestClass.class.getDeclaredField("name");
	RecordInfo recordInfo = table.getRecord(field.getName());
	assertNotNull(recordInfo);
	assertEquals("name", recordInfo.name());
	// Verificar el createStatement
	String expectedStatement = "create table testclasss (" + "name boolean not null,"
		+ "id int not null auto_increment," + "primary key (id));";
	assertEquals(expectedStatement, tableCreator.createStatement(table));
    }

    @Test
    public void testCreateTableWithMultipleRecords() throws DataTypeException, SQLException, RelationTypeException {
	class TestClass {
	    @ID
	    @Column(name = "id", autoIncrement = true)
	    private int id;
	    @Column(name = "name")
	    private String name;
	    @Column(name = "description")
	    @Nullable
	    private long description;
	}

	DBTable table = tableCreator.createTable(TestClass.class);
	assertNotNull(table);
	assertEquals("testclasss", table.name());
	assertNotNull(table.getRecord("id"));
	assertNotNull(table.getRecord("name"));
	assertNotNull(table.getRecord("description"));
	String expectedStatement = "create table testclasss (" +
		 "id int not null auto_increment," + 
	"name varchar(255) not null," 
	+"description bigint null,"
	+ "primary key (id));";
	assertEquals(expectedStatement, tableCreator.createStatement(table));
    }

    @Test
    public void testCreateTableWithMultipleIdRecords() throws DataTypeException, SQLException, RelationTypeException {
	@Entity
	class CompositeKeyClass {
	    @ID
	    @Column(name = "part1")
	    private int part1;
	    @ID
	    @Column(name = "part2")
	    private String part2;
	    @Column(name = "name")
	    private short name;
	}

	DBTable table = tableCreator.createTable(CompositeKeyClass.class);

	assertNotNull(table);
	assertEquals("compositekeyclasss", table.name());
	assertNotNull(table.getRecord("part1"));
	assertNotNull(table.getRecord("part2"));
	assertNotNull(table.getRecord("name"));

	List<RecordInfo> ids = table.id();
	assertEquals(2, ids.size());
	assertTrue(ids.stream().anyMatch(id -> id.name().equals("part1")));
	assertTrue(ids.stream().anyMatch(id -> id.name().equals("part2")));
	 String expectedStatement = "create table compositekeyclasss ("
		        + "part1 int not null,"
		        + "part2 varchar(255) not null,"
		        + "name smallint not null,"
		        + "primary key (part1,part2));";
		    assertEquals(expectedStatement, tableCreator.createStatement(table));
    }

    @Test
    public void testCreateTableWithDifferentTypes() throws DataTypeException, SQLException, RelationTypeException {
	class TestClass {
	    @Column(name = "id", autoIncrement = true)
	    private int id;
	    @Column(name = "name")
	    @Unique
	    private double name;
	    @Column(name = "active")
	    private boolean active;
	    @Column(name = "created_at")
	    private byte createdAt;
	}

	DBTable table = tableCreator.createTable(TestClass.class);
	assertNotNull(table);
	assertEquals("testclasss", table.name());
	assertNotNull(table.getRecord("id"));
	assertNotNull(table.getRecord("name"));
	assertNotNull(table.getRecord("active"));
	assertNotNull(table.getRecord("created_at"));
	 String expectedStatement = "create table testclasss ("
		 + "name double unique not null,"
		        + "active boolean not null,"
		        + "created_at tinyint not null,"
		        + "id int not null auto_increment,"
		        + "primary key (id));";
		    assertEquals(expectedStatement, tableCreator.createStatement(table));
    }

    @Entity
    class TestClass {
	@Column(name = "id", autoIncrement = true)
	private int id;
	@Column(name = "name")
	private String name;
	@OneToMany
	private List<RelatedClass> related;
    }

    @Entity
    class RelatedClass {
	@Column(name = "relatedClass_id", autoIncrement = true)
	private int relatedId;
	@ManyToOne
	private TestClass test;
    }

    @Test
    public void testCreateTableWithRelationManyToOne() throws DataTypeException, SQLException, RelationTypeException {

	DBTable table = tableCreator.createTable(TestClass.class);
	DBTable relatedTable = tableCreator.createTable(RelatedClass.class);

	assertNotNull(table);
	assertEquals("testclasss", table.name());
	assertNotNull(table.getRecord("id"));
	assertNotNull(table.getRecord("name"));

	assertNotNull(relatedTable);
	assertEquals("relatedclasss", relatedTable.name());
	assertNotNull(relatedTable.getRecord("relatedClass_id"));
	assertNotNull(relatedTable.getRecord("test"));

	assertEquals(1, table.relations().size());
	for (Relation relation : table.relations()) {
	    assertEquals(OneToMany.class.getSimpleName(), relation.type());
	    assertEquals("related", relation.origin());
	    assertEquals("relatedclasss", relation.destinationTable());
	    assertEquals("relatedClass_id", relation.destination());
	}
	for (Relation relation : relatedTable.relations()) {
	    assertEquals(ManyToOne.class.getSimpleName(), relation.type());
	    assertEquals("test", relation.origin());
	    assertEquals("testclasss", relation.destinationTable());
	    assertEquals("id", relation.destination());
	}
    }

    @Entity
    @Table("table1")
    class TestOneToOne {
	@Column(name = "id", autoIncrement = true)
	private int id;
	@Column(name = "name")
	private String name;
    }

    @Entity
    @Table("table2")
    class TestOneToOne2 {
	@Column(name = "TestOneToOne2Id", autoIncrement = true)
	private int relatedId;
	@OneToOne
	private TestOneToOne test;
    }

    @Test
    public void testCreateTableWithRelationOneToOne() throws DataTypeException, SQLException, RelationTypeException {

	DBTable table = tableCreator.createTable(TestOneToOne2.class);
	DBTable relatedTable = DatabaseStorage.getInstance().getTable("table1");
	assertNotNull(relatedTable);
	assertEquals("table1", relatedTable.name());
	assertNotNull(relatedTable.getRecord("id"));
	assertNotNull(relatedTable.getRecord("name"));

	assertNotNull(table);
	assertEquals("table2", table.name());
	assertNotNull(table.getRecord("TestOneToOne2Id"));
	assertNotNull(table.getRecord("table1_id"));

	assertEquals(1, table.relations().size());
	for (Relation relation : table.relations()) {
	    assertEquals(OneToOne.class.getSimpleName(), relation.type());
	    assertEquals("table1_id", relation.origin());
	    assertEquals("table1", relation.destinationTable());
	    assertEquals("id", relation.destination());
	}
	  String expectedTestClassStatement = "create table table1 ("
		  + "name varchar(255) not null," + 
		         "id int not null auto_increment," 
		        + "primary key (id));";
		    assertEquals(expectedTestClassStatement, tableCreator.createStatement(relatedTable));

		    // Verificar el createStatement para RelatedClass
		    String expectedRelatedClassStatement = "create table table2 ("
		        + "TestOneToOne2Id int not null auto_increment," +
			    "table1_id int not null," 
		        + "primary key (TestOneToOne2Id),"
		        + "foreign key (table1_id) references table1(id) on delete cascade);";
		    assertEquals(expectedRelatedClassStatement, tableCreator.createStatement(table));
    }

    @Entity
    @Table("table1")
    class OneToOneMultipleKeys {
	@ID
	@Column(name = "part1")
	private int part1;
	@ID
	@Column(name = "part2")
	private String part2;
	@Column(name = "name")
	private String name;
    }

    @Entity
    @Table("table2")
    class OneToOneMultipleKeys2 {
	@Column(name = "id", autoIncrement = true)
	@ID
	private int relatedId;
	@OneToOne
	private OneToOneMultipleKeys test;
    }

    @Test
    public void testCreateTableWithOneToOneAndMultipleIds()
	    throws DataTypeException, SQLException, RelationTypeException {
	DBTable table = tableCreator.createTable(OneToOneMultipleKeys.class);
	DBTable relatedTable = tableCreator.createTable(OneToOneMultipleKeys2.class);

	assertNotNull(table);
	assertEquals("table1", table.name());
	assertNotNull(table.getRecord("part1"));
	assertNotNull(table.getRecord("part2"));
	assertNotNull(table.getRecord("name"));

	assertNotNull(relatedTable);
	assertEquals("table2", relatedTable.name());
	assertNotNull(relatedTable.getRecord("id"));
	for (var id : table.id()) {
	    assertNotNull(relatedTable.getRecord(String.format("%s_%s", table.name(), id.name())));
	}

	List<RecordInfo> ids = table.id();
	assertEquals(2, ids.size());
	assertTrue(ids.stream().anyMatch(id -> id.name().equals("part1")));
	assertTrue(ids.stream().anyMatch(id -> id.name().equals("part2")));

	assertEquals(0, table.relations().size());
	var iterator = table.id().iterator();
	while (iterator.hasNext()) {
	    for (Relation relation : relatedTable.relations()) {
		assertEquals(OneToOne.class.getSimpleName(), relation.type());
		assertEquals("table1", relation.destinationTable());
		var id = iterator.next();
		assertEquals(String.format("%s_%s", table.name(), id.name()), relation.origin());
		assertEquals(id.name(), relation.destination());

	    }
	}
	String expectedTestClassStatement = "create table table1 ("
	        + "part1 int not null,"
	        + "part2 varchar(255) not null,"
	        + "name varchar(255) not null,"
	        + "primary key (part1,part2));";
	    assertEquals(expectedTestClassStatement, tableCreator.createStatement(table));
	    // Verificar el createStatement para RelatedClass
	    String expectedRelatedClassStatement = "create table table2 ("
	        + "id int not null auto_increment," +
		    "table1_part1 int not null,table1_part2 varchar(255) not null,"
	        + "primary key (id),"
	        + "foreign key (table1_part1,table1_part2) references table1(part1,part2) on delete cascade);";
	    assertEquals(expectedRelatedClassStatement, tableCreator.createStatement(relatedTable));
    }

    @Entity
    @Table("ManyToManyClass1")
    class ManyToManyClass1 {
	@ID
	@Column(name = "id1", autoIncrement = true)
	private int class1Id;
	@Column(name = "name")
	private String name;
	@ManyToMany
	private List<ManyToManyClass2> related;
    }

    @Entity

    @Table("ManyToManyClass2")
    class ManyToManyClass2 {
	@Column(name = "id")
	private int class2Id;
	private String description;
	@ManyToMany
	private List<ManyToManyClass1> related;
    }

    @Test
    public void testCreateTableWithManyToMany() throws DataTypeException, SQLException, RelationTypeException {
	DBTable class1Table = tableCreator.createTable(ManyToManyClass1.class);
	DBTable class2Table = tableCreator.createTable(ManyToManyClass2.class);

	assertNotNull(class1Table);
	assertEquals("ManyToManyClass1", class1Table.name());
	assertNotNull(class1Table.getRecord("id1"));
	assertNotNull(class1Table.getRecord("name"));

	assertNotNull(class2Table);
	assertEquals("ManyToManyClass2", class2Table.name());
	assertNotNull(class2Table.getRecord("id"));
	assertNotNull(class2Table.getRecord("description"));

	// Verificar la existencia de la tercera tabla para la relación ManyToMany
	DBTable relationTable = DatabaseStorage.getInstance().getRelationTable(class1Table, class2Table);
	DBTable relationTable2 = DatabaseStorage.getInstance().getRelationTable(class2Table, class1Table);
	assertNotNull(relationTable);
	assertEquals("ManyToManyClass2_ManyToManyClass1", relationTable.name());
	assertEquals(relationTable, relationTable2);

	assertNotNull(relationTable.getRecord("ManyToManyClass1_id1"));
	assertNotNull(relationTable.getRecord("ManyToManyClass2_id"));

	assertEquals(2, relationTable.relations().size());
	for (Relation relation : relationTable.relations()) {
	    assertEquals(ManyToMany.class.getSimpleName(), relation.type());
	    assertTrue(relation.origin().equals("ManyToManyClass1_id1")
		    || relation.origin().equals("ManyToManyClass2_id"));
	    assertTrue(relation.destination().equals("id1") || relation.destination().equals("id"));
	}
	   // Verificar el createStatement para ManyToManyClass1
	    String expectedClass1Statement = "create table ManyToManyClass1 ("
	        + "id1 int not null auto_increment,"
	        + "name varchar(255) not null,"
	        + "primary key (id1));";
	    assertEquals(expectedClass1Statement, tableCreator.createStatement(class1Table));

	    // Verificar el createStatement para ManyToManyClass2
	    String expectedClass2Statement = "create table ManyToManyClass2 ("
	        + "description varchar(255) not null,"
	        + "id int not null,"
	        + "primary key (id));";
	    assertEquals(expectedClass2Statement, tableCreator.createStatement(class2Table));

	    // Verificar el createStatement para la tabla de relación ManyToMany
	    String expectedRelationTableStatement = "create table ManyToManyClass2_ManyToManyClass1 ("
		    + "ManyToManyClass1_id1 int not null,"
		    + "ManyToManyClass2_id int not null,"
	        + "primary key (ManyToManyClass1_id1,ManyToManyClass2_id),"
	        + "foreign key (ManyToManyClass1_id1) references ManyToManyClass1(id1) on delete cascade,"
	        + "foreign key (ManyToManyClass2_id) references ManyToManyClass2(id) on delete cascade);";
	    assertEquals(expectedRelationTableStatement, tableCreator.createStatement(relationTable));
    }

}
