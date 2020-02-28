package works.hop.db.record;

import works.hop.db.api.DbConnector;
import works.hop.db.api.PgDbConnector;
import works.hop.db.api.Record;
import works.hop.db.entity.EventEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventEntityRecord extends Record<EventEntity> {

    public final static String insertRecord = "insert into tbl_events_log (id, name, sender, version, body) values (?, ?, ?, ?, ?);";
    public final static String selectRecordById = "select id, name, sender, version, body, date_created from tbl_events_log where id=?";
    public final static String selectRecordByDateRange = "select id, name, sender, version, body, date_created from tbl_events_log where date_created => ? and date_created <= ??";
    public final static String selectRecordBySender = "select id, name, sender, version, body, date_created from tbl_events_log where sender=?";
    public final static String fetchRecords = "select id, name, sender, version, body, date_created from tbl_events_log offset ? limit ?";
    public final static String clearRecords = "truncate table tbl_events_log";

    public EventEntityRecord() {
        this(PgDbConnector.instance());
    }

    public EventEntityRecord(DbConnector dbConnector) {
        super(EventEntity.class, dbConnector);
    }

    @Override
    public String tableName() {
        return "tbl_events_log";
    }

    @Override
    public String[] dropTable() {
        return new String[]{
                "drop table if exists tbl_events_log;",
        };
    }

    @Override
    public String[] createTable() {
        return new String[]{
                "create table if not exists tbl_events_log (\n" +
                        "    id varchar(128) not null,\n" +
                        "    name varchar(128) not null,\n" +
                        "    sender varchar(128) not null,\n" +
                        "    version char(8) not null,\n" +
                        "    body text not null,\n" +
                        "    date_created timestamp default current_timestamp\n" +
                        ")"
        };
    }

    @Override
    public void prepareCheck(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        //currently not in use
    }

    @Override
    public Long generatedKey(ResultSet rs) throws SQLException {
        if (rs != null && rs.next()) {
            return rs.getLong("id");
        }
        return null;
    }

    @Override
    public void prepareInsert(PreparedStatement pst, EventEntity record) throws SQLException {
        pst.setString(1, record.id);
        pst.setString(2, record.name);
        pst.setString(3, record.sender);
        pst.setString(4, record.version);
        pst.setString(5, record.body.toString());
    }

    @Override
    public EventEntity extractRecord(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new EventEntity(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("sender"),
                    rs.getString("version"),
                    rs.getString("body"),
                    new Date(rs.getTimestamp("date_created").getTime())
            );
        }
        return null;
    }

    @Override
    public void prepareSelect(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString());
    }

    @Override
    public void prepareSelectList(PreparedStatement pst, Map<Integer, Object> params, Integer offset, Integer limit) throws SQLException {
        pst.setInt(1, offset);
        pst.setInt(2, limit);
    }

    @Override
    public List<EventEntity> extractRecords(ResultSet rs) throws SQLException {
        List<EventEntity> records = new LinkedList<>();
        while (rs.next()) {
            records.add(new EventEntity(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("sender"),
                    rs.getString("version"),
                    rs.getString("body"),
                    new Date(rs.getTimestamp("date_created").getTime())));
        }
        return records;
    }

    @Override
    public void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        throw new UnsupportedOperationException("This operation is not permitted here");
    }

    @Override
    public void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        throw new UnsupportedOperationException("This operation is not permitted here");
    }
}
