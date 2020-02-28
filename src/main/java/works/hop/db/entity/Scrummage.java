package works.hop.db.entity;

import works.hop.db.event.ValidatableEntity;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Entity
@Table(name = "jpa_scrummage")
@IdClass(ScrumDeckId.class)
public class Scrummage extends ValidatableEntity {

    @Id
    @NotNull(message = "Team id should not be null")
    @ManyToOne(targetEntity = Team.class)
    public final Long team;
    @Id
    @NotNull(message = "Deck id should not be null")
    @ManyToOne(targetEntity = CardDeck.class)
    public final Long deck;
    @NotNull(message = "Date created should not be null")
    @Future(message = "Date created should be in the future")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date startTime;
    @NotNull(message = "Date created should not be null")
    @PastOrPresent(message = "Date created should be either now or in the past")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date dateCreated;

    public Scrummage(Long team, Long deck, Date startTime, Date dateCreated) {
        this.team = team;
        this.deck = deck;
        this.startTime = startTime;
        this.dateCreated = dateCreated;
    }
}
