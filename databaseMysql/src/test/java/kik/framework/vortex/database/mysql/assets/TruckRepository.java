package kik.framework.vortex.database.mysql.assets;

import kik.framework.vortex.database.mysq.connector.JPARepository;
import vortex.annotate.components.Repository;

@Repository
public class TruckRepository extends JPARepository<Truck, Long>{

}