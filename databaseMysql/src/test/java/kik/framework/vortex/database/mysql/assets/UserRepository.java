package kik.framework.vortex.database.mysql.assets;

import java.util.Map;

import kik.framework.vortex.database.mysq.connector.JPARepository;
import vortex.annotate.components.Repository;

@Repository
public class UserRepository extends JPARepository<User, Map<String, Object>>{

}
