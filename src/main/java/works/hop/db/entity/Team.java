package works.hop.db.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "jpa_teams")
public class Team {

    @Id
    @GeneratedValue
    @NotNull(message = "Id value should not be null")
    public final Long id;
    @Size(min = 8, max = 64, message = "Team title be between 8 to 64 letters")
    @NotNull(message = "Team title should not be null")
    public final String title;
    @NotNull(message = "Team captain id should not be null")
    @ManyToOne(targetEntity = User.class)
    public final Long captain;
    @NotNull(message = "Team location id should not be null")
    @ManyToOne(targetEntity = Location.class)
    public final Long location;
    @NotNull(message = "Date created should not be null")
    @PastOrPresent(message = "Date created should be either now or in the past")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date dateCreated;

    public Team(Long id, String title, Long captain, Long location, Date dateCreated) {
        this.id = id;
        this.title = title;
        this.captain = captain;
        this.location = location;
        this.dateCreated = dateCreated;
    }
}
