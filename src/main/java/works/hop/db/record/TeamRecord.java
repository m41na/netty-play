package works.hop.db.record;

import works.hop.db.api.DbConnector;
import works.hop.db.api.PgDbConnector;
import works.hop.db.api.Record;
import works.hop.db.entity.Team;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TeamRecord extends Record<Team> {

    public final static String insertRecord = "insert into tbl_team (title, captain, location) values (?, ?, ?);";
    public final static String updateRecord = "update tbl_team set title=?, captain=? where id=?";
    public final static String deleteRecord = "delete from tbl_team where id=?";
    public final static String selectRecord = "select id, title, captain, location, date_created from tbl_team where id=?";
    public final static String fetchRecords = "select id, title, captain, location, date_created from tbl_team offset ? limit ?";
    public final static String clearRecords = "truncate table tbl_team";

    public TeamRecord() {
        this(PgDbConnector.instance());
    }

    public TeamRecord(DbConnector dbConnector) {
        super(Team.class, dbConnector);
    }

    @Override
    public String tableName() {
        return "tbl_team";
    }

    @Override
    public String[] dropTable() {
        return new String[]{
                "drop table if exists tbl_team;"
        };
    }

    @Override
    public String[] createTable() {
        return new String[]{
                "create table if not exists tbl_team\n" +
                        "(\n" +
                        "    id           serial,\n" +
                        "    title        varchar(64) not null,\n" +
                        "    captain      int         not null,\n" +
                        "    location     int         not null,\n" +
                        "    date_created timestamp default current_timestamp,\n" +
                        "    primary key (id),\n" +
                        "    foreign key (captain) references tbl_user (id),\n" +
                        "    foreign key (location) references tbl_location (id)\n" +
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
    public void prepareInsert(PreparedStatement pst, Team record) throws SQLException {
        pst.setString(1, record.title);
        pst.setLong(2, record.captain);
        pst.setLong(3, record.location);
    }

    @Override
    public Team extractRecord(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Team(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getLong("captain"),
                    rs.getLong("location"),
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
    public List<Team> extractRecords(ResultSet rs) throws SQLException {
        List<Team> records = new LinkedList<>();
        while (rs.next()) {
            records.add(new Team(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getLong("captain"),
                    rs.getLong("location"),
                    new Date(rs.getTimestamp("date_created").getTime())));
        }
        return records;
    }

    @Override
    public void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString()); //title
        pst.setLong(2, Long.valueOf(params.get(2).toString())); //captain
        pst.setLong(3, Long.valueOf(params.get(2).toString())); //location
    }

    @Override
    public void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString()));
    }
}
