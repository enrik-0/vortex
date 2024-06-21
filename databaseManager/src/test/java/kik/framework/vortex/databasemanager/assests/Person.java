package kik.framework.vortex.databasemanager.assests;

import kik.framework.vortex.databasemanager.annotation.ID;
import kik.framework.vortex.databasemanager.annotation.Table;
@Table("TestTable")

public class Person {


    @ID
    private String name;
    
    private String surname;
    private String dni;
    private int  date;
    private String idFailed;
    public Person() {
	name = "Manolo";
    }

}
