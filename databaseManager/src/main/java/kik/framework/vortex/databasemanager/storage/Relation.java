package kik.framework.vortex.databasemanager.storage;

import java.lang.annotation.Annotation;

public record Relation(String origin, String destinationTable, String destination, boolean cascade, String type) {

    public Relation(String field, DBTable table, RecordInfo recor, Annotation type) {
	this(field, table.name(), recor.name(), type);
    }

    public Relation(RecordInfo origin, DBTable table, RecordInfo recor, boolean cascade, Annotation type) {
	this(origin.name(), table.name(), recor.name(), cascade, type.annotationType().getSimpleName());
    }
    public Relation(RecordInfo origin, DBTable table, RecordInfo recor, boolean cascade, String type) {
	this(origin.name(), table.name(), recor.name(), cascade, type);
    }
    public Relation(String field, String table, RecordInfo recor, Annotation type) {
	this(field, new DBTable(table), recor, type);
    }

    public Relation(String field, String table, String recor, Annotation type) {
	this(field, table, recor, false, type);
    }
    public Relation(String field, String table, String recor, boolean cascade, Annotation type) {
	this(field, table, recor, cascade, type.annotationType().getSimpleName());
    }
}