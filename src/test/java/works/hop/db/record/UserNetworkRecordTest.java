package works.hop.db.record;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import works.hop.db.entity.Account;
import works.hop.db.entity.User;
import works.hop.db.entity.UserNetwork;

import java.sql.SQLException;
import java.util.*;

import static org.junit.Assert.*;

public class UserNetworkRecordTest {

    private UserRecord users = new UserRecord();
    private AccountRecord accounts = new AccountRecord();
    private UserNetworkRecord networks = new UserNetworkRecord();

    @Before
    public void setUp() {
        networks.destroy();
        users.destroy();
        accounts.destroy();

        accounts.init();
        users.init();
        networks.init();

        String accountsSource = "/tbl_account_test.sql";
        Integer accountsCount = accounts.loadSql(accountsSource, 10);
        System.out.println("setUp loaded " + accountsCount + " records");

        String usersSource = "/tbl_user_test.sql";
        Integer usersCount = users.loadSql(usersSource, 10);
        System.out.println("setUp loaded " + usersCount + " records");

        String netsSource = "/tbl_users_network_test.sql";
        Integer netsCount = networks.loadSql(netsSource, 10);
        System.out.println("setUp loaded " + netsCount + " records");
    }

    @After
    public void tearDown() {
        networks.destroy();
        users.destroy();
        accounts.destroy();
        System.out.println("tearDown resetting database");
    }

    @Test
    public void requestToFollow() {
        Account account = new Account(null, "musa", "mwali", "musa.mwali@testing.com", Account.Type.user, null);
        Long accId = accounts.insert(AccountRecord.insertRecord, account);
        assertEquals(accId.longValue(), 6);

        //follower
        User user = new User(null, "musa", "mwali", "musa_mwali", accId, null);
        Long id = users.insert(UserRecord.insertRecord, user);
        assertEquals(id.longValue(), 6);

        UserNetwork request = new UserNetwork(1l, id, UserNetwork.FollowStatus.requested, null);
        Optional<Long> result = networks.insert(UserNetworkRecord.requestToFollow, request, false);
        assertEquals(result.get().intValue(), 1);
    }

    @Test
    public void updateRequest() {
        //follower
        Account account = new Account(null, "musa", "mwali", "musa.mwali@testing.com", Account.Type.user, null);
        Long accId = accounts.insert(AccountRecord.insertRecord, account);
        assertEquals(accId.longValue(), 6);

        //follower
        User user = new User(null, "musa", "mwali", "musa_mwali", accId, null);
        Long id = users.insert(UserRecord.insertRecord, user);
        assertEquals(id.longValue(), 6);

        UserNetwork request = new UserNetwork(1l, id, UserNetwork.FollowStatus.requested, null);
        Optional<Long> requested = networks.insert(UserNetworkRecord.requestToFollow, request, false);
        assertEquals(requested.get().intValue(), 1);

        //accept follow request (followee accepting follower)
        Integer response = networks.update(UserNetworkRecord.updateRequest, pst -> {
            try {
                pst.setString(1, UserNetwork.FollowStatus.accepted.toString());
                pst.setLong(2, 1); //followee
                pst.setLong(3, id); //follower
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        assertEquals(response.intValue(), 1);

        //update followee status (follower muting followee)
        Integer update = networks.update(UserNetworkRecord.updateFollowee, pst -> {
            try {
                pst.setString(1, UserNetwork.FollowStatus.muted.toString());
                pst.setLong(2, 1l); //followee
                pst.setLong(3, id); //follower
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        assertEquals(update.intValue(), 1);

        //update follower status (followee unfollowing follower)
        Integer update2 = networks.update(UserNetworkRecord.updateFollower, pst -> {
            try {
                pst.setString(1, UserNetwork.FollowStatus.unfollow.toString());
                pst.setLong(2, id); //follower
                pst.setLong(3, 1); //followee
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        assertEquals(update2.intValue(), 1);
    }

    @Test
    public void selectRecord() {
        UserNetwork unet = networks.select(UserNetworkRecord.selectRecord, pst -> {
            try {
                pst.setLong(1, 5L); //followee
                pst.setLong(2, 4L); //follower
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        assertEquals(unet.followee.longValue(), 5);
        assertEquals(unet.follower.longValue(), 4);
        assertEquals(unet.type, UserNetwork.FollowStatus.requested);
    }

    @Test
    public void selectFollowers() throws SQLException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 5); //follower
        List<UserNetwork> unets = networks.selectList(UserNetworkRecord.fetchFollowers, params, 0, 5);
        assertEquals(unets.size(), 4);
    }

    @Test
    public void selectFollowees() throws SQLException {
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 1); //followee
        List<UserNetwork> unets = networks.selectList(UserNetworkRecord.fetchFollowees, params, 0, 5);
        assertEquals(unets.size(), 4);
    }

    @Test
    public void deleteRecord() {
        //record before delete
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 2); //followee
        params.put(2, 1); //follower
        UserNetwork unet = networks.select(UserNetworkRecord.selectRecord, params);
        assertEquals(unet.followee.longValue(), 2L);
        assertEquals(unet.follower.longValue(), 1L);
        //delete action
        Integer result = networks.delete(UserNetworkRecord.deleteRecord, params);
        assertEquals(result.intValue(), 1);
        //records after delete
        unet = networks.select(UserNetworkRecord.selectRecord, params);
        assertNull(unet);
    }

    @Test
    public void purgeRecords() throws SQLException {
        Integer result = networks.truncate(UserNetworkRecord.clearRecords);
        assertEquals(result.intValue(), 0);
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, 1);
        List<UserNetwork> accounts = networks.selectList(UserNetworkRecord.fetchFollowers, params, 0, 5);
        assertEquals(accounts.size(), 0);
    }
}
