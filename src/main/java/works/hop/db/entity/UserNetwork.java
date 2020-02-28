package works.hop.db.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Entity
@Table(name = "jpa_user_networks")
@IdClass(UserFollowId.class)
public class UserNetwork {

    public enum FollowStatus {requested, accepted, rejected, blocked, muted, unfollow}

    @Id
    @NotNull(message = "followee value should not be null")
    @ManyToOne(targetEntity = User.class)
    public final Long followee;
    @Id
    @NotNull(message = "follower value should not be null")
    @ManyToOne(targetEntity = User.class)
    public final Long follower;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Follow status value should not be null")
    public final FollowStatus status;
    @NotNull(message = "Date requested should not be null")
    @PastOrPresent(message = "Date requested should be either now or in the past")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date dateRequested;

    public UserNetwork(Long followee, Long follower, FollowStatus status, Date dateRequested) {
        this.followee = followee;
        this.follower = follower;
        this.status = status;
        this.dateRequested = dateRequested;
    }
}
