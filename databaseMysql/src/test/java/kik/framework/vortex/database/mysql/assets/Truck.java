package kik.framework.vortex.database.mysql.assets;

import kik.framework.vortex.databasemanager.annotation.OneToOne;
import vortex.annotate.components.Entity;

@Entity
public class Truck {
    
    
    @OneToOne
    private User user;

    public Truck(User user) {
	this.user = user;
    }

}
