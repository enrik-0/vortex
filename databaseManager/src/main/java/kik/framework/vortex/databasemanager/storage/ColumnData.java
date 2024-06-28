package kik.framework.vortex.databasemanager.storage;

public record ColumnData(String name, int length, int precision, int scale, boolean autoIncrement) {

    
    ColumnData(String name){
	this(name, 255, -1, -1, false);
    }
    public ColumnData copy() {
	
	return new ColumnData(name, length, precision, scale, autoIncrement);
    }
}
