package works.hop.db.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AdjacencyLinkRecord extends Record<AdjacencyLink> {

    public final static String insertRecord = "insert into tbl_adjacency_links (table_from, column_from, value_from, table_to, column_to, value_to, link_name) values (?, ?, ?, ?, ?, ?, ?);";
    public final static String updateRecord = "update tbl_adjacency_links set table_from=?, column_from=?, value_from=?, table_to=?, column_to=?, value_to=?, link_name=? where " +
            "table_from=? and column_from=? and value_from=? and table_to=? and column_to=? and value_to=? and link_name=?";
    public final static String deleteRecord = "delete from tbl_adjacency_links where table_from=? and column_from=? and value_from=? and table_to=? and column_to=? and value_to=? and link_name=?";
    public final static String checkRecordExists = "select 1 from tbl_adjacency_links where table_from=? and column_from=? and value_from=? and table_to=? and column_to=? and value_to=? and link_name=? limit 1";
    public final static String fetchLinksOut = "select table_from, column_from, value_from, table_to, column_to, value_to, link_name from tbl_adjacency_links where table_from=? and column_from=? and link_name=? offset ? limit ?";
    public final static String fetchLinksIn = "select table_from, column_from, value_from, table_to, column_to, value_to, link_name from tbl_adjacency_links where table_to=? and column_to=? and link_name=? offset ? limit ?";
    public final static String fetchLinksByName = "select table_from, column_from, value_from, table_to, column_to, value_to, link_name from tbl_adjacency_links where link_name=? offset ? limit ?";
    public final static String clearRecords = "truncate table tbl_adjacency_links";

    public AdjacencyLinkRecord() {
        this(PgDbConnect.instance());
    }

    public AdjacencyLinkRecord(DbConnect dbConnect) {
        super(AdjacencyLink.class, dbConnect);
    }

    @Override
    public String tableName() {
        return "tbl_adjacency_links";
    }

    @Override
    public String[] dropTable() {
        return new String[]{
                "drop table if exists tbl_adjacency_links;"
        };
    }

    @Override
    public String[] createTable() {
        return new String[]{
                "create table if not exists tbl_adjacency_links\n" +
                        "(\n" +
                        "    table_from  varchar(64) not null,\n" +
                        "    column_from varchar(64) not null,\n" +
                        "    value_from  int         not null,\n" +
                        "    table_to    varchar(64) not null,\n" +
                        "    column_to   varchar(64) not null,\n" +
                        "    value_to    int         not null,\n" +
                        "    link_name   varchar(64) not null,\n" +
                        "    foreign key (link_name) references tbl_link_names (name),\n" +
                        "    unique (table_from, column_from, value_from, table_to, column_to, value_to, link_name)\n" +
                        ");"
        };
    }

    @Override
    public void prepareCheck(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        prepareQueryParams(pst, params);
    }

    @Override
    public Long generatedKey(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public void prepareInsert(PreparedStatement pst, AdjacencyLink record) throws SQLException {
        pst.setString(1, record.from.table);
        pst.setString(2, record.from.column);
        pst.setLong(3, record.from.value);
        pst.setString(4, record.to.table);
        pst.setString(5, record.to.column);
        pst.setLong(6, record.to.value);
        pst.setString(7, record.linkName);
    }

    @Override
    public AdjacencyLink extractRecord(ResultSet rs) throws SQLException {
        if (rs.next()) {
            //table_from, column_from, table_to, column_to, link_name, value_to
            return new AdjacencyLink(
                    rs.getString("table_from"),
                    rs.getString("column_from"),
                    rs.getLong("value_from"),
                    rs.getString("table_to"),
                    rs.getString("column_to"),
                    rs.getLong("value_to"),
                    rs.getString("link_name")
            );
        }
        return null;
    }

    @Override
    public void prepareSelect(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        //no in use
    }

    @Override
    public void prepareSelectList(PreparedStatement pst, Map<Integer, Object> params, Integer offset, Integer limit) throws SQLException {
        //not in use
    }

    @Override
    public List<AdjacencyLink> extractRecords(ResultSet rs) throws SQLException {
        List<AdjacencyLink> records = new LinkedList<>();
        while (rs.next()) {
            records.add(new AdjacencyLink(
                    rs.getString("table_from"),
                    rs.getString("column_from"),
                    rs.getLong("value_from"),
                    rs.getString("table_to"),
                    rs.getString("column_to"),
                    rs.getLong("value_to"),
                    rs.getString("link_name")));
        }
        return records;
    }

    @Override
    public void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        prepareQueryParams(pst, params);
        pst.setString(8, params.get(8).toString());
        pst.setString(9, params.get(9).toString());
        pst.setLong(10, Long.valueOf(params.get(10).toString()));
        pst.setString(11, params.get(11).toString());
        pst.setString(12, params.get(12).toString());
        pst.setLong(13, Long.valueOf(params.get(13).toString()));
        pst.setString(14, params.get(14).toString());
    }

    private void prepareQueryParams(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString());
        pst.setString(2, params.get(2).toString());
        pst.setLong(3, Long.valueOf(params.get(3).toString()));
        pst.setString(4, params.get(4).toString());
        pst.setString(5, params.get(5).toString());
        pst.setLong(6, Long.valueOf(params.get(6).toString()));
        pst.setString(7, params.get(7).toString());
    }

    @Override
    public void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        prepareQueryParams(pst, params);
    }
}
