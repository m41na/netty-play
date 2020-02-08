package works.hop.db.record;

import works.hop.db.api.DbConnect;
import works.hop.db.api.PgDbConnect;
import works.hop.db.api.Record;
import works.hop.db.entity.Address;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AddressRecord extends Record<Address> {

    public final static String insertRecord = "insert into tbl_address (street, unit, city, state) values (?, ?, ?, ?);";
    public final static String updateRecord = "update tbl_address set street=?, unit=?, city=?, state=? where id=?";
    public final static String deleteRecord = "delete from tbl_address where id=?";
    public final static String selectRecord = "select id, street, unit, city, state, date_created from tbl_address where id=?";
    public final static String fetchRecords = "select id, street, unit, city, state, date_created from tbl_address offset ? limit ?";
    public final static String clearRecords = "truncate table tbl_address";

    public AddressRecord() {
        this(PgDbConnect.instance());
    }

    public AddressRecord(DbConnect dbConnect) {
        super(Address.class, dbConnect);
    }

    @Override
    public String tableName() {
        return "tbl_address";
    }

    @Override
    public String[] dropTable() {
        return new String[]{
                "drop table if exists tbl_address;"
        };
    }

    @Override
    public String[] createTable() {
        return new String[]{
                "create table if not exists tbl_address\n" +
                        "(\n" +
                        "    id     serial,\n" +
                        "    street varchar(50),\n" +
                        "    unit   varchar(50),\n" +
                        "    city   varchar(50) not null,\n" +
                        "    state  varchar(3)  not null,\n" +
                        "    date_created timestamp default current_timestamp,\n" +
                        "    primary key (id),\n" +
                        "    unique (city, state)\n" +
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
    public void prepareInsert(PreparedStatement pst, Address record) throws SQLException {
        pst.setString(1, record.street);
        pst.setString(2, record.unit);
        pst.setString(3, record.city);
        pst.setString(4, record.state);
    }

    @Override
    public Address extractRecord(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Address(
                    rs.getLong("id"),
                    rs.getString("street"),
                    rs.getString("unit"),
                    rs.getString("city"),
                    rs.getString("state"),
                    new Date(rs.getTimestamp("date_created").getTime())
            );
        }
        return null;
    }

    @Override
    public void prepareSelect(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString()));
    }

    @Override
    public void prepareSelectList(PreparedStatement pst, Map<Integer, Object> params, Integer offset, Integer limit) throws SQLException {
        pst.setInt(1, offset);
        pst.setInt(2, limit);
    }

    @Override
    public List<Address> extractRecords(ResultSet rs) throws SQLException {
        List<Address> records = new LinkedList<>();
        while (rs.next()) {
            records.add(new Address(
                    rs.getLong("id"),
                    rs.getString("street"),
                    rs.getString("unit"),
                    rs.getString("city"),
                    rs.getString("state"),
                    new Date(rs.getTimestamp("date_created").getTime())));
        }
        return records;
    }

    @Override
    public void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString()); //street
        pst.setString(2, params.get(2).toString()); //unit
        pst.setString(3, params.get(3).toString()); //city
        pst.setString(4, params.get(4).toString()); //state
        pst.setLong(5, Long.valueOf(params.get(5).toString()));
    }

    @Override
    public void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString())); //id
    }
}
