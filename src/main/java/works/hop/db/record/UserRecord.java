package works.hop.db.record;

import works.hop.db.api.DbConnect;
import works.hop.db.api.PgDbConnect;
import works.hop.db.api.Record;
import works.hop.db.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserRecord extends Record<User> {

    public final static String insertRecord = "insert into tbl_user (first_name, last_name, nick_name, user_account) values (?, ?, ?, ?);";
    public final static String updateRecord = "update tbl_user set first_name=?, last_name=?, nick_name=? where id=?";
    public final static String deleteRecord = "delete from tbl_user where id=?";
    public final static String selectRecordById = "select id, first_name, last_name, nick_name, user_account, date_created from tbl_user where id=?";
    public final static String selectRecordByEmail = "select u.id, u.first_name, u.last_name, u.nick_name, u.user_account, u.date_created from tbl_user u " +
            "inner join tbl_account a on u.user_account = a.id where a.email_address=?";
    public final static String selectRecordByUsername = "select u.id, u.first_name, u.last_name, u.nick_name, u.user_account, u.date_created from tbl_user u " +
            "inner join tbl_account a on u.user_account = a.id where a.user_name=?";
    public final static String fetchRecords = "select id, first_name, last_name, nick_name, user_account, date_created from tbl_user offset ? limit ?";
    public final static String clearRecords = "truncate table tbl_user";

    public UserRecord() {
        this(PgDbConnect.instance());
    }

    public UserRecord(DbConnect dbConnect) {
        super(User.class, dbConnect);
    }

    @Override
    public String tableName() {
        return "tbl_user";
    }

    @Override
    public String[] dropTable() {
        return new String[]{
                "drop table if exists tbl_user;"
        };
    }

    @Override
    public String[] createTable() {
        return new String[]{
                "create table if not exists tbl_user\n" +
                        "(\n" +
                        "    id           serial,\n" +
                        "    first_name   varchar(50),\n" +
                        "    last_name    varchar(50),\n" +
                        "    nick_name    varchar(50),\n" +
                        "    user_account int not null,\n" +
                        "    date_created timestamp default current_timestamp,\n" +
                        "    primary key (id),\n" +
                        "    foreign key (user_account) references tbl_account (id),\n" +
                        "    unique(nick_name)\n" +
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
    public void prepareInsert(PreparedStatement pst, User record) throws SQLException {
        pst.setString(1, record.firstName);
        pst.setString(2, record.lastName);
        pst.setString(3, record.nickName);
        pst.setLong(4, record.account);
    }

    @Override
    public User extractRecord(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new User(
                    rs.getLong("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("nick_name"),
                    rs.getLong("user_account"),
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
    public List<User> extractRecords(ResultSet rs) throws SQLException {
        List<User> records = new LinkedList<>();
        while (rs.next()) {
            records.add(new User(
                    rs.getLong("id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("nick_name"),
                    rs.getLong("user_account"),
                    new Date(rs.getTimestamp("date_created").getTime())));
        }
        return records;
    }

    @Override
    public void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString());
        pst.setString(2, params.get(2).toString());
        pst.setString(3, params.get(3).toString());
        pst.setLong(4, Long.valueOf(params.get(4).toString()));
    }

    @Override
    public void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString()));
    }
}
