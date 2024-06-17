package kik.framework.vortex.databasemanager.storage;

public record RecordParameters(Type data,Class<?> originalClass, int length, int precision, int scale, boolean autoIncrement) {
    

    public RecordParameters(Type data, Class<?> original) {
	this(data, original, 255,-1,-1, false);

    }
    public RecordParameters(Type data,Class<?> original, boolean autoIncrement) {
	this(data, original, 255,-1,-1, autoIncrement);

    }
}
