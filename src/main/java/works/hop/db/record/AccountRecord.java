package works.hop.db.record;

import works.hop.db.api.DbConnector;
import works.hop.db.api.PgDbConnector;
import works.hop.db.api.Record;
import works.hop.db.entity.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AccountRecord extends Record<Account> {

    public final static String insertRecord = "insert into tbl_account (user_name, pass_code, email_address) values (?, ?, ?);";
    public final static String updateRecord = "update tbl_account set account_type=?::account_type where id=?";
    public final static String deleteRecord = "delete from tbl_account where id=?";
    public final static String selectRecordById = "select id, user_name, pass_code, email_address, account_type, date_created from tbl_account where id=?";
    public final static String selectRecordByEmail = "select id, user_name, pass_code, email_address, account_type, date_created from tbl_account where email_address=?";
    public final static String selectRecordByUsername = "select id, user_name, pass_code, email_address, account_type, date_created from tbl_account where user_name=?";
    public final static String fetchRecords = "select id, user_name, pass_code, email_address, account_type, date_created from tbl_account offset ? limit ?";
    public final static String clearRecords = "truncate table tbl_account";

    public AccountRecord() {
        this(PgDbConnector.instance());
    }

    public AccountRecord(DbConnector dbConnector) {
        super(Account.class, dbConnector);
    }

    @Override
    public String tableName() {
        return "tbl_account";
    }

    @Override
    public String[] dropTable() {
        return new String[]{
                "drop table if exists tbl_account;",
                "drop type if exists account_type;",
                "drop index if exists idx_account_emails;",
                "drop index if exists idx_account_users;"
        };
    }

    @Override
    public String[] createTable() {
        return new String[]{
                "create type account_type AS ENUM ('admin', 'user', 'guest');",
                "create table if not exists tbl_account\n" +
                        "(\n" +
                        "    id            serial,\n" +
                        "    user_name     varchar(50)  not null,\n" +
                        "    pass_code     varchar(512) not null,\n" +
                        "    email_address varchar(100) not null,\n" +
                        "    account_type account_type default 'user',\n" +
                        "    date_created  timestamp default current_timestamp,\n" +
                        "    primary key (id)\n" +
                        ");",
                "create index idx_account_users on tbl_account (user_name);",
                "create index idx_account_emails on tbl_account (email_address);"
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
    public void prepareInsert(PreparedStatement pst, Account record) throws SQLException {
        pst.setString(1, record.username);
        pst.setString(2, record.password);
        pst.setString(3, record.emailAddress);
    }

    @Override
    public Account extractRecord(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Account(
                    rs.getLong("id"),
                    rs.getString("user_name"),
                    rs.getString("pass_code"),
                    rs.getString("email_address"),
                    Account.Type.valueOf(rs.getString("account_type")),
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
    public List<Account> extractRecords(ResultSet rs) throws SQLException {
        List<Account> records = new LinkedList<>();
        while (rs.next()) {
            records.add(new Account(
                    rs.getLong("id"),
                    rs.getString("user_name"),
                    rs.getString("pass_code"),
                    rs.getString("email_address"),
                    Account.Type.valueOf(rs.getString("account_type")),
                    new Date(rs.getTimestamp("date_created").getTime())));
        }
        return records;
    }

    @Override
    public void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString());
        pst.setLong(2, Long.valueOf(params.get(2).toString()));
    }

    @Override
    public void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString()));
    }
}
