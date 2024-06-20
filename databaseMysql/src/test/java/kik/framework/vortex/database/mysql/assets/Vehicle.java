package kik.framework.vortex.database.mysql.assets;

import java.util.ArrayList;
import java.util.List;

import kik.framework.vortex.databasemanager.annotation.Column;
import kik.framework.vortex.databasemanager.annotation.ManyToMany;
import vortex.annotate.components.Entity;

@Entity
public class Vehicle {

    @Column(name = "register", autoIncrement = true)
    private int registerNumber;

    public int getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(int registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(String idVehicle) {
        this.idVehicle = idVehicle;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Column(length = 35, name = "id")
    private String idVehicle;

    @ManyToMany
    private List<User> users;

    public Vehicle(int register, String id, List<User> users) {
	registerNumber = register;
	idVehicle = id;
	this.users = users;
    }

    public Vehicle(int register, String id) {
	this(register, id, new ArrayList<>());
    }
}
