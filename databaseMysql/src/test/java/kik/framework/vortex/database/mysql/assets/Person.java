package kik.framework.vortex.database.mysql.assets;

import java.util.List;

public class Person extends User {

    public Person(String name, String surname, int nomina, int id, List<Vehicle> vehicles) {
	super(name, surname, nomina, id, vehicles);
    }

    public Person(String name, String surname, int nomina, int id) {
	super(name, surname, nomina, id);
    }

    public List<Vehicle> getVehicles() {
	return vehicles;
    }
}
