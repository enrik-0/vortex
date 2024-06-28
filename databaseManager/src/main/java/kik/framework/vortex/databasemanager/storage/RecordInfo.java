package kik.framework.vortex.databasemanager.storage;

import java.lang.reflect.Field;

import kik.framework.vortex.databasemanager.annotation.ManyToOne;

public record RecordInfo(String name,String fieldName, boolean identifier, boolean unique,
	boolean nullable, boolean saved, RecordParameters data) {
    
    public RecordInfo(String name, String fieldName, boolean identifier, boolean unique, boolean nullable, RecordParameters data){
	this(name, fieldName, identifier, unique, nullable, true, data);
    }

    public RecordInfo(String name, Field fieldName, boolean identifier, boolean unique, boolean nullable, RecordParameters data){
	this(name, fieldName.getName(), identifier, unique, nullable, true, data);
    }
    
    public RecordInfo merge(RecordInfo toMerge, Relation relation) {
	RecordInfo recor;
	if(toMerge.data().data().isPrimitive()) {
	    
	return new RecordInfo(toMerge.name, fieldName, toMerge.identifier, toMerge.unique, toMerge.nullable, toMerge.saved, data);
	}
	else if(relation.type().equals(ManyToOne.class.getSimpleName())){
	recor = new RecordInfo(name, toMerge.fieldName, toMerge.identifier, toMerge.unique, toMerge.nullable, toMerge.saved, data);
	}else {
	    recor = new RecordInfo(name, fieldName, toMerge.identifier, toMerge.unique, toMerge.nullable, toMerge.saved, data);
	}
	return recor;
    }
}



