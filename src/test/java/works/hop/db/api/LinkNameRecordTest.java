package works.hop.db.api;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LinkNameRecordTest {

    private LinkNameRecord records = new LinkNameRecord();

    @Before
    public void setUp() {
        records.destroy();
        records.init();
        String source = "/tbl_link_names_test.sql";
        Integer count = records.loadSql(source, 10);
        System.out.println("setUp loaded " + count + " records");
    }

    @After
    public void tearDown() {
        records.destroy();
        System.out.println("tearDown resetting database");
    }

    @Test
    public void createRecord() {
        LinkName address = new LinkName("backup", "substitute player", null);
        Optional<Long> id = records.insert(LinkNameRecord.insertRecord, address, false);
        assertEquals(id.get().longValue(), 1);
    }

    @Test
    public void updateRecord() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "backup-player"); //name
        params.put(2, "123"); //description
        params.put(3, "reserve-player"); //name
        Integer result = records.update(LinkNameRecord.updateRecord, params);
        assertEquals(result.intValue(), 1);

        params.put(1, "backup-player");
        LinkName middleton = records.select(LinkNameRecord.selectRecord, params);
        assertEquals(middleton.name, "backup-player");
        assertEquals(middleton.description, "123");
    }

    @Test
    public void selectRecord() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "user-account");
        LinkName link = records.select(LinkNameRecord.selectRecord, params);
        assertEquals(link.name, "user-account");
        assertEquals(link.description, "links a user to their account");
    }

    @Test
    public void selectRecordsList() throws SQLException {
        List<LinkName> addresses = records.selectList(LinkNameRecord.fetchRecords, Collections.emptyMap(), 0, 5);
        assertEquals(addresses.size(), 5);
    }

    @Test
    public void deleteRecord(){
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "user-account");
        //record before delete
        LinkName link = records.select(LinkNameRecord.selectRecord, params);
        assertEquals(link.name, "user-account");
        //delete action
        Integer result = records.delete(LinkNameRecord.deleteRecord, params);
        assertEquals(result.intValue(), 1);
        //records after delete
        link = records.select(LinkNameRecord.selectRecord, params);
        assertNull(link);
    }

    @Test
    public void purgeRecords() throws SQLException {
        Integer result = records.truncate(LinkNameRecord.clearRecords);
        assertEquals(result.intValue(), 0);
        List<LinkName> addresss = records.selectList(LinkNameRecord.fetchRecords, Collections.emptyMap(), 0, 5);
        assertEquals(addresss.size(), 0);
    }
}
