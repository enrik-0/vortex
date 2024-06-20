package kik.framework.vortex.databasemanager.assests;

import kik.framework.vortex.databasemanager.annotation.Column;

public class Employee {

    @Column(name = "emp_name")
    private String name;

    @Column(name = "emp_position")
    private String position;

    private int salary;

    public Employee() {
        // Constructor vacío
    }

    // Getters y setters pueden ser añadidos si es necesario
}
