package works.hop.db.entity;

import java.io.Serializable;
import java.util.Objects;

public class TeamMemberId implements Serializable {

    private Long team;
    private Long member;

    public TeamMemberId() {
        super();
    }

    public TeamMemberId(Long team, Long member) {
        this.team = team;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamMemberId)) return false;
        TeamMemberId that = (TeamMemberId) o;
        return team.equals(that.team) &&
                member.equals(that.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, member);
    }
}
