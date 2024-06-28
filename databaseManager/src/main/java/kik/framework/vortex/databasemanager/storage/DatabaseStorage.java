package kik.framework.vortex.databasemanager.storage;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import edu.emory.mathcs.backport.java.util.Collections;
import kik.framework.vortex.databasemanager.annotation.ManyToMany;
import kik.framework.vortex.databasemanager.annotation.ManyToOne;
import kik.framework.vortex.databasemanager.annotation.Table;
import kik.framework.vortex.databasemanager.exception.RepositoryNotExistsException;
import vortex.annotate.components.Repository;
import vortex.annotate.manager.Storage;

public final class DatabaseStorage {

    private static DatabaseStorage storage;
    private Collection<DBTable> tables;
    private Graph<String, DefaultEdge> relations = new DefaultDirectedGraph<>(DefaultEdge.class);

    private DatabaseStorage() {
	tables = new ArrayList<>();
    }

    public static DatabaseStorage getInstance() {
	synchronized (DatabaseStorage.class) {
	    if (storage == null) {
		storage = new DatabaseStorage();
	    }

	}
	return storage;
    }

    public DBTable getTable(Class<?> clazz) {
	String tableName = null;
	for (Annotation annotation : clazz.getAnnotations()) {
	    if (annotation instanceof Table) {
		Table table = (Table) annotation;
		tableName = table.value();
	    }
	}
	if (tableName == null) {
	    tableName = clazz.getSimpleName().toLowerCase() + "s";
	}
	return getTable(tableName);

    }

    public DBTable getTable(String name) {
	DBTable table;
	try {

	    table = tables.stream().filter(dbTable -> {
		return dbTable.name().equals(name);
	    }).findFirst().get();
	} catch (NoSuchElementException e) {
	    table = null;
	}
	return table;
    }

    public DBTable getRelationTable(DBTable table1, DBTable table2) {
	DBTable table = null;
	try {
	    if (table1.isInheritance()) {
		if (table2.isInheritance()) {

		    table = getRelationTable(getTable(table1.clazz().getSuperclass()),
			    getTable(table2.clazz().getSuperclass()));
		} else {
		    table = getRelationTable(getTable(table1.clazz().getSuperclass()), table2);
		}
	    } else

	    if (table2.isInheritance()) {
		table = getRelationTable(table1, getTable(table2.clazz().getSuperclass()));
	    } else {

		table = tables.stream().filter(dbTable -> {
		    return dbTable.name().contains(table1.name()) && dbTable.name().contains(table2.name());
		}).findFirst().get();
	    }

	} catch (NoSuchElementException e) {
	    table = null;
	}
	return table;
    }

    public DatabaseStorage addTable(DBTable table) {
	if (getTable(table.name()) == null) {
	    tables.add(table);
	    relations.addVertex(table.name());
	}
	return this;
    }

    public DatabaseStorage addTables(Collection<DBTable> tables) {
	tables.forEach(this::addTable);
	return this;
    }

    public DatabaseStorage addRelation(String origin, String destination) {
	relations.addEdge(origin, destination);
	return this;
    }

    public List<DBTable> orderRelations() {
	TopologicalOrderIterator<String, DefaultEdge> iterator = new TopologicalOrderIterator<>(relations);
	List<String> order = new ArrayList<>();
	List<DBTable> ordered = new ArrayList<>();
	while (iterator.hasNext()) {
	    String element = iterator.next();
	    if(element != null)
	    order.add(element);
	}

	order.stream().forEach(table -> {
	    ordered.add(getTable(table));
	});
	Collections.reverse(ordered);
	return ordered;
    }

    public DatabaseStorage fillRelationGraph() {

	for (DBTable table : tables) {
	    for (Relation relation : table.relations()) {
		if (relation.type().equals(ManyToOne.class.getSimpleName())) {
		    break;
		}
		if (relation.type().equals(ManyToMany.class.getSimpleName()) && !table.created()) {
		    break;
		}
		if(relation.type().equals("inheritance")) {
		    
		addRelation( table.name(),relation.destinationTable());
		}else
		addRelation(relation.destinationTable(), table.name());
	    }
	}

	return this;
    }

    public kik.framework.vortex.databasemanager.Repository getRepository(Class<?> clazz) throws RepositoryNotExistsException {
	Object object = null;
	var repositories = Storage.getInstance().getComponent(Repository.class);
	    var optional = repositories.stream().filter(repo -> {
		var tClass = ((ParameterizedType) repo.getGenericSuperclass()).getActualTypeArguments()[0];
		return tClass.getTypeName().equals(clazz.getTypeName());
	    }).findFirst();
	    if(optional.isEmpty()) {
		throw new RepositoryNotExistsException(String.format("Repository for %s class not found", clazz.getSimpleName()));
	    }
	    var repository = optional.get();

	    Objenesis objenesis = new ObjenesisStd();
	    ObjectInstantiator instantiator = objenesis.getInstantiatorOf(repository);
	    object = instantiator.newInstance();
	return (kik.framework.vortex.databasemanager.Repository) object;
    }

    public Collection<DBTable> getAllTables() {
	return Collections.unmodifiableCollection(tables);
    }

    public void removeTable(DBTable table) {
	tables.remove(table);
	relations.removeVertex(table.name());

    }

}