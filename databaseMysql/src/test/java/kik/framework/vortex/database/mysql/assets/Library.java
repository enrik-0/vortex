package kik.framework.vortex.database.mysql.assets;

import java.util.ArrayList;
import java.util.List;

import kik.framework.vortex.databasemanager.annotation.OneToMany;
import vortex.annotate.components.Entity;

@Entity
public class Library {
    private String name;
    private String street;

    private long idLibrary;
    
    @OneToMany
    private List<Book> books;
    public Library(String name, String street, long id,List<Book> books) {
	this.name = name;
	this.street = street;
	this.idLibrary = id;
	this.books = books;
    }
    public Library set(String name) {
	this.name = name;
	return this;
    }
public Library(String name, String street, long id) {
    this(name, street, id,new ArrayList<>());
    }

}
