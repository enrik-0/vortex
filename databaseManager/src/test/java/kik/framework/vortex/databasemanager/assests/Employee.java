package kik.framework.vortex.databasemanager.assests;

import java.util.List;

import kik.framework.vortex.databasemanager.annotation.Column;
import kik.framework.vortex.databasemanager.annotation.ManyToOne;

public class Employee {

    @Column(name = "emp_name")
    private String name;

    @Column(name = "emp_position")
    private String position;

    private int salary;
    
    @ManyToOne
    private List<Person> persons;

public Employee() {
    }

}
