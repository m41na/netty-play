package works.hop.db.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class NodeLinkRecordTest {

    private NodeLinkRecord edges = new NodeLinkRecord();
    private LinkNameRecord links = new LinkNameRecord();

    @Before
    public void setUp() {
        edges.destroy();
        links.destroy();
        links.init();
        edges.init();

        String linksSource = "/tbl_link_names_test.sql";
        Integer linksCount = links.loadSql(linksSource, 10);
        System.out.println("setUp loaded " + linksCount + " records");

        String adjacencySource = "/tbl_node_links_test.sql";
        Integer adjacencyCount = edges.loadSql(adjacencySource, 10);
        System.out.println("setUp loaded " + adjacencyCount + " records");
    }

    @After
    public void tearDown() {
        edges.destroy();
        links.destroy();
        System.out.println("tearDown resetting database");
    }

    @Test
    public void createRecord() {
        LongNodeLink account = new LongNodeLink("tbl_user", "user_account", 1L, "tbl_account", "id", 6L, "user-account");
        Optional<Long> res = edges.insert(NodeLinkRecord.insertRecord, account, false);
        res.ifPresent(val -> assertEquals(val.longValue(), 1));
    }

    @Test
    public void updateRecord() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "tbl_user");          //expected table_from
        params.put(2, "user_account");      //expected column_from
        params.put(3, 1L);                  //expected value_from
        params.put(4, "tbl_account");       //expected table_to
        params.put(5, "id");                //expected column_to
        params.put(6, 8L);                  //expected value_to
        params.put(7, "user-account");      //expected link_name

        params.put(8, "tbl_user");          //current table_from
        params.put(9, "user_account");      //current column_from
        params.put(10, 1L);                 //current value_from
        params.put(11, "tbl_account");      //current table_to
        params.put(12, "id");               //current column_to
        params.put(13, 1L);                 //current value_to
        params.put(14, "user-account");     //current link_name

        Integer result = edges.update(NodeLinkRecord.updateRecord, params);
        assertEquals(result.intValue(), 1);

        //apply updated value and check if the updated record exists
        params.put(6, 8L);              //current value_to
        Boolean exists = edges.exists(NodeLinkRecord.checkRecordExists, params);
        assertTrue(exists);
    }

    @Test
    public void fetchLinksOut() {
        //table_from=? and column_from=? and link_name=? offset ? limit ?
        List<LongNodeLink> list = edges.selectList(NodeLinkRecord.fetchLinksOut, pst -> {
            try {
                pst.setString(1, "tbl_user");
                pst.setString(2, "user_account");
                pst.setString(3, "user-account");
                pst.setLong(4, 0);
                pst.setLong(5, 10);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        assertEquals(list.size(), 5);
    }

    @Test
    public void fetchLinksIn() {
        //table_to=? and column_to=? and link_name=? offset ? limit ?
        List<LongNodeLink> list = edges.selectList(NodeLinkRecord.fetchLinksIn, pst -> {
            try {
                pst.setString(1, "tbl_account");
                pst.setString(2, "id");
                pst.setString(3, "user-account");
                pst.setLong(4, 0);
                pst.setLong(5, 10);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        assertEquals(list.size(), 5);
    }

    @Test
    public void fetchLinksByName() throws SQLException {
        //link_name=? offset ? limit ?
        List<LongNodeLink> accounts = edges.selectList(NodeLinkRecord.fetchLinksByName, pst -> {
            try {
                pst.setString(1, "user-account");
                pst.setInt(2, 0);
                pst.setInt(3, 10);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        assertEquals(accounts.size(), 5);
    }

    @Test
    public void deleteRecord() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "tbl_user");      //table_from
        params.put(2, "user_account");  //column_from
        params.put(3, 1L);              //value_from
        params.put(4, "tbl_account");   //table_to
        params.put(5, "id");            //column_to
        params.put(6, 1L);              //value_to
        params.put(7, "user-account");  //link_name
        //record before delete
        Boolean exists = edges.exists(NodeLinkRecord.checkRecordExists, params);
        assertTrue(exists);
        //delete action - table_from=? and column_from=? and table_to=? and column_to=? and link_name=? and value_to=?
        Integer result = edges.delete(NodeLinkRecord.deleteRecord, params);
        assertEquals(result.intValue(), 1);
        //records after delete
        exists = edges.exists(NodeLinkRecord.checkRecordExists, params);
        assertFalse(exists);
    }

    @Test
    public void purgeRecords() throws SQLException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "tbl_user");      //current table_from
        params.put(2, "user_account");  //current column_from
        params.put(3, 5L);              //current value_from
        params.put(4, "tbl_account");   //current table_to
        params.put(5, "id");            //current column_to
        params.put(6, 5L);              //current value_to
        params.put(7, "user-account");  //current link_name

        //check if any records exists before purge
        Boolean exists = edges.exists(NodeLinkRecord.checkRecordExists, params);
        assertTrue(exists);

        //purge records
        Integer result = edges.truncate(NodeLinkRecord.clearRecords);
        assertEquals(result.intValue(), 0);

        //verify record does not exist at this point
        exists = edges.exists(NodeLinkRecord.checkRecordExists, params);
        assertFalse(exists);
    }
}
