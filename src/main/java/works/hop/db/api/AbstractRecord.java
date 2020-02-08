package works.hop.db.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class AbstractRecord<T> extends Record<T> {

    public AbstractRecord(Class<T> type, DbConnect dbc) {
        super(type, dbc);
    }

    @Override
    public void prepareCheck(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        //override in subclass
    }

    @Override
    public Long generatedKey(ResultSet rs) throws SQLException {
        //override in subclass
        return null;
    }

    @Override
    public void prepareInsert(PreparedStatement pst, T record) throws SQLException {
        //override in subclass
    }

    @Override
    public void prepareSelect(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        //override in subclass
    }

    @Override
    public T extractRecord(ResultSet rs) throws SQLException {
        //override in subclass
        return null;
    }

    @Override
    public void prepareSelectList(PreparedStatement pst, Map<Integer, Object> params, Integer offset, Integer limit) throws SQLException {
        //override in subclass
    }

    @Override
    public List<T> extractRecords(ResultSet rs) throws SQLException {
        //override in subclass
        return null;
    }

    @Override
    public void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        //override in subclass
    }

    @Override
    public void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        //override in subclass
    }

    @Override
    public String tableName() {
        //override in subclass
        return null;
    }

    @Override
    public String[] dropTable() {
        //override in subclass
        return new String[0];
    }

    @Override
    public String[] createTable() {
        //override in subclass
        return new String[0];
    }
}
