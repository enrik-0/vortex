package kik.framework.vortex.database.mysql.assets;

import java.util.Map;

import kik.framework.vortex.database.mysq.connector.JPARepository;
import vortex.annotate.components.Repository;

@Repository
public class BookRepository extends JPARepository<Book, Map<String, Object>> {

}
