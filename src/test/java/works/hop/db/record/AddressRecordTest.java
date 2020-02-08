package works.hop.db.record;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import works.hop.db.entity.Address;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AddressRecordTest {

    private AddressRecord records = new AddressRecord();

    @Before
    public void setUp() {
        records.destroy();
        records.init();
        String source = "/tbl_address_test.sql";
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
        Address address = new Address(null, "388", "Dr King Avenue", "Canton", "OH", null);
        Long id = records.insert(AddressRecord.insertRecord, address);
        assertEquals(id.longValue(), 6);
    }

    @Test
    public void updateRecord() {
        Map<Integer, Object> params = new HashMap<>(); //'987 Lakeshore Drive','12','Madison', 'WI'
        params.put(1, "987 Lakeshore Drive"); //street
        params.put(2, "123"); //unit
        params.put(3, "Middleton"); //city
        params.put(4, "WI"); //state
        params.put(5, 3); //id
        Integer result = records.update(AddressRecord.updateRecord, params);
        assertEquals(result.intValue(), 1);
        Address middleton = records.select(AddressRecord.selectRecord, 3L);
        assertEquals(middleton.id.longValue(), 3L);
        assertEquals(middleton.city, "Middleton");
        assertEquals(middleton.state, "WI");
    }

    @Test
    public void selectRecord() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 1);
        Address addr = records.select(AddressRecord.selectRecord, 1L);
        assertEquals(addr.city, "Chicago");
        assertEquals(addr.id.longValue(), 1L);
    }

    @Test
    public void selectRecordsList() throws SQLException {
        List<Address> addresses = records.selectList(AddressRecord.fetchRecords, Collections.emptyMap(), 0, 5);
        assertEquals(addresses.size(), 5);
    }

    @Test
    public void deleteRecord(){
        //record before delete
        Address addr = records.select(AddressRecord.selectRecord, 2L);
        assertEquals(addr.id.longValue(), 2L);
        //delete action
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 2);
        Integer result = records.delete(AddressRecord.deleteRecord, params);
        assertEquals(result.intValue(), 1);
        //records after delete
        addr = records.select(AddressRecord.selectRecord, 2L);
        assertNull(addr);
    }

    @Test
    public void purgeRecords() throws SQLException {
        Integer result = records.truncate(AddressRecord.clearRecords);
        assertEquals(result.intValue(), 0);
        List<Address> addresss = records.selectList(AddressRecord.fetchRecords, Collections.emptyMap(), 0, 5);
        assertEquals(addresss.size(), 0);
    }
}
