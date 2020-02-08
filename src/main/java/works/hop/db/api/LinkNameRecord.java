package works.hop.db.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LinkNameRecord extends Record<LinkName> {

    public final static String insertRecord = "insert into tbl_link_names (name, description) values (?, ?);";
    public final static String updateRecord = "update tbl_link_names set name=?, description=? where name=?";
    public final static String deleteRecord = "delete from tbl_link_names where name=?";
    public final static String selectRecord = "select name, description, date_created from tbl_link_names where name=?";
    public final static String fetchRecords = "select name, description, date_created from tbl_link_names offset ? limit ?";
    public final static String clearRecords = "truncate table tbl_link_names";

    public LinkNameRecord() {
        this(PgDbConnect.instance());
    }

    public LinkNameRecord(DbConnect dbConnect) {
        super(LinkName.class, dbConnect);
    }

    @Override
    public String tableName() {
        return "tbl_link_names";
    }

    @Override
    public String[] dropTable() {
        return new String[]{
                "drop table if exists tbl_link_names;"
        };
    }

    @Override
    public String[] createTable() {
        return new String[]{
                "create table if not exists tbl_link_names (\n" +
                        "    name varchar(64) not null,\n" +
                        "    description varchar(64),\n" +
                        "    date_created  timestamp    default current_timestamp,\n" +
                        "    primary key (name)\n" +
                        ");"
        };
    }

    @Override
    public void prepareCheck(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        //currently not in use
    }

    @Override
    public Long generatedKey(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public void prepareInsert(PreparedStatement pst, LinkName record) throws SQLException {
        pst.setString(1, record.name);
        pst.setString(2, record.description);
    }

    @Override
    public LinkName extractRecord(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new LinkName(
                    rs.getString("name"),
                    rs.getString("description"),
                    new Date(rs.getTimestamp("date_created").getTime())
            );
        }
        return null;
    }

    @Override
    public void prepareSelect(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString()); //name
    }

    @Override
    public void prepareSelectList(PreparedStatement pst, Map<Integer, Object> params, Integer offset, Integer limit) throws SQLException {
        pst.setInt(1, offset);
        pst.setInt(2, limit);
    }

    @Override
    public List<LinkName> extractRecords(ResultSet rs) throws SQLException {
        List<LinkName> records = new LinkedList<>();
        while (rs.next()) {
            records.add(new LinkName(
                    rs.getString("name"),
                    rs.getString("description"),
                    new Date(rs.getTimestamp("date_created").getTime())));
        }
        return records;
    }

    @Override
    public void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString()); //name
        pst.setString(2, params.get(2).toString()); //description
        pst.setString(3, params.get(3).toString()); //name
    }

    @Override
    public void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString()); //name
    }
}
