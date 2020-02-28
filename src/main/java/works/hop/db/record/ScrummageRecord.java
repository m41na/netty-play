package works.hop.db.record;

import works.hop.db.api.DbConnector;
import works.hop.db.api.PgDbConnector;
import works.hop.db.api.Record;
import works.hop.db.entity.CardDeck;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ScrummageRecord extends Record<CardDeck> {

    public final static String insertRecord = "insert into tbl_card_deck (name, values) values (?, ?);";
    public final static String updateRecord = "update tbl_card_deck set name=?, values=? where id=?";
    public final static String deleteRecord = "delete from tbl_card_deck where name=?";
    public final static String selectRecordById = "select id, name, values, date_created from tbl_card_deck where id=?";
    public final static String selectRecordByName = "select id, name, values, date_created from tbl_card_deck where name=?";
    public final static String fetchRecords = "select id, name, values, date_created from tbl_card_deck offset ? limit ?";
    public final static String clearRecords = "truncate table tbl_card_deck";

    public ScrummageRecord() {
        this(PgDbConnector.instance());
    }

    public ScrummageRecord(DbConnector dbConnector) {
        super(CardDeck.class, dbConnector);
    }

    @Override
    public String tableName() {
        return "tbl_card_deck";
    }

    @Override
    public String[] dropTable() {
        return new String[]{
                "drop table if exists tbl_card_deck;",
        };
    }

    @Override
    public String[] createTable() {
        return new String[]{
                "create table if not exists tbl_card_deck\n" +
                        "(\n" +
                        "    id           serial,\n" +
                        "    name         varchar(32) not null,\n" +
                        "    values       varchar(16)[] not null,\n" +
                        "    date_created timestamp default current_timestamp,\n" +
                        "    primary key (id)\n" +
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
    public void prepareInsert(PreparedStatement pst, CardDeck record) throws SQLException {
        pst.setString(1, record.name);
        pst.setArray(2, pst.getConnection().createArrayOf("varchar", record.values));
    }

    @Override
    public CardDeck extractRecord(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new CardDeck(
                    rs.getLong("id"),
                    rs.getString("name"),
                    (String[])rs.getArray("values").getArray(),
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
    public List<CardDeck> extractRecords(ResultSet rs) throws SQLException {
        List<CardDeck> records = new LinkedList<>();
        while (rs.next()) {
            records.add(new CardDeck(
                    rs.getLong("id"),
                    rs.getString("name"),
                    (String[])rs.getArray("values").getArray(),
                    new Date(rs.getTimestamp("date_created").getTime())));
        }
        return records;
    }

    @Override
    public void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString());
        pst.setArray(2, pst.getConnection().createArrayOf("varchar", (String[])params.get(2)));
        pst.setLong(3, Long.valueOf(params.get(3).toString()));
    }

    @Override
    public void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString()));
    }
}
