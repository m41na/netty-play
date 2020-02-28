package works.hop.db.record;

import works.hop.db.api.DbConnector;
import works.hop.db.api.PgDbConnector;
import works.hop.db.api.Record;
import works.hop.db.entity.UserNetwork;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserNetworkRecord extends Record<UserNetwork> {

    //you (follower) requesting (requested) to follow them (followee)
    public final static String requestToFollow = "insert into tbl_users_network (followee, follower, follow_status) values (?, ?, ?::follow_status);";
    //them (followee) updating your (follower) follow request (accepted/rejected/blocked)
    public final static String updateRequest = "update tbl_users_network set follow_status=?::follow_status where followee=? and follower=?";
    //you (follower) updating (silenced/unfollow) their (followee) follow status
    public final static String updateFollowee = "update tbl_users_network set follow_status=?::follow_status where followee=? and follower=?";
    //them (followee) updating (silenced/unfollow) your (follower) follow status
    public final static String updateFollower = "update tbl_users_network set follow_status=?::follow_status where follower=? and followee=?";
    //select a connection pair of follower/followee
    public final static String selectRecord = "select followee, follower, follow_status, date_requested from tbl_users_network where followee=? and follower=?";
    //delete a connection pair of follower/followee
    public final static String deleteRecord = "delete from tbl_users_network where followee=? and follower=?";
    //you (followee) retrieving your followers
    public final static String fetchFollowers = "select followee, follower, follow_status, date_requested from tbl_users_network where followee=? offset ? limit ?";
    //you (follower) retrieving your followees
    public final static String fetchFollowees = "select followee, follower, follow_status, date_requested from tbl_users_network where follower=? offset ? limit ?";
    //clear table records
    public final static String clearRecords = "truncate table tbl_users_network";

    public UserNetworkRecord() {
        this(PgDbConnector.instance());
    }

    public UserNetworkRecord(DbConnector dbConnector) {
        super(UserNetwork.class, dbConnector);
    }

    @Override
    public String tableName() {
        return "tbl_users_network";
    }

    @Override
    public String[] dropTable() {
        return new String[]{
                "drop table if exists tbl_users_network;",
                "drop type if exists follow_status;"
        };
    }

    @Override
    public String[] createTable() {
        return new String[]{
                "create type follow_status AS ENUM ('requested', 'accepted', 'rejected', 'blocked', 'muted', 'unfollow');",
                "create table if not exists tbl_users_network (\n" +
                        "    followee int not null,\n" +
                        "    follower int not null,\n" +
                        "    date_requested timestamp default current_timestamp,\n" +
                        "    follow_status follow_status not null default 'requested',\n" +
                        "    foreign key (followee) references tbl_user(id),\n" +
                        "    foreign key (follower) references tbl_user(id)\n" +
                        ");"
        };
    }

    @Override
    public void prepareCheck(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        //currently not in use
    }

    @Override
    public Long generatedKey(ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    public void prepareInsert(PreparedStatement pst, UserNetwork record) throws SQLException {
        pst.setLong(1, record.followee);
        pst.setLong(2, record.follower);
        pst.setString(3, record.status.toString());
    }

    @Override
    public UserNetwork extractRecord(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new UserNetwork(
                    rs.getLong("followee"),
                    rs.getLong("follower"),
                    UserNetwork.FollowStatus.valueOf(rs.getString("follow_status")),
                    new Date(rs.getTimestamp("date_requested").getTime())
            );
        }
        return null;
    }

    @Override
    public void prepareSelect(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString()));
        pst.setLong(2, Long.valueOf(params.get(2).toString()));
    }

    @Override
    public void prepareSelectList(PreparedStatement pst, Map<Integer, Object> params, Integer offset, Integer limit) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString())); //followee/follower
        pst.setInt(2, offset);
        pst.setInt(3, limit);
    }

    @Override
    public List<UserNetwork> extractRecords(ResultSet rs) throws SQLException {
        List<UserNetwork> records = new LinkedList<>();
        while (rs.next()) {
            records.add(new UserNetwork(
                    rs.getLong("followee"),
                    rs.getLong("follower"),
                    UserNetwork.FollowStatus.valueOf(rs.getString("follow_status")),
                    new Date(rs.getTimestamp("date_requested").getTime())));
        }
        return records;
    }

    @Override
    public void prepareUpdate(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setString(1, params.get(1).toString());
        pst.setLong(2, Long.valueOf(params.get(2).toString()));
        pst.setLong(3, Long.valueOf(params.get(3).toString()));
    }

    @Override
    public void prepareDelete(PreparedStatement pst, Map<Integer, Object> params) throws SQLException {
        pst.setLong(1, Long.valueOf(params.get(1).toString()));
        pst.setLong(2, Long.valueOf(params.get(2).toString()));
    }
}
