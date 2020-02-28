package works.hop.db.record;

import works.hop.db.api.DbConnector;
import works.hop.db.api.PgDbConnector;
import works.hop.db.api.Record;
import works.hop.db.entity.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LocationRecord extends Record<Location> {

    public final static String insertRecord = "insert into tbl_location (title, address) values (?, ?);";
    public final static String updateRecord = "update tbl_location set title=? where id=?";
    public final static String deleteRecord = "delete from tbl_location where id=?";
    public final static String selectRecord = "select id, title, address, date_created from tbl_location where id=?";
    public final static String fetchRecords = "select id, title, address, date_created from tbl_location offset ? limit ?";
    public final static String clearRecords = "truncate table tbl_location";

    public LocationRecord() {
        this(PgDbConnector.instance());
    }

    public LocationRecord(DbConnector dbConnector) {
        super(Location.class, dbConnector);
    }

    @Override
    public String tableName() {
        return "tbl_location";
    }

    @Override
    public String[] dropTable() {
        return new String[]{
                "drop table if exists tbl_location;"
        };
    }

    @Override
    public String[] createTable() {
        return new String[]{
                "create table if not exists tbl_location\n" +
                        "(\n" +
                        "    id           serial,\n" +
                        "    address      int         not null,\n" +
                        "    title        varchar(64) not null,\n" +
                        "    date_created timestamp default current_timestamp,\n" +
                        "    foreign key (address) references tbl_address (id),\n" +
                        "    primary key (id),\n" +
                        "    unique (address, title)\n" +
                        ");"
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
    public void prepareInsert(PreparedStatement pst, Location record) throws SQLException {
        pst.setString(1, record.title);
        pst.setLong(2, record.address);
    }

    @Override
    public Location extractRecord(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Location(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getLong("address"),
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
    public List<Location> extractRecords(ResultSet rs) throws SQLException {
        List<Location> records = new LinkedList<>();
        while (rs.next()) {
            records.add(new Location(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getLong("address"),
                    new Date(rs.getTimestamp("date_created").getTime())));
        }
        return records;
    }

    @Override
    public void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString());
        pst.setLong(2, Long.valueOf(params.get(4).toString()));
    }

    @Override
    public void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString()));
    }
}
