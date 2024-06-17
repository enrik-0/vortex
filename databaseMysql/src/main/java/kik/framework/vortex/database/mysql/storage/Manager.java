package kik.framework.vortex.database.mysql.storage;

import java.lang.Thread.State;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import kik.framework.vortex.database.mysq.connector.Connector;
import kik.framework.vortex.database.mysql.TableCreator;
import kik.framework.vortex.databasemanager.annotation.ManyToMany;
import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.exception.RelationTypeException;
import kik.framework.vortex.databasemanager.storage.DBTable;
import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import kik.framework.vortex.databasemanager.storage.Relation;
import kik.framework.vortex.databasemanager.storage.StorageManager;
import vortex.annotate.components.Entity;
import vortex.annotate.manager.Storage;

public class Manager extends StorageManager {

    public Manager() throws SQLException, RelationTypeException {

	initialize(Storage.getInstance());
    }

    public Manager(Storage storage) throws SQLException, RelationTypeException {

	initialize(storage);
    }

    @Override
    public void initialize(Storage storage) throws SQLException, RelationTypeException {
	List<Class<?>> entities = storage.getComponent(Entity.class);
	List<Thread> threads = new ArrayList<>();
	sort(entities);
	for (Class<?> entitie : entities) {
	    DBTable table = null;
	    try {
		table = TableCreator.getInstance(storage).createTable(entitie);
	    } catch (DataTypeException | SQLException e) {
		e.printStackTrace();
	    }
	    DatabaseStorage.getInstance().addTable(table);

	}
	List<DBTable> tables = DatabaseStorage.getInstance().fillRelationGraph().orderRelations();
	for (DBTable table : tables) {

	    Runnable runnable = new Runnable() {
		public void run() {
		    try {
			int count = 0;
			String sql = String.format(
				"select count(*) as count from information_schema.tables where table_schema = \'%s\' and table_name = \'%s\'",
				Connector.getInstance().getSchema(), table.name());

			ResultSet tableExists = Connector.getInstance().sendResultRequest(sql).executeQuery(sql);
			if (tableExists.next()) {
			    count = tableExists.getInt("count");
			}
			tableExists.close();
			if (count == 0) {
			    boolean dependenciesCreated = false;
			    int tries = 0;
			    var relationMap = table.mapRelations();
			    do {
				List<Boolean> results = new ArrayList<>();
				results.add(true);
				for (String tableName : relationMap.keySet()) {
				    Collection<Relation> relations = table 
					    .relations();
				    long nrelations = relations.stream().filter(r -> {
					return !r.type().equals(ManyToMany.class.getSimpleName());
				    }).count();
				    if (table.created() || nrelations > 0) {

					sql = String.format(
						"select count(*) as count from information_schema.tables where table_schema = \'%s\' and table_name = \'%s\'",
						Connector.getInstance().getSchema(), tableName);
					tableExists = Connector.getInstance().sendResultRequest(sql).executeQuery(sql);
					if (tableExists.next()) {
					    count = tableExists.getInt("count");
					}
					results.add(count != 0);
				    }
				}
				if (results.contains(false)) {
				    dependenciesCreated = false;
				    tries += 1;
				} else {
				    dependenciesCreated = true;
				}
				if (!dependenciesCreated) {
				    Thread.sleep(1000);
				}
			    } while (!dependenciesCreated && tries <= 3);

			    String tableStatement = TableCreator.getInstance().createStatement(table);
			    Connector.getInstance().sendRequest(tableStatement);
			    System.out.println(table.name() + " number of tries" + tries + "y boolean " + dependenciesCreated );
			}
		    } catch (SQLException | DataTypeException e) {
			System.out.println(
				String.format("el hilo con la tabla %s ha acabado con excepcion", table.name()));
			e.printStackTrace();
		    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    System.out.println(String.format("el hilo con la tabla %s ha acabado", table.name()));
		}
	    };
	    Thread thread = new Thread(runnable);
	    thread.setPriority(Thread.MAX_PRIORITY);
	    thread.setName(String.format("%s table creator", table.name()));
	    threads.add(thread);
	    thread.start();

	}
	while(!threads.isEmpty()) {
	    for(int i = 0; i < threads.size(); i++) {
		if(threads.get(i).getState().equals(State.TERMINATED)) {
		    threads.remove(i);
		}
	    }
	}
	var s = DatabaseStorage.getInstance();
	s.getTable(s.getClass());
    }

    private void sort(List<Class<?>> entities) {
	Collections.sort(entities, new Comparator<Class<?>>() {
	    @Override
	    public int compare(Class<?> c1, Class<?> c2) {
		// Obtener el número de campos de cada clase
		int fieldsCount1 = c1.getDeclaredFields().length;
		int fieldsCount2 = c2.getDeclaredFields().length;

		// Comparar los números de campos
		return Integer.compare(fieldsCount1, fieldsCount2);
	    }
	});
    }
}
