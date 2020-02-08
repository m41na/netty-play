package works.hop.db.api;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class Record<T> implements DbTable<T> {

    public final Class<T> type;
    public final DbConnect dbc;

    public Record(Class<T> type, DbConnect dbc) {
        this.type = type;
        this.dbc = dbc;
    }

    @Override
    public void init() {
        try (Connection conn = dbc.connection()) {
            System.out.println("Connecting to database...");
            Statement stmt = conn.createStatement();
            for (String sql : createTable()) {
                int res = stmt.executeUpdate(sql);
                if (res > 0) {
                    System.out.printf("'%s' query executed\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        try (Connection conn = dbc.connection()) {
            System.out.println("Connecting to database...");
            Statement stmt = conn.createStatement();
            for (String sql : dropTable()) {
                int res = stmt.executeUpdate(sql);
                if (res > 0) {
                    System.out.printf("'%s' query executed\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long loadCsv(String source) {
        String query = String.format("COPY %s FROM STDIN DELIMITER ','", tableName());
        try (Connection conn = dbc.connection()) {
            long count = new CopyManager((BaseConnection) conn)
                    .copyIn(query, loadReader(source));
            System.out.printf("%d row(s) inserted%n", count);
            return count;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
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
        try (Connection conn = dbc.connection()) {
            System.out.println("Connecting to database...");
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Integer batchExecute(String[] queries, int batchSize) {
        try (Connection conn = dbc.connection()) {
            try (Statement pst = conn.createStatement()) {
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
                return count;
            } catch (SQLException e) {
                System.err.println("Could not execute batched queries");
                e.printStackTrace();
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    public abstract void prepareCheck(PreparedStatement pst, Map<Integer, Object> params) throws SQLException;

    @Override
    public Boolean exists(String query, Map<Integer, Object> params) {
        try (Connection conn = dbc.connection()) {
            try (PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                prepareCheck(pst, params);
                ResultSet rs = pst.executeQuery();
                if(rs.next()){
                    return rs.getInt(1) == 1;
                }
                return false;
            } catch (SQLException e) {
                System.err.println("Could not find existence of record using params: '" + params);
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Long insert(String query, T record) {
        try (Connection conn = dbc.connection()) {
            try (PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                prepareInsert(pst, record);
                int res = pst.executeUpdate();
                if (res > 0) {
                    try (ResultSet rs = pst.getGeneratedKeys()) {
                        return generatedKey(rs);
                    }
                }
                return null;
            } catch (SQLException e) {
                System.err.println("Could not insert new record: '" + record);
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Long> insert(String query, T record, Boolean hasKey) {
        if (hasKey) {
            return Optional.ofNullable(insert(query, record));
        } else {
            try (Connection conn = dbc.connection()) {
                try (PreparedStatement pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    prepareInsert(pst, record);
                    int res = pst.executeUpdate();
                    if (res > 0) {
                        System.out.printf("Created %d new record(s)%n", res);
                        return Optional.of(Long.valueOf(res));
                    }
                    return Optional.empty();
                } catch (SQLException e) {
                    System.err.println("Could not insert new record: '" + record);
                    e.printStackTrace();
                    return Optional.ofNullable(null);
                }
            } catch (SQLException e) {
                System.out.println("Could not connect to database...");
                e.printStackTrace();
                return Optional.ofNullable(null);
            }
        }
    }

    public abstract Long generatedKey(ResultSet rs) throws SQLException;

    public abstract void prepareInsert(PreparedStatement pst, T record) throws SQLException;

    @Override
    public Integer batchInsert(String query, int batchSize, T[] records) {
        try (Connection conn = dbc.connection()) {
            try (PreparedStatement pst = conn.prepareStatement(query)) {
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
                return count;
            } catch (SQLException e) {
                System.err.println("Could not insert new records batch");
                e.printStackTrace();
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T select(String query, Long id) {
        try (Connection conn = dbc.connection()) {
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setLong(1, id);
                try (ResultSet rs = pst.executeQuery()) {
                    return extractRecord(rs);
                }
            } catch (SQLException e) {
                System.err.println("Could not find record by id '" + id + "'");
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T select(String query, Map<Integer, Object> params) {
        try (Connection conn = dbc.connection()) {
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                prepareSelect(pst, params);
                try (ResultSet rs = pst.executeQuery()) {
                    return extractRecord(rs);
                }
            } catch (SQLException e) {
                System.err.println("Could not find record with params '" + params + "'");
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    public T select(String query, Consumer<PreparedStatement> prepare) {
        try (Connection conn = dbc.connection()) {
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                prepare.accept(pst);
                try (ResultSet rs = pst.executeQuery()) {
                    return extractRecord(rs);
                }
            } catch (SQLException e) {
                System.err.println("Could not find record by query '" + query + "'");
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    public abstract void prepareSelect(PreparedStatement pst, Map<Integer, Object> params) throws SQLException;

    public abstract T extractRecord(ResultSet rs) throws SQLException;

    @Override
    public List<T> selectList(String query, Map<Integer, Object> params, Integer offset, Integer limit) {
        try (Connection conn = dbc.connection()) {
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                prepareSelectList(pst, params, offset, limit);
                try (ResultSet rs = pst.executeQuery()) {
                    return extractRecords(rs);
                }
            } catch (SQLException e) {
                System.err.println("Could not fetch records from offset '" + offset + "'");
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> selectList(String query, Consumer<PreparedStatement> prepare) {
        try (Connection conn = dbc.connection()) {
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                prepare.accept(pst);
                try (ResultSet rs = pst.executeQuery()) {
                    return extractRecords(rs);
                }
            } catch (SQLException e) {
                System.err.println("Could not fetch records using query '" + query + "'");
                e.printStackTrace();
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    public abstract void prepareSelectList(PreparedStatement pst, Map<Integer, Object> params, Integer offset, Integer limit) throws SQLException;

    public abstract List<T> extractRecords(ResultSet rs) throws SQLException;

    @Override
    public Integer update(String query, Map<Integer, Object> params) {
        try (Connection conn = dbc.connection()) {
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                prepareUpdate(pst, params);
                return pst.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Could not update record with params '" + params + "'");
                e.printStackTrace();
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    public abstract void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException;

    @Override
    public Integer update(String query, Consumer<PreparedStatement> prepare) {
        try (Connection conn = dbc.connection()) {
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                prepare.accept(pst);
                return pst.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Could not update record with query '" + query + "'");
                e.printStackTrace();
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer delete(String query, Long id) {
        try (Connection conn = dbc.connection()) {
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setLong(1, id);
                return pst.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Could not remove record by id '" + id + "'");
                e.printStackTrace();
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer delete(String query, Map<Integer, Object> params) {
        try (Connection conn = dbc.connection()) {
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                prepareDelete(pst, params);
                return pst.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Could not remove record with params '" + params + "'");
                e.printStackTrace();
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    public abstract void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException;

    @Override
    public Integer truncate(String query) {
        try (Connection conn = dbc.connection()) {
            try (Statement pst = conn.createStatement()) {
                return pst.executeUpdate(query);
            } catch (SQLException e) {
                System.err.println("Could not truncate table " + tableName());
                e.printStackTrace();
                return 0;
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to database...");
            e.printStackTrace();
            return null;
        }
    }

    protected InputStreamReader loadReader(String source){
        InputStream is = Record.class.getResourceAsStream(source);
        if(is == null){
            is = Record.class.getClassLoader().getResourceAsStream(source);
        }
        System.out.println((is == null? "Could not load " : " Loaded ") + "file : '" + source + "'");
        return new InputStreamReader(is);
    }
}
