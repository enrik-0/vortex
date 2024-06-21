package kik.framework.vortex.databasemanager.storage;

public class Id {

    private String name;
    private Object value;
    public Id(String name, String value) {
	this.name = name;
	this.value = value;
    }
    
    public String getName() {
	return name;
    }
    public Object getValue() {
	return value;
    }

}
