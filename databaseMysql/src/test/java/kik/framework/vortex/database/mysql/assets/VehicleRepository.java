package kik.framework.vortex.database.mysql.assets;

import kik.framework.vortex.database.mysq.connector.JPARepository;
import vortex.annotate.components.Repository;

@Repository
public class VehicleRepository extends JPARepository<Vehicle, String> {


}