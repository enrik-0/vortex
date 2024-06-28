package kik.framework.vortex.database.mysql.assets;

import java.util.ArrayList;
import java.util.List;

import kik.framework.vortex.databasemanager.annotation.Column;
import kik.framework.vortex.databasemanager.annotation.ID;
import kik.framework.vortex.databasemanager.annotation.ManyToMany;
import kik.framework.vortex.databasemanager.annotation.Nullable;
import kik.framework.vortex.databasemanager.annotation.Unique;
import vortex.annotate.components.Entity;

@Entity
public class User {

    @Nullable
    private String name;
    @Unique
    @Column(name = "surname", length = 35)
    @ID
    private String surname;
    private int salary;

    @Column(name = "id")
    @ID
    private int user_id;

    @ManyToMany
    protected List<Vehicle> vehicles;
    public User(String name, String surname, int salary, int id) {
	this.name = name;
	this.surname = surname;
	this.salary = salary;
	this.user_id = id;
	vehicles = new ArrayList<>();
    }
    public User(String name, String surname, int salary, int id, List<Vehicle> vehicles) {
	this.name = name;
	this.surname = surname;
	this.salary = salary;
	this.user_id = id;
	this.vehicles = vehicles;
    }

    public String getName() {
	return name;
    }

    public boolean isAlive() {
	return true;
    }

    public String setName() {
	return name;
    }

    public String setpa() {
	return name;
    }

    public String getSurname() {
	return surname;
    }

    public int getSalary() {
	return salary;
    }


    public int getUser_id() {
	return user_id;
    }
}
