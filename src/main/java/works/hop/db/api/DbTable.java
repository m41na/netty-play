package works.hop.db.api;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface DbTable<T> {

    void init();

    void destroy();

    String tableName();

    String[] dropTable();

    String[] createTable();

    Long loadCsv(String source);

    Integer loadSql(String source, Integer batchSize);

    Integer execute(String query);

    Integer batchExecute(String[] queries, int batchSize);

    Boolean exists(String query, Map<Integer, Object> params);

    Long insert(String query, T record);

    Optional<Long> insert(String query, T record, Boolean hasKey);

    Integer batchInsert(String query, int batchSize, T[] records);

    T select(String query, Long key);

    T select(String query, Map<Integer, Object> params);

    T select(String query, Consumer<PreparedStatement> prepare);

    List<T> selectList(String query, Map<Integer, Object> params, Integer offset, Integer limit);

    List<T> selectList(String query, Consumer<PreparedStatement> prepare);

    Integer update(String query, Map<Integer, Object> params);

    Integer update(String query, Consumer<PreparedStatement> prepare);

    Integer delete(String query, Long key);

    Integer delete(String query, Map<Integer, Object> params);

    Integer truncate(String query);
}
