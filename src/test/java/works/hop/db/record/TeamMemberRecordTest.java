package works.hop.db.record;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import works.hop.db.entity.Account;
import works.hop.db.entity.TeamMember;
import works.hop.db.entity.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TeamMemberRecordTest {

    private TeamRecord teams = new TeamRecord();
    private LocationRecord locations = new LocationRecord();
    private AddressRecord addresses = new AddressRecord();
    private UserRecord users = new UserRecord();
    private AccountRecord accounts = new AccountRecord();
    private TeamMemberRecord members = new TeamMemberRecord();

    @Before
    public void setUp() {
        members.destroy();
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
        members.init();

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

        String membersSource = "/tbl_team_members_test.sql";
        Integer membersCount = members.loadSql(membersSource, 10);
        System.out.println("setUp loaded " + membersCount + " records");
    }

    @After
    public void tearDown() {
        members.destroy();
        teams.destroy();
        locations.destroy();
        addresses.destroy();
        users.destroy();
        accounts.destroy();
        System.out.println("tearDown resetting database");
    }

    @Test
    public void requestToJoin() {
        Account account = new Account(null, "musa", "mwali", "musa.mwali@testing.com", Account.Type.user, null);
        Long accId = accounts.insert(AccountRecord.insertRecord, account);
        assertEquals(accId.longValue(), 6);

        //follower
        User user = new User(null, "musa", "mwali", "musa_mwali", accId, null);
        Long id = users.insert(UserRecord.insertRecord, user);
        assertEquals(id.longValue(), 6);

        TeamMember request = new TeamMember(1l, id, TeamMember.MemberStatus.requested, null);
        Optional<Long> result = members.insert(TeamMemberRecord.requestToJoin, request, false);
        assertEquals(result.get().intValue(), 1);
    }

    @Test
    public void updateRequest() {
        //account
        Account account = new Account(null, "musa", "mwali", "musa.mwali@testing.com", Account.Type.user, null);
        Long accId = accounts.insert(AccountRecord.insertRecord, account);
        assertEquals(accId.longValue(), 6);

        //member
        User user = new User(null, "musa", "mwali", "musa_mwali", accId, null);
        Long id = users.insert(UserRecord.insertRecord, user);
        assertEquals(id.longValue(), 6);

        //team member
        TeamMember request = new TeamMember(1l, id, TeamMember.MemberStatus.requested, null);
        Optional<Long> requested = members.insert(TeamMemberRecord.requestToJoin, request, false);
        assertEquals(requested.get().intValue(), 1);

        //accept join request (team accepting member)
        Integer response = members.update(TeamMemberRecord.updateRequest, pst -> {
            try {
                pst.setString(1, TeamMember.MemberStatus.accepted.toString());
                pst.setLong(2, 1); //team
                pst.setLong(3, id); //member
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        assertEquals(response.intValue(), 1);
    }

    @Test
    public void selectRecord() {
        TeamMember member = members.select(TeamMemberRecord.selectRecord, pst -> {
            try {
                pst.setLong(1, 5L); //team
                pst.setLong(2, 4L); //member
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        assertEquals(member.team.longValue(), 5);
        assertEquals(member.member.longValue(), 4);
        assertEquals(member.type, TeamMember.MemberStatus.requested);
    }

    @Test
    public void selectMembers() throws SQLException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 1); //team
        List<TeamMember> members = this.members.selectList(TeamMemberRecord.fetchMembers, params, 0, 5);
        assertEquals(members.size(), 3);
    }

    @Test
    public void deleteRecord() {
        //record before delete
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 2); //team
        params.put(2, 1); //member
        TeamMember unet = members.select(TeamMemberRecord.selectRecord, params);
        assertEquals(unet.team.longValue(), 2L);
        assertEquals(unet.member.longValue(), 1L);
        //delete action
        Integer result = members.delete(TeamMemberRecord.deleteRecord, params);
        assertEquals(result.intValue(), 1);
        //records after delete
        unet = members.select(TeamMemberRecord.selectRecord, params);
        assertNull(unet);
    }

    @Test
    public void purgeRecords() throws SQLException {
        Integer result = members.truncate(TeamMemberRecord.clearRecords);
        assertEquals(result.intValue(), 0);
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 1);
        List<TeamMember> members = this.members.selectList(TeamMemberRecord.fetchMembers, params, 0, 5);
        assertEquals(members.size(), 0);
    }
}
