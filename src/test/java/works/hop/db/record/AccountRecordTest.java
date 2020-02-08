package works.hop.db.record;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import works.hop.db.entity.Account;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AccountRecordTest {

    private AccountRecord records = new AccountRecord();

    @Before
    public void setUp() {
        records.destroy();
        records.init();
        String source = "/tbl_account_test.sql";
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
        Account account = new Account(null, "musa", "mwali", "musa.mwali@testing.com", Account.Type.user, null);
        Long id = records.insert(AccountRecord.insertRecord, account);
        assertEquals(id.longValue(), 6);
    }

    @Test
    public void updateRecord() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "admin");
        params.put(2, 2);
        Integer result = records.update(AccountRecord.updateRecord, params);
        assertEquals(result.intValue(), 1);
        Account james = records.select(AccountRecord.selectRecordById, 2L);
        assertEquals(james.type, Account.Type.admin);
    }

    @Test
    public void selectRecordById() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "steve");
        Account steve = records.select(AccountRecord.selectRecordById, 1L);
        assertEquals(steve.username, "steve");
        assertEquals(steve.id.longValue(), 1L);
    }

    @Test
    public void selectRecordByEmail(){
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "james.bond@test.com");
        Account james = records.select(AccountRecord.selectRecordByEmail,  params);
        assertEquals(james.username, "james");
        assertEquals(james.emailAddress, "james.bond@test.com");
        assertEquals(james.id.longValue(), 2L);
    }

    @Test
    public void selectRecordByUsername() throws SQLException {
        Account steve = records.select(AccountRecord.selectRecordByUsername, (pst) -> {
            try {
                pst.setString(1, "mary");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        assertEquals(steve.username, "mary");
        assertEquals(steve.id.longValue(), 3L);
    }

    @Test
    public void selectRecordsList() throws SQLException {
        List<Account> accounts = records.selectList(AccountRecord.fetchRecords, Collections.emptyMap(), 0, 5);
        assertEquals(accounts.size(), 5);
    }

    @Test
    public void deleteRecord(){
        //record before delete
        Account james = records.select(AccountRecord.selectRecordById, 2L);
        assertEquals(james.id.longValue(), 2L);
        //delete action
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 2);
        Integer result = records.delete(AccountRecord.deleteRecord, params);
        assertEquals(result.intValue(), 1);
        //records after delete
        james = records.select(AccountRecord.selectRecordById, 2L);
        assertNull(james);
    }

    @Test
    public void purgeRecords() throws SQLException {
        Integer result = records.truncate(AccountRecord.clearRecords);
        assertEquals(result.intValue(), 0);
        List<Account> accounts = records.selectList(AccountRecord.fetchRecords, Collections.emptyMap(), 0, 5);
        assertEquals(accounts.size(), 0);
    }
}
