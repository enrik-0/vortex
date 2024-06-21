package kik.framework.vortex.database.mysql.assets;


import kik.framework.vortex.databasemanager.annotation.ID;
import kik.framework.vortex.databasemanager.annotation.ManyToOne;
import vortex.annotate.components.Entity;

@Entity
public class Book {

    @ID
    private String author;
    @ID
    private String identification;
    
    @ManyToOne
    private Library library;
    public Book(String name, String id, Library library) {
	author = name;
	identification = id;
	this.library = library;
    }
 public Book(String name, String id ) {
     this(name, id, null);

    }
    
}
