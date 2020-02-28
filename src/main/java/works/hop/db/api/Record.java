package works.hop.db.api;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import works.hop.db.api.DbConnector.DbConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Record<T> implements DbTable<T> {

    public final Class<T> type;
    public final DbConnector dbc;
    public final Supplier<DbConnection> connections;

    public Record(Class<T> type, DbConnector dbc) {
        this.type = type;
        this.dbc = dbc;
        this.connections = () -> dbc.connection();
    }

    @Override
    public void init() {
        DbConnection db = connections.get();
        try (Statement stmt = db.connect().createStatement()) {
            for (String sql : createTable()) {
                int res = stmt.executeUpdate(sql);
                if (res > 0) {
                    System.out.printf("'%s' query executed\n");
                }
            }
            db.commit();
        } catch (SQLException e) {
            db.rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            db.close();
        }
    }

    @Override
    public void destroy() {
        DbConnection conn = connections.get();
        try (Statement stmt = conn.connect().createStatement()) {
            for (String sql : dropTable()) {
                int res = stmt.executeUpdate(sql);
                if (res > 0) {
                    System.out.printf("'%s' query executed\n");
                }
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            conn.close();
        }
    }

    @Override
    public Long loadCsv(String source) {
        String query = String.format("COPY %s FROM STDIN DELIMITER ','", tableName());
        DbConnection conn = connections.get();
        try {
            long count = new CopyManager((BaseConnection) conn)
                    .copyIn(query, loadReader(source));
            System.out.printf("%d row(s) inserted%n", count);
            conn.commit();
            return count;
        } catch (SQLException | IOException e) {
            conn.rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            conn.close();
        }
    }

    @Override
    public Integer loadSql(String source, Integer batchSize) {
        StringBuilder builder = new StringBuilder();
        List<String> queries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(loadReader(source))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.endsWith(";")) {
                    builder.append(line);
                    queries.add(builder.toString());
                    builder.delete(0, builder.length());
                } else if (line.startsWith("--") || line.startsWith("//")) {
                    continue;
                } else {
                    builder.append(line + " "); //add space for new lines
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return batchExecute(queries.toArray(new String[queries.size()]), batchSize);
    }

    @Override
    public Integer execute(String query) {
        DbConnection conn = connections.get();
        try (Statement stmt = conn.connect().createStatement()) {
            int result = stmt.executeUpdate(query);
            conn.commit();
            return result;
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            conn.close();
        }
    }

    @Override
    public Integer batchExecute(String[] queries, int batchSize) {
        DbConnection conn = connections.get();
        try (Statement pst = conn.connect().createStatement()) {
            Integer count = 0;
            for (int i = 0; i < queries.length; i += batchSize) {
                //create batch
                for (int j = 0; j < batchSize && (i + j) < queries.length; j++) {
                    pst.addBatch(queries[i + j]);
                }
                int[] batched = pst.executeBatch();
                count += batched.length;
                System.out.println("batch size: " + batchSize + ". batch success: " + batched.length);
            }
            pst.clearBatch();
            conn.commit();
            return count;
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not execute batched queries");
            e.printStackTrace();
            return 0;
        } finally {
            conn.close();
        }
    }

    public abstract void prepareCheck(PreparedStatement pst, Map<Integer, Object> params) throws SQLException;

    @Override
    public Boolean exists(String query, Map<Integer, Object> params) {
        DbConnection conn = connections.get();
        try (PreparedStatement pst = conn.connect().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            prepareCheck(pst, params);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 1;
            }
            conn.commit();
            return false;
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not find existence of record using params: '" + params);
            e.printStackTrace();
            return false;
        } finally {
            conn.close();
        }
    }

    @Override
    public Long insert(String query, T record) {
        DbConnection conn = connections.get();
        try (PreparedStatement pst = conn.connect().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            Long generatedKey = null;
            prepareInsert(pst, record);
            int res = pst.executeUpdate();
            if (res > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    generatedKey = generatedKey(rs);
                }
            }
            conn.commit();
            return generatedKey;
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not insert new record: '" + record);
            e.printStackTrace();
            return null;
        } finally {
            conn.close();
        }
    }

    @Override
    public Optional<Long> insert(String query, T record, Boolean hasKey) {
        if (hasKey) {
            return Optional.ofNullable(insert(query, record));
        } else {
            DbConnection conn = connections.get();
            try (PreparedStatement pst = conn.connect().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                prepareInsert(pst, record);
                int res = pst.executeUpdate();
                conn.commit();
                if (res > 0) {
                    System.out.printf("Created %d new record(s)%n", res);
                    return Optional.of(Long.valueOf(res));
                }
                return Optional.empty();
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Could not insert new record: '" + record);
                e.printStackTrace();
                return Optional.ofNullable(null);
            } finally {
                conn.close();
            }
        }
    }

    public abstract Long generatedKey(ResultSet rs) throws SQLException;

    public abstract void prepareInsert(PreparedStatement pst, T record) throws SQLException;

    @Override
    public Integer batchInsert(String query, int batchSize, T[] records) {
        DbConnection conn = connections.get();
        try (PreparedStatement pst = conn.connect().prepareStatement(query)) {
            Integer count = 0;
            for (int i = 0; i < records.length; i += batchSize) {
                //create batch
                for (int j = 0; j < batchSize && (i + j) < records.length; j++) {
                    prepareInsert(pst, records[i + j]);
                    pst.addBatch();
                }
                pst.clearParameters();
                int[] batched = pst.executeBatch();
                count += batched.length;
                System.out.println("batch size: " + batchSize + ". batch success: " + batched.length);
            }
            pst.clearBatch();
            conn.commit();
            return count;
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not insert new records batch");
            e.printStackTrace();
            return 0;
        } finally {
            conn.close();
        }
    }

    @Override
    public T select(String query, Long id) {
        DbConnection conn = connections.get();
        try (PreparedStatement pst = conn.connect().prepareStatement(query)) {
            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                T selected = extractRecord(rs);
                conn.commit();
                return selected;
            }
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not find record by id '" + id + "'");
            e.printStackTrace();
            return null;
        } finally {
            conn.close();
        }
    }

    @Override
    public T select(String query, Map<Integer, Object> params) {
        DbConnection conn = connections.get();
        try (PreparedStatement pst = conn.connect().prepareStatement(query)) {
            prepareSelect(pst, params);
            try (ResultSet rs = pst.executeQuery()) {
                T selected = extractRecord(rs);
                conn.commit();
                return selected;
            }
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not find record with params '" + params + "'");
            e.printStackTrace();
            return null;
        } finally {
            conn.close();
        }
    }

    public T select(String query, Consumer<PreparedStatement> prepare) {
        DbConnection conn = connections.get();
        try (PreparedStatement pst = conn.connect().prepareStatement(query)) {
            prepare.accept(pst);
            try (ResultSet rs = pst.executeQuery()) {
                T selected = extractRecord(rs);
                conn.commit();
                return selected;
            }
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not find record by query '" + query + "'");
            e.printStackTrace();
            return null;
        } finally {
            conn.close();
        }
    }

    public abstract void prepareSelect(PreparedStatement pst, Map<Integer, Object> params) throws SQLException;

    public abstract T extractRecord(ResultSet rs) throws SQLException;

    @Override
    public List<T> selectList(String query, Map<Integer, Object> params, Integer offset, Integer limit) {
        DbConnection conn = connections.get();
        try (PreparedStatement pst = conn.connect().prepareStatement(query)) {
            prepareSelectList(pst, params, offset, limit);
            try (ResultSet rs = pst.executeQuery()) {
                List<T> selected = extractRecords(rs);
                conn.commit();
                return selected;
            }
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not fetch records from offset '" + offset + "'");
            e.printStackTrace();
            return null;
        } finally {
            conn.close();
        }
    }

    @Override
    public List<T> selectList(String query, Consumer<PreparedStatement> prepare) {
        DbConnection conn = connections.get();
        try (PreparedStatement pst = conn.connect().prepareStatement(query)) {
            prepare.accept(pst);
            try (ResultSet rs = pst.executeQuery()) {
                List<T> selected = extractRecords(rs);
                conn.commit();
                return selected;
            }
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not fetch records using query '" + query + "'");
            e.printStackTrace();
            return null;
        } finally {
            conn.close();
        }
    }

    public abstract void prepareSelectList(PreparedStatement pst, Map<Integer, Object> params, Integer offset, Integer limit) throws SQLException;

    public abstract List<T> extractRecords(ResultSet rs) throws SQLException;

    @Override
    public Integer update(String query, Map<Integer, Object> params) {
        DbConnection conn = connections.get();
        try (PreparedStatement pst = conn.connect().prepareStatement(query)) {
            prepareUpdate(pst, params);
            int updated = pst.executeUpdate();
            conn.commit();
            return updated;
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not update record with params '" + params + "'");
            e.printStackTrace();
            return 0;
        } finally {
            conn.commit();
        }
    }

    public abstract void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException;

    @Override
    public Integer update(String query, Consumer<PreparedStatement> prepare) {
        DbConnection conn = connections.get();
        try (PreparedStatement pst = conn.connect().prepareStatement(query)) {
            prepare.accept(pst);
            int updated = pst.executeUpdate();
            conn.commit();
            return updated;
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not update record with query '" + query + "'");
            e.printStackTrace();
            return 0;
        } finally {
            conn.close();
        }
    }

    @Override
    public Integer delete(String query, Long id) {
        DbConnection conn = connections.get();
        try (PreparedStatement pst = conn.connect().prepareStatement(query)) {
            pst.setLong(1, id);
            int deleted = pst.executeUpdate();
            conn.commit();
            return deleted;
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not remove record by id '" + id + "'");
            e.printStackTrace();
            return 0;
        } finally {
            conn.close();
        }
    }

    @Override
    public Integer delete(String query, Map<Integer, Object> params) {
        DbConnection conn = connections.get();
        try (PreparedStatement pst = conn.connect().prepareStatement(query)) {
            prepareDelete(pst, params);
            int deleted = pst.executeUpdate();
            conn.commit();
            return deleted;
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not remove record with params '" + params + "'");
            e.printStackTrace();
            return 0;
        } finally {
            conn.close();
        }
    }

    public abstract void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException;

    @Override
    public Integer truncate(String query) {
        DbConnection conn = connections.get();
        try (Statement pst = conn.connect().createStatement()) {
            int result = pst.executeUpdate(query);
            conn.commit();
            return result;
        } catch (SQLException e) {
            conn.rollback();
            System.err.println("Could not truncate table " + tableName());
            e.printStackTrace();
            return 0;
        } finally {
            conn.close();
        }
    }

    protected InputStreamReader loadReader(String source) {
        InputStream is = Record.class.getResourceAsStream(source);
        if (is == null) {
            is = Record.class.getClassLoader().getResourceAsStream(source);
        }
        System.out.println((is == null ? "Could not load " : " Loaded ") + "file : '" + source + "'");
        return new InputStreamReader(is);
    }
}
