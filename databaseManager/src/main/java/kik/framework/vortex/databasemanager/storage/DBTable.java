package kik.framework.vortex.databasemanager.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import kik.framework.vortex.databasemanager.Repository;

public record DBTable(String name, Class<?> clazz, boolean created, Collection<RecordInfo> records,
	Collection<Relation> relations) {
    public DBTable {
	if (name == null || name.isEmpty()) {
	    throw new IllegalArgumentException("The name cannot be null or empty");
	}
	records = new ArrayList<>(records);
    }

    public DBTable(String name, Class<?> clazz) {
	this(name, clazz, false);
    }

    public DBTable(String name, Class<?> clazz, boolean created) {
	this(name, clazz, created, new ArrayList<>(), new ArrayList<>());
    }

    public DBTable(String name) {
	this(name, null);
    }

    public boolean isInheritance() {
	
	return !clazz.getSuperclass().equals(Object.class) && !clazz.equals(Relation.class);
    }

    public Collection<RecordInfo> records() {
	// Return an unmodifiable view of the collection to protect it from external
	// modifications
	return Collections.unmodifiableCollection(records);
    }

    public Collection<Relation> relations() {
	// Return an unmodifiable view of the collection to protect it from external
	// modifications
	return Collections.unmodifiableCollection(relations);
    }

    // Method to add records to the internal mutable collection
    public DBTable addRecord(RecordInfo record) {
	if (record != null && getRecord(record.name()) == null) {
	    ((ArrayList<RecordInfo>) records).add(record);
	}
	return this;
    }

    // Method to remove records from the internal mutable collection
    public DBTable removeRecord(RecordInfo record) {
	((ArrayList<RecordInfo>) records).remove(record);
	return this;
    }

    public RecordInfo getRecord(String name) {
	RecordInfo recor;
	try {
	    recor = records().stream().filter(r -> r.name().equals(name)).findFirst().get();
	} catch (NoSuchElementException e) {
	    recor = null;
	}
	return recor;
    }

    public DBTable addRelation(Relation relation) {
	if (relation != null && getRelation(relation.origin()) == null) {
	    relations.add(relation);
	}

	return this;
    }

    public Relation getRelation(String origin) {
	Relation relation;
	try {

	    relation = relations.stream().filter(r -> {
		return r.origin().equals(origin);
	    }).findFirst().get();
	} catch (NoSuchElementException e) {
	    relation = null;
	}
	return relation;

    }

    public List<RecordInfo> id() {
	return records.stream().filter(RecordInfo::identifier).toList();
    }

    public Map<String, List<Relation>> mapRelations() {
	Map<String, List<Relation>> map = new HashMap<>();

	for (Relation relation : getAllRelations()) {
	    if (!map.containsKey(relation.destinationTable())) {
		map.put(relation.destinationTable(), new ArrayList<>());
	    }
	    map.get(relation.destinationTable()).add(relation);
	}
	return map;
    }

    public List<RecordInfo> getAllRecords() {
	List<RecordInfo> allRecord = new ArrayList<>();
	allRecord.addAll(records);
	if(isInheritance()) {
	    allRecord.addAll(DatabaseStorage.getInstance().getTable(clazz.getSuperclass()).getAllRecords());
	}

	return allRecord;
    }
    public List<Relation> getAllRelations() {
	List<Relation> allRecord = new ArrayList<>();
	allRecord.addAll(relations);
	if(isInheritance()) {
	    allRecord.addAll(DatabaseStorage.getInstance().getTable(clazz.getSuperclass()).getAllRelations());
	}

	return allRecord;
    }

    public boolean hasId() {

	return records.stream().filter(RecordInfo::identifier).count() > 0;
    }

    public boolean isAssginableFor(String tableName) {
	
	if(tableName.equals(name)) {
	    return true;
	}
	if(isInheritance()) {
	    return DatabaseStorage.getInstance().getTable(clazz.getSuperclass()).isAssginableFor(tableName);
	}
	return false;
    }
}
