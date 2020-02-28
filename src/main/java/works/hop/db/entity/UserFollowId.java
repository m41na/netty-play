package works.hop.db.entity;

import java.io.Serializable;
import java.util.Objects;

public class UserFollowId implements Serializable {

    private Long followee;
    private Long follower;

    public UserFollowId() {
        super();
    }

    public UserFollowId(Long followee, Long follower) {
        this.followee = followee;
        this.follower = follower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserFollowId)) return false;
        UserFollowId that = (UserFollowId) o;
        return followee.equals(that.followee) &&
                follower.equals(that.follower);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followee, follower);
    }
}
