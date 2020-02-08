package works.hop.db.record;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import works.hop.db.entity.Account;
import works.hop.db.entity.User;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserRecordTest {

    private UserRecord records = new UserRecord();
    private AccountRecord accounts = new AccountRecord();

    @Before
    public void setUp() {
        records.destroy();
        accounts.destroy();
        accounts.init();
        records.init();

        String accountsSource = "/tbl_account_test.sql";
        Integer accountsCount = accounts.loadSql(accountsSource, 10);
        System.out.println("setUp loaded " + accountsCount + " records");

        String usersSource = "/tbl_user_test.sql";
        Integer usersCount = records.loadSql(usersSource, 10);
        System.out.println("setUp loaded " + usersCount + " records");
    }

    @After
    public void tearDown() {
        records.destroy();
        accounts.destroy();
        System.out.println("tearDown resetting database");
    }

    @Test
    public void createRecord() {
        Account account = new Account(null, "musa", "mwali", "musa.mwali@testing.com", Account.Type.user, null);
        Long accId = accounts.insert(AccountRecord.insertRecord, account);
        assertEquals(accId.longValue(), 6);

        User user = new User(null, "musa", "mwali", "musa_mwali", accId, null);
        Long id = records.insert(UserRecord.insertRecord, user);
        assertEquals(id.longValue(), 6);
    }

    @Test
    public void updateRecord() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "same");
        params.put(2, "limwa");
        params.put(3, "jenzi");
        params.put(4, 2);
        Integer result = records.update(UserRecord.updateRecord, params);
        assertEquals(result.intValue(), 1);
        User james = records.select(UserRecord.selectRecordById, 2L);
        assertEquals(james.firstName, "same");
        assertEquals(james.lastName, "limwa");
        assertEquals(james.nickName, "jenzi");
    }

    @Test
    public void selectRecordById() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "steve");
        User steve = records.select(UserRecord.selectRecordById, 1L);
        assertEquals(steve.firstName, "steve");
        assertEquals(steve.id.longValue(), 1L);
    }

    @Test
    public void selectRecordByEmail() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "james.bond@test.com");
        User james = records.select(UserRecord.selectRecordByEmail, params);
        assertEquals(james.firstName, "james");
        assertEquals(james.nickName, "james_bond");
        assertEquals(james.id.longValue(), 2L);
    }

    @Test
    public void selectRecordByUsername() throws SQLException {
        User steve = records.select(UserRecord.selectRecordByUsername, (pst) -> {
            try {
                pst.setString(1, "mary");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        assertEquals(steve.firstName, "mary");
        assertEquals(steve.id.longValue(), 3L);
    }

    @Test
    public void selectRecordsList() throws SQLException {
        List<User> accounts = records.selectList(UserRecord.fetchRecords, Collections.emptyMap(), 0, 5);
        assertEquals(accounts.size(), 5);
    }

    @Test
    public void deleteRecord() {
        //record before delete
        User james = records.select(UserRecord.selectRecordById, 2L);
        assertEquals(james.id.longValue(), 2L);
        //delete action
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 2);
        Integer result = records.delete(UserRecord.deleteRecord, params);
        assertEquals(result.intValue(), 1);
        //records after delete
        james = records.select(UserRecord.selectRecordById, 2L);
        assertNull(james);
    }

    @Test
    public void purgeRecords() throws SQLException {
        Integer result = records.truncate(UserRecord.clearRecords);
        assertEquals(result.intValue(), 0);
        List<User> accounts = records.selectList(UserRecord.fetchRecords, Collections.emptyMap(), 0, 5);
        assertEquals(accounts.size(), 0);
    }
}
