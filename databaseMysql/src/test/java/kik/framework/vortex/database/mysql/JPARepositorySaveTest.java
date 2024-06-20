package kik.framework.vortex.database.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import kik.framework.vortex.database.mysq.connector.Connector;
import kik.framework.vortex.database.mysql.assets.Book;
import kik.framework.vortex.database.mysql.assets.BookRepository;
import kik.framework.vortex.database.mysql.assets.Library;
import kik.framework.vortex.database.mysql.assets.LibraryRepository;
import kik.framework.vortex.database.mysql.assets.Person;
import kik.framework.vortex.database.mysql.assets.PersonRepository;
import kik.framework.vortex.database.mysql.assets.Truck;
import kik.framework.vortex.database.mysql.assets.TruckRepository;
import kik.framework.vortex.database.mysql.assets.User;
import kik.framework.vortex.database.mysql.assets.UserRepository;
import kik.framework.vortex.database.mysql.assets.Vehicle;
import kik.framework.vortex.database.mysql.assets.VehicleRepository;
import kik.framework.vortex.database.mysql.storage.Manager;
import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.exception.RelationShipNotExistsException;
import kik.framework.vortex.databasemanager.exception.RelationTypeException;
import kik.framework.vortex.databasemanager.exception.RepositoryNotExistsException;
import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.manager.AnnotationManager;
import vortex.annotate.manager.Storage;
import vortex.properties.filemanager.FileReader;

public class JPARepositorySaveTest {

    @BeforeAll
    public static void setUp() throws UriException, InitiateServerException, SQLException, RelationTypeException,
	    IOException, DataTypeException {
	FileReader.readPropertyFile("application-test.properties");
	List<Thread> threads = new ArrayList<>();

	AnnotationManager.getInstance();
	TableCreator.getInstance().createTable(Person.class);
	TableCreator.getInstance().createTable(Book.class);
	TableCreator.getInstance().createTable(Truck.class);
	var r = DatabaseStorage.getInstance().getAllTables();

	Connector.getInstance().sendRequest(
		TableCreator.getInstance().createStatement(DatabaseStorage.getInstance().getTable(User.class)));
	Connector.getInstance().sendRequest(
		TableCreator.getInstance().createStatement(DatabaseStorage.getInstance().getTable(Person.class)));
	Connector.getInstance().sendRequest(
		TableCreator.getInstance().createStatement(DatabaseStorage.getInstance().getTable(Vehicle.class)));
	Connector.getInstance().sendRequest(
		TableCreator.getInstance().createStatement(DatabaseStorage.getInstance().getTable(Library.class)));
	Connector.getInstance().sendRequest(
		TableCreator.getInstance().createStatement(DatabaseStorage.getInstance().getTable(Book.class)));
	Connector.getInstance().sendRequest(
		TableCreator.getInstance().createStatement(DatabaseStorage.getInstance().getTable(Truck.class)));
	Connector.getInstance()
		.sendRequest(TableCreator.getInstance()
			.createStatement(DatabaseStorage.getInstance().getRelationTable(
				DatabaseStorage.getInstance().getTable(User.class),
				DatabaseStorage.getInstance().getTable(Vehicle.class))));
    }

    @AfterAll
    static void drop() throws SQLException {

	Connector.getInstance()
		.sendRequest("drop table vehicles_users, books, librarys, vehicles,trucks,persons,users");
    }

    @Test
    void saveEntityOneToOneRelation()
	    throws SQLException, DataTypeException, RepositoryNotExistsException, RelationShipNotExistsException {
	UserRepository user = new UserRepository();
	Truck truck = new Truck(new User("Manuel", "Izaguirre", 2500, 1));

	int count = numberOfRecords("trucks");
	assertEquals(0, count);
	count = numberOfRecords("users");
	assertEquals(0, count);
	TruckRepository repository = new TruckRepository();
	user.save(new User("Manuel", "Izaguirre", 2500, 1));
	repository.save(truck);
	count = numberOfRecords("trucks");
	assertEquals(1, count);
	count = numberOfRecords("users");
	assertEquals(1, count);

	repository.delete(truck);
	count = numberOfRecords("trucks");
	assertEquals(0, count);
	count = numberOfRecords("users");
	assertEquals(1, count);
	user.delete(new User("Manuel", "Izaguirre", 2500, 1));
	count = numberOfRecords("users");
	assertEquals(0, count);
    }

    @Test
    void saveEntityOneToOneRelationWithInheritance()
	    throws SQLException, DataTypeException, RepositoryNotExistsException, RelationShipNotExistsException {
	PersonRepository person = new PersonRepository();
	Truck truck = new Truck(new Person("Manuel", "Izaguirre", 2500, 1));

	int count = numberOfRecords("trucks");
	assertEquals(0, count);
	count = numberOfRecords("persons");
	assertEquals(0, count);
	count = numberOfRecords("users");
	assertEquals(0, count);
	TruckRepository repository = new TruckRepository();
	person.save(new Person("Manuel", "Izaguirre", 2500, 1));
	repository.save(truck);
	count = numberOfRecords("trucks");
	assertEquals(1, count);
	count = numberOfRecords("users");
	assertEquals(1, count);
	count = numberOfRecords("persons");
	assertEquals(1, count);

	person.delete(new Person("Manuel", "Izaguirre", 2500, 1));
	assertEquals(0, numberOfRecords("persons"));
	assertEquals(0, numberOfRecords("users"));
	System.err.println();

    }

