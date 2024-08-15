package kik.framework.vortex.databasemanager.storage;

import java.lang.annotation.Annotation;


/**
 * Data Structure that represents a relationship between tables
 *
 * @param origin           {@link RecordInfo#name} of the origin registry
 * @param destinationTable {@link DBTable#name} of the destination table
 * @param destination      {@link RecordInfo#name} of the destination registry
 * @param cascade          delete query's must be cascade or not
 * @param type             relation type it can be ManyToMany, ManyToOne, OneToMany, OneToOne and inheritance
 */
public
record Relation(String origin, String destinationTable, String destination, boolean cascade, String type){

    public
    Relation(RecordInfo origin, DBTable table, RecordInfo recor, boolean cascade, Annotation type){
        this(origin.name(), table.name(), recor.name(), cascade, type.annotationType().getSimpleName());
    }

    public
    Relation(RecordInfo origin, DBTable table, RecordInfo recor, boolean cascade, String type){
        this(origin.name(), table.name(), recor.name(), cascade, type);
    }

    public
    Relation(String field, String table, RecordInfo recor, Annotation type){
        this(field, new DBTable(table), recor, type);
    }

    public
    Relation(String field, DBTable table, RecordInfo recor, Annotation type){
        this(field, table.name(), recor.name(), type);
    }

    public
    Relation(String field, String table, String recor, Annotation type){
        this(field, table, recor, false, type);
    }

    public
    Relation(String field, String table, String recor, boolean cascade, Annotation type){
        this(field, table, recor, cascade, type.annotationType().getSimpleName());
    }
}