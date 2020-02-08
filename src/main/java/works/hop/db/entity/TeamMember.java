package works.hop.db.entity;

import java.util.Date;

public class TeamMember {

    public enum MemberStatus {requested, accepted, rejected, blocked, suspended, revoked}

    public final Long team;
    public final Long member;
    public final MemberStatus type;
    public final Date dateJoined;

    public TeamMember(Long team, Long member, MemberStatus type, Date dateJoined) {
        this.team = team;
        this.member = member;
        this.type = type;
        this.dateJoined = dateJoined;
    }
}
