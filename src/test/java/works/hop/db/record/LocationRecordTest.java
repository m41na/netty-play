package works.hop.db.record;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import works.hop.db.entity.Address;
import works.hop.db.entity.Location;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocationRecordTest {

    private LocationRecord records = new LocationRecord();
    private AddressRecord addresses = new AddressRecord();

    @Before
    public void setUp() {
        records.destroy();
        addresses.destroy();
        addresses.init();
        records.init();

        String addressesSource = "/tbl_address_test.sql";
        Integer addressesCount = addresses.loadSql(addressesSource, 10);
        System.out.println("setUp loaded " + addressesCount + " records");

        String usersSource = "/tbl_location_test.sql";
        Integer usersCount = records.loadSql(usersSource, 10);
        System.out.println("setUp loaded " + usersCount + " records");
    }

    @After
    public void tearDown() {
        records.destroy();
        addresses.destroy();
        System.out.println("tearDown resetting database");
    }

    @Test
    public void createRecord() {
        Address addr = new Address(null, "128 Watson Park", "1", "Oak Park", "IL", null);
        Long aid = addresses.insert(AddressRecord.insertRecord, addr);
        assertEquals(aid.longValue(), 6);

        Location loc = new Location(null, "Bouncing castles", aid, null);
        Long id = records.insert(LocationRecord.insertRecord, loc);
        assertEquals(id.longValue(), 6);
    }

    @Test
    public void updateRecord() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "Maji Tamu");
        params.put(4, 2);
        Integer result = records.update(LocationRecord.updateRecord, params);
        assertEquals(result.intValue(), 1);
        Location james = records.select(LocationRecord.selectRecord, 2L);
        assertEquals(james.title, "Maji Tamu");
        assertEquals(james.id.longValue(), 2l);
    }

    @Test
    public void selectRecordById() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 1L);
        Location loc1 = records.select(LocationRecord.selectRecord, 1L);
        assertEquals(loc1.title.trim(), "Soldier's Palace");
        assertEquals(loc1.id.longValue(), 1L);
    }

    @Test
    public void selectRecordsList() throws SQLException {
        List<Location> addresses = records.selectList(LocationRecord.fetchRecords, Collections.emptyMap(), 0, 5);
        assertEquals(addresses.size(), 5);
    }

    @Test
    public void deleteRecord() {
        //record before delete
        Location loc2 = records.select(LocationRecord.selectRecord, 2L);
        assertEquals(loc2.id.longValue(), 2L);
        //delete action
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 2);
        Integer result = records.delete(LocationRecord.deleteRecord, params);
        assertEquals(result.intValue(), 1);
        //records after delete
        loc2 = records.select(LocationRecord.selectRecord, 2L);
        assertNull(loc2);
    }

    @Test
    public void purgeRecords() throws SQLException {
        Integer result = records.truncate(LocationRecord.clearRecords);
        assertEquals(result.intValue(), 0);
        List<Location> locations = records.selectList(LocationRecord.fetchRecords, Collections.emptyMap(), 0, 5);
        assertEquals(locations.size(), 0);
    }
}
