package kik.framework.vortex.database.mysql;

import kik.framework.vortex.databasemanager.annotation.Column;
import kik.framework.vortex.databasemanager.annotation.ID;
import kik.framework.vortex.databasemanager.annotation.Nullable;
import kik.framework.vortex.databasemanager.annotation.Table;
import kik.framework.vortex.databasemanager.annotation.Unique;
import vortex.annotate.components.Entity;

@Entity
@Table("users")
public class User {
    @Nullable
    private String userId;
    @Unique
    @Column(name = "eda")
    private int edad;

    public User(String string, int i) {
	userId = string;
	edad = i;
    }

    public String getUserId() {
	return null;
    }

    public int getEdad() {
	return edad;
    }
}