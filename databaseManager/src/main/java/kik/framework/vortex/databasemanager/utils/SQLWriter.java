package kik.framework.vortex.databasemanager.utils;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import kik.framework.vortex.databasemanager.storage.RecordInfo;
import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.storage.DBTable;

public final class SQLWriter {

    public static String insert(DBTable table, Map<String, Object> values) throws DataTypeException {
	StringBuilder builder = new StringBuilder();
	StringJoiner namesJoiner = new StringJoiner(",");
	StringJoiner valuesJoiner = new StringJoiner(",");
	builder.append(String.format("insert into %s", table.name()));

	for (String key : values.keySet()) {

	    RecordInfo reco = null;
	    var optionalReco = table.records().stream().filter(r -> {
		return r.name().toLowerCase().equals(key);
	    }).findFirst();
	    if (optionalReco.isPresent()) {
		reco = optionalReco.get();
	    }
	    if (reco != null && reco.saved()) {

		Object value = values.get(key);
		namesJoiner.add(key);
		if (reco.nullable() && reco.identifier()) {
		    throw new DataTypeException("Identiferes can not be null");
		}
		if(value == null && !reco.nullable()) {
		    throw new DataTypeException("null value in a non nullable record");
		}
		if (value == null && reco.nullable() && !reco.identifier()) {
		    valuesJoiner.add("null");
		} else if (value instanceof String) {
		    valuesJoiner.add(String.format("\"%s\"", (String) value));
		} else {

		    valuesJoiner.add(value.toString());
		}
	    }
	}
	builder.append(String.format(" (%s) values (%s);", namesJoiner.toString(), valuesJoiner.toString()));
	return builder.toString();
    }

    public static String insert(Class<?> clazz, Map<String, Object> values) throws DataTypeException {
	DBTable table = DatabaseStorage.getInstance().getTable(clazz);
	return insert(table, values);

    }

    public static String insertAll(Class<?> clazz, List<Map<String, Object>> values) {
	StringBuilder builder = new StringBuilder();
	StringJoiner joiner = new StringJoiner(",");
	DBTable table = DatabaseStorage.getInstance().getTable(clazz);

	builder.append("insert into ");
	builder.append(table.name());
	values.get(0).keySet().forEach(joiner::add);
	builder.append(" (" + joiner.toString() + ") ");
	joiner = new StringJoiner(",");
	builder.append("values");
	for (var value : values) {

	    var valJoiner = new StringJoiner(",");
	    for (Object val : value.values()) {
		if (val instanceof String) {
		    valJoiner.add(String.format("\"%s\"", (String) val));
		} else {
		    valJoiner.add(val.toString());
		}

	    }
	    joiner.add("(" + valJoiner.toString() + ")");
	}
	builder.append(joiner.toString() + ";");
	return builder.toString();
    }

    public static String find(Map<String, Object> filter, DBTable table) {
	StringBuilder builder = new StringBuilder();
	StringJoiner whereJoiner = new StringJoiner(" and ");
	StringJoiner paramJoiner = new StringJoiner(",");
	table.records().forEach(r -> {
	    if (r.saved())
		paramJoiner.add(String.format("%s as %s", r.name(), r.name()));
	});

	builder.append(String.format("select %s from %s where ", paramJoiner.toString(), table.name()));
	filter.forEach((name, value) -> {
	    RecordInfo r = table.getRecord(name);
	    if(r != null)
	    if ( r.data().data().isString()) {

		whereJoiner.add(String.format("%s = \'%s\'", name, value));
	    } else {
		whereJoiner.add(String.format("%s = %s", name, value));

	    }
	});
	builder.append(whereJoiner);
	builder.append(";");
	return builder.toString();

    }

    public static String findAll(Class<?> clazz) {
	DBTable table = DatabaseStorage.getInstance().getTable(clazz);
	return findAll(table);
    }

    public static String findAll(DBTable table) {
	StringJoiner namesJoiner = new StringJoiner(",");
	for (RecordInfo record : table.id()) {
	    namesJoiner.add(record.name());
	}
	return String.format("select (%s) from %s;", namesJoiner.toString(), table.name());
    }

    public static String update(DBTable table, Map<String, Object> values) {
	StringJoiner whereJoiner = new StringJoiner(" and ");
	StringJoiner setJoiner = new StringJoiner(",");
	for(RecordInfo r : table.getAllRecords()) {
	    for(String key : values.keySet()) {
		    if(r.identifier() && r.name().toLowerCase().equals(key)) {
			if(r.data().data().isString()) {
			    whereJoiner.add(String.format("%s = \'%s\'", key, values.get(key)));
			}else {
			    whereJoiner.add(String.format("%s = %s", key, values.get(key)));
			    
			}
		    }
		    if (r.saved() && r.name().toLowerCase().equals(key)) {
			    if(r.data().data().isString()) {
			setJoiner.add(String.format("%s = \'%s\'", key, values.get(key)));
		    }else {
			setJoiner.add(String.format("%s = %s", key, values.get(key)));
			
		    }
		}
	    }
	}

	return String.format("update %s set %s where %s", table.name(), setJoiner.toString(), whereJoiner.toString());
	
    }
    public static String delete(DBTable table, Map<String, Object> where) {
	if(where.isEmpty()){
	    return null;
	}
	StringJoiner whereJoiner = new StringJoiner(" and ");

	for(String key : where.keySet()) {
	    RecordInfo recor = table.getRecord(key);
	    if(recor != null && recor.data().data().isString()) {
		whereJoiner.add(String.format("%s = \'%s\' ", key, where.get(key)));
	    }else {
		
		whereJoiner.add(String.format("%s = %s ", key, where.get(key)));
	    }
	}
	
	
	
	return String.format("delete from %s where %s", table.name(), whereJoiner.toString());
	
    }

}
