package kik.framework.vortex.databasemanager;

import kik.framework.vortex.databasemanager.assests.Employee;
import kik.framework.vortex.databasemanager.assests.Person;
import kik.framework.vortex.databasemanager.storage.DBTable;
import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import kik.framework.vortex.databasemanager.storage.RecordInfo;
import kik.framework.vortex.databasemanager.utils.JPAUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class JPAUtilsTest {

    private static Person person;
    private static Employee employee;

    @BeforeAll
    public static void setUp() {
        var databaseStorage = DatabaseStorage.getInstance();

        var personRecords = new ArrayList<RecordInfo>();
        personRecords.add(new RecordInfo("name", "name", false, false, true, true, null));
        personRecords.add(new RecordInfo("surname", "surname", false, false, true, true, null));
        personRecords.add(new RecordInfo("dni", "dni", false, false, true, true, null));
        personRecords.add(new RecordInfo("date", "date", false, false, true, true, null));
        
        
        var tablePerson = new DBTable("TestTable", Person.class, false);
        tablePerson.addAllRecords(personRecords);
        databaseStorage.addTable(tablePerson);

        person = new Person();
        setField(person, "name", "John");
        setField(person, "surname", "Doe");
        setField(person, "dni", "12345678A");
        setField(person, "date", 20230618);
        setField(person, "idFailed", "ID123");

        // Configuración para Employee
        var employeeRecords = new ArrayList<RecordInfo>();
        employeeRecords.add(new RecordInfo("emp_name", "emp_name", false, false, true, true, null));
        employeeRecords.add(new RecordInfo("emp_position", "emp_position", false, false, true, true, null));
        employeeRecords.add(new RecordInfo("salary", "salary", false, false, true, true, null));
       
        var employeeTable = new DBTable("employees", Employee.class, false);
        employeeTable.addAllRecords(employeeRecords);

        databaseStorage.addTable(employeeTable);

        employee = new Employee();
        setField(employee, "name", "Alice");
        setField(employee, "position", "Developer");
        setField(employee, "salary", 80000);
    }

    private static void setField(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetValues() throws IllegalAccessException {
        Map<String, Object> values = JPAUtils.getValues(person);
        assertEquals(5, values.size());
        assertEquals("John", values.get("name"));
        assertEquals("Doe", values.get("surname"));
        assertEquals("12345678A", values.get("dni"));
        assertEquals(20230618, values.get("date"));
        assertEquals("ID123", values.get("idfailed"));
    }

    @Test
    public void testGetValuesWithAnnotations() throws IllegalAccessException {
        Map<String, Object> values = JPAUtils.getValues(employee);
        assertEquals(3, values.size());
        assertEquals("Alice", values.get("emp_name"));
        assertEquals("Developer", values.get("emp_position"));
        assertEquals(80000, values.get("salary"));
    }

}
