package works.hop.db.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class AdjacencyLinkRecordTest {

    private AdjacencyLinkRecord adjacency = new AdjacencyLinkRecord();
    private LinkNameRecord links = new LinkNameRecord();

    @Before
    public void setUp() {
        adjacency.destroy();
        links.destroy();
        links.init();
        adjacency.init();

        String linksSource = "/tbl_link_names_test.sql";
        Integer linksCount = links.loadSql(linksSource, 10);
        System.out.println("setUp loaded " + linksCount + " records");

        String adjacencySource = "/tbl_adjacency_links_test.sql";
        Integer adjacencyCount = adjacency.loadSql(adjacencySource, 10);
        System.out.println("setUp loaded " + adjacencyCount + " records");
    }

    @After
    public void tearDown() {
        adjacency.destroy();
        links.destroy();
        System.out.println("tearDown resetting database");
    }

    @Test
    public void createRecord() {
        AdjacencyLink account = new AdjacencyLink("tbl_user", "user_account", 1L, "tbl_account", "id", 6L, "user-account");
        Optional<Long> res = adjacency.insert(AdjacencyLinkRecord.insertRecord, account, false);
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

        Integer result = adjacency.update(AdjacencyLinkRecord.updateRecord, params);
        assertEquals(result.intValue(), 1);

        //apply updated value and check if the updated record exists
        params.put(6, 8L);              //current value_to
        Boolean exists = adjacency.exists(AdjacencyLinkRecord.checkRecordExists, params);
        assertTrue(exists);
    }

    @Test
    public void fetchLinksOut() {
        //table_from=? and column_from=? and link_name=? offset ? limit ?
        List<AdjacencyLink> list = adjacency.selectList(AdjacencyLinkRecord.fetchLinksOut, pst -> {
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
        List<AdjacencyLink> list = adjacency.selectList(AdjacencyLinkRecord.fetchLinksIn, pst -> {
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
        List<AdjacencyLink> accounts = adjacency.selectList(AdjacencyLinkRecord.fetchLinksByName, pst -> {
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
        Boolean exists = adjacency.exists(AdjacencyLinkRecord.checkRecordExists, params);
        assertTrue(exists);
        //delete action - table_from=? and column_from=? and table_to=? and column_to=? and link_name=? and value_to=?
        Integer result = adjacency.delete(AdjacencyLinkRecord.deleteRecord, params);
        assertEquals(result.intValue(), 1);
        //records after delete
        exists = adjacency.exists(AdjacencyLinkRecord.checkRecordExists, params);
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
        Boolean exists = adjacency.exists(AdjacencyLinkRecord.checkRecordExists, params);
        assertTrue(exists);

        //purge records
        Integer result = adjacency.truncate(AdjacencyLinkRecord.clearRecords);
        assertEquals(result.intValue(), 0);

        //verify record does not exist at this point
        exists = adjacency.exists(AdjacencyLinkRecord.checkRecordExists, params);
        assertFalse(exists);
    }
}
