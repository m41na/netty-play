package works.hop.db.record;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import works.hop.db.entity.*;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TeamRecordTest {

    private TeamRecord teams = new TeamRecord();
    private LocationRecord locations = new LocationRecord();
    private AddressRecord addresses = new AddressRecord();
    private UserRecord users = new UserRecord();
    private AccountRecord accounts = new AccountRecord();

    @Before
    public void setUp() {
        teams.destroy();
        locations.destroy();
        addresses.destroy();
        users.destroy();
        accounts.destroy();

        accounts.init();
        users.init();
        addresses.init();
        locations.init();
        teams.init();

        String accountsSource = "/tbl_account_test.sql";
        Integer accountsCount = accounts.loadSql(accountsSource, 10);
        System.out.println("setUp loaded " + accountsCount + " records");

        String usersSource = "/tbl_user_test.sql";
        Integer usersCount = users.loadSql(usersSource, 10);
        System.out.println("setUp loaded " + usersCount + " records");

        String addressesSource = "/tbl_address_test.sql";
        Integer addressesCount = addresses.loadSql(addressesSource, 10);
        System.out.println("setUp loaded " + addressesCount + " records");

        String locationsSource = "/tbl_location_test.sql";
        Integer locationsCount = locations.loadSql(locationsSource, 10);
        System.out.println("setUp loaded " + locationsCount + " records");

        String teamsSource = "/tbl_team_test.sql";
        Integer teamsCount = teams.loadSql(teamsSource, 10);
        System.out.println("setUp loaded " + teamsCount + " records");
    }

    @After
    public void tearDown() {
        teams.destroy();
        locations.destroy();
        addresses.destroy();
        users.destroy();
        accounts.destroy();
        System.out.println("tearDown resetting database");
    }

    @Test
    public void createRecord() {
        Account account = new Account(null, "musa", "mwali", "musa.mwali@testing.com", Account.Type.user, null);
        Long acid = accounts.insert(AccountRecord.insertRecord, account);
        assertEquals(acid.longValue(), 6);

        User user = new User(null, "musa", "mwali", "musa_mwali", acid, null);
        Long uid = users.insert(UserRecord.insertRecord, user);
        assertEquals(uid.longValue(), 6);

        Address addr = new Address(null, "111 Stake lane", "12", "Sherlock", "IL", null);
        Long aid = addresses.insert(AddressRecord.insertRecord, addr);
        assertEquals(aid.longValue(), 6);

        Location loc = new Location(null, "Bouncing castles", aid, null);
        Long lid = locations.insert(LocationRecord.insertRecord, loc);
        assertEquals(lid.longValue(), 6);

        Team team = new Team(null, "Lunar Watts", uid, lid, null);
        Long tid = teams.insert(TeamRecord.insertRecord, team);
        assertEquals(tid.longValue(), 6);
    }

    @Test
    public void updateRecord() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, "Maji Tamu");
        params.put(2, 2);
        Integer result = teams.update(TeamRecord.updateRecord, params);
        assertEquals(result.intValue(), 1);
        Team team = teams.select(TeamRecord.selectRecord, 2L);
        assertEquals(team.title, "Maji Tamu");
        assertEquals(team.id.longValue(), 2l);
    }

    @Test
    public void selectRecordById() {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 1L);
        Team team1 = teams.select(TeamRecord.selectRecord, 1L);
        assertEquals(team1.title.trim(), "Air raiders");
        assertEquals(team1.id.longValue(), 1L);
    }

    @Test
    public void selectRecordsList() throws SQLException {
        List<Team> teams = this.teams.selectList(TeamRecord.fetchRecords, Collections.emptyMap(), 0, 5);
        assertEquals(teams.size(), 5);
    }

    @Test
    public void deleteRecord() {
        //record before delete
        Team team2 = teams.select(TeamRecord.selectRecord, 2L);
        assertEquals(team2.id.longValue(), 2L);
        //delete action
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 2);
        Integer result = teams.delete(TeamRecord.deleteRecord, params);
        assertEquals(result.intValue(), 1);
        //records after delete
        team2 = teams.select(TeamRecord.selectRecord, 2L);
        assertNull(team2);
    }

    @Test
    public void purgeRecords() throws SQLException {
        Integer result = teams.truncate(TeamRecord.clearRecords);
        assertEquals(result.intValue(), 0);
        List<Team> teams = this.teams.selectList(TeamRecord.fetchRecords, Collections.emptyMap(), 0, 5);
        assertEquals(teams.size(), 0);
    }
}
