package kik.framework.vortex.database.mysql.storage;

import java.lang.Thread.State;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kik.framework.vortex.database.mysql.TableCreator;
import kik.framework.vortex.database.mysql.connector.Connector;
import kik.framework.vortex.databasemanager.annotation.ManyToMany;
import kik.framework.vortex.databasemanager.annotation.OneToMany;
import kik.framework.vortex.databasemanager.exception.DataTypeException;
import kik.framework.vortex.databasemanager.exception.RelationTypeException;
import kik.framework.vortex.databasemanager.storage.DBTable;
import kik.framework.vortex.databasemanager.storage.DatabaseStorage;
import kik.framework.vortex.databasemanager.storage.Relation;
import kik.framework.vortex.databasemanager.storage.StorageManager;
import vortex.annotate.components.Entity;
import vortex.annotate.manager.Storage;
import vortex.properties.kinds.Database;

public class Manager extends StorageManager {

    public Manager() throws SQLException, RelationTypeException {
	initialize(Storage.getInstance());
    }

    public Manager(Storage storage) throws SQLException, RelationTypeException {
	if(Database.Credentials.URL.value() != null) {
	initialize(storage);
	}
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
					return !r.type().equals(ManyToMany.class.getSimpleName()) &&!r.type().equals(OneToMany.class.getSimpleName());
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
			    } while (!dependenciesCreated && tries <= DatabaseStorage.getInstance().getAllTables().size() * 2);

			    String tableStatement = TableCreator.getInstance().createStatement(table);
			    Connector.getInstance().sendRequest(tableStatement);
			}
		    } catch (SQLException | DataTypeException e) {
			
			e.printStackTrace();
		    } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
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
  private void sortTables(List<DBTable> entities) {
	Collections.sort(entities, new Comparator<DBTable>() {

	    @Override
	    public int compare(DBTable table1, DBTable table2) {
		// TODO Auto-generated method stub
		return Integer.compare(table1.relations().size(), table2.relations().size());
	    }
	});
    }
}
