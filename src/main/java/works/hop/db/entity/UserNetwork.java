package works.hop.db.entity;

import java.util.Date;

public class UserNetwork {

    public enum FollowStatus {requested, accepted, rejected, blocked, muted, unfollow}

    public final Long followee;
    public final Long follower;
    public final FollowStatus type;
    public final Date dateRequested;

    public UserNetwork(Long followee, Long follower, FollowStatus type, Date dateRequested) {
        this.followee = followee;
        this.follower = follower;
        this.type = type;
        this.dateRequested = dateRequested;
    }
}