    @Test
    void saveEntityOneToOneRelationWithInheritanceUsingFatherRepository()
	    throws SQLException, DataTypeException, RepositoryNotExistsException, RelationShipNotExistsException {
	UserRepository person = new UserRepository();
	Truck truck = new Truck(new Person("Manuel", "Izaguirre", 2500, 1));

	int count = numberOfRecords("trucks");
	assertEquals(0, count);
	count = numberOfRecords("persons");
	assertEquals(0, count);
	count = numberOfRecords("users");
	assertEquals(0, count);
	TruckRepository repository = new TruckRepository();
	person.saveInheriance(new Person("Manuel", "Izaguirre", 2500, 1));
	repository.save(truck);
	count = numberOfRecords("trucks");
	assertEquals(1, count);
	count = numberOfRecords("users");
	assertEquals(1, count);
	count = numberOfRecords("persons");
	assertEquals(1, count);

	person.delete(new Person("Manuel", "Izaguirre", 2500, 1));
	assertEquals(0, numberOfRecords("persons"));
	assertEquals(0, numberOfRecords("users"));

    }

    @Test
    @Order(5)
    void findManyToMany() throws SQLException, DataTypeException, RepositoryNotExistsException,
	    RelationShipNotExistsException, InterruptedException {
	List<Vehicle> vehicles = new ArrayList<>();
	List<User> users = new ArrayList<>();
	vehicles.add(new Vehicle(2, "toyota", users));
	vehicles.add(new Vehicle(3, "audi", users));
	vehicles.add(new Vehicle(4, "maria", users));
	vehicles.add(new Vehicle(5, "zaragoza", users));
	vehicles.add(new Vehicle(1, "seat", users));
	users.add(new User("maria", "mendoza", 6000, 1, vehicles));
	users.add(new User("manolo", "chaton", 3500, 2, vehicles));
	users.add(new User("zhalatia", "obsnur", 1000, 3, vehicles));

	UserRepository userRepo = new UserRepository();
	VehicleRepository vehiclesRepo = new VehicleRepository();
	userRepo.saveAll(users);

	for (User user : users) {
	    User temp = userRepo.findById(userRepo.generateId(user));
	    assertNotEquals(user, temp);
	    assertEquals(user.getUser_id(), temp.getUser_id());
	    assertEquals(user.getName(), temp.getName());
	    assertEquals(user.getSalary(), temp.getSalary());
	    assertEquals(user.getSurname(), temp.getSurname());
	}

	for (Vehicle vehicle : vehicles) {
	    Vehicle temp = vehiclesRepo.findById(vehicle.getIdVehicle());
	    assertNotEquals(vehicle, temp);
	    assertEquals(vehicle.getIdVehicle(), temp.getIdVehicle());
	    assertEquals(vehicle.getRegisterNumber(), temp.getRegisterNumber());
	    assertNotEquals(vehicle.getUsers(), temp.getUsers());
	    assertEquals(vehicle.getUsers().size(), temp.getUsers().size());

	}

	for (User user : users) {
	    userRepo.delete(user);
	}
	for (Vehicle vehicle : vehicles) {
	    vehiclesRepo.delete(vehicle);

	}
	vehiclesRepo.saveAll(vehicles);

	Vehicle searched = vehiclesRepo.findById("toyota");
	for (User user : users) {
	    userRepo.delete(user);

	}
	for (Vehicle vehicle : vehicles) {
	    vehiclesRepo.delete(vehicle);
	}

    }

    @Test
    @Order(4)
    void findByIdTest() throws SQLException, RepositoryNotExistsException, RelationShipNotExistsException {
	User user = new User("Manuel", "1", 3000, 1);
	User user2 = new User("Manuel", "2", 4000, 2);
	User user3 = new User("Manuel", "Callas", 5000, 3);
	UserRepository repository = new UserRepository();
	repository.saveAll(List.of(user, user2, user3));
	var list = List.of(user, user2, user3);
	for (User u : list) {
	    var map = repository.generateId(u);
	    var temp = repository.findById(map);
	    assertFalse(u.equals(temp));
	    assertEquals(u.getUser_id(), temp.getUser_id());
	    assertEquals(u.getName(), temp.getName());
	    assertEquals(u.getSalary(), temp.getSalary());
	    assertEquals(u.getSurname(), temp.getSurname());
	}

	for (User u : list) {
	    repository.delete(u);
	}
    }

    @Test
    void updateTest() throws SQLException, DataTypeException, RepositoryNotExistsException, RelationShipNotExistsException {
	BookRepository books = new BookRepository();
	LibraryRepository librarys = new LibraryRepository();
	int count = numberOfRecords("books");
	assertEquals(0, count);
	count = numberOfRecords("librarys");
	assertEquals(0, count);
	Library library = new Library("Alcucia", "Real", 2);
	Book book = new Book("Hamlet", "123", library);
	librarys.save(new Library("Alcucia", "Real", 2));
	books.save(book);
	count = numberOfRecords("books");
	assertEquals(1, count);
	count = numberOfRecords("librarys");
	assertEquals(1, count);
	books.delete(book);
	librarys.delete(library);
	assertEquals(0, numberOfRecords("books"));
	assertEquals(0, numberOfRecords("librarys"));

    }

    private int numberOfRecords(String name) throws SQLException {
	int count = -1;
	var c = Connector.getInstance().getConnection();
	var resultSet = Connector.getInstance().sendResultRequest(c, String.format("select count(*) from %s", name));
	assertTrue(resultSet.next());
	count = resultSet.getInt(1);
	c.close();
	return count;
    }

}
