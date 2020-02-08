package works.hop.db.entity;

import java.util.Date;

public class Team {

    public final Long id;
    public final String title;
    public final Long captain;
    public final Long location;
    public final Date dateCreated;

    public Team(Long id, String title, Long captain, Long location, Date dateCreated) {
        this.id = id;
        this.title = title;
        this.captain = captain;
        this.location = location;
        this.dateCreated = dateCreated;
    }
}
