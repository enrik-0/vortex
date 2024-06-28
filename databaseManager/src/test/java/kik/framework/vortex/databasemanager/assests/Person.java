package kik.framework.vortex.databasemanager.assests;

import kik.framework.vortex.databasemanager.annotation.Column;
import kik.framework.vortex.databasemanager.annotation.ID;
import kik.framework.vortex.databasemanager.annotation.OneToMany;
import kik.framework.vortex.databasemanager.annotation.Table;
@Table("TestTable")

public class Person {


    @ID
    private String name;
    
    @Column(name = "lastName")
    private String surname;
    private String dni;
    private int  date;
    private String idFailed;
    @OneToMany
    private Employee employee;
public Person() {
	name = "Manolo";
    }

}
