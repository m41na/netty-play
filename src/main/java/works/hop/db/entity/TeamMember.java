package works.hop.db.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Entity
@Table(name = "jpa_team_members", uniqueConstraints = {@UniqueConstraint(columnNames = {"fk_team", "team_member"})})
@IdClass(TeamMemberId.class)
public class TeamMember {

    public enum MemberStatus {requested, accepted, rejected, blocked, suspended, revoked}

    @Id
    @NotNull(message = "team value should not be null")
    @ManyToOne(targetEntity = Team.class)
    @JoinColumn(name = "fk_team")
    public final Long team;
    @Id
    @NotNull(message = "member value should not be null")
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "team_member")
    public final Long member;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Member type value should not be null")
    public final MemberStatus type;
    @NotNull(message = "Date joined should not be null")
    @PastOrPresent(message = "Date joined should be either now or in the past")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date dateJoined;

    public TeamMember(Long team, Long member, MemberStatus type, Date dateJoined) {
        this.team = team;
        this.member = member;
        this.type = type;
        this.dateJoined = dateJoined;
    }
}
