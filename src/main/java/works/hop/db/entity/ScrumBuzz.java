package works.hop.db.entity;

import works.hop.db.event.ValidatableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "jpa_scrum_buzz")
@IdClass(ScrumDeckId.class)
public class ScrumBuzz extends ValidatableEntity {

    @Id
    @ManyToOne(targetEntity = Team.class)
    @NotNull(message = "Team id should not be null")
    public final Long team;
    @Id
    @ManyToOne(targetEntity = CardDeck.class)
    @NotNull(message = "Deck id should not be null")
    public final Long deck;
    @ManyToOne(targetEntity = User.class)
    @NotNull(message = "Player id (from) should not be null")
    public final Long fromPlayer;
    @ManyToOne(targetEntity = User.class)
    @NotNull(message = "Player id (to) should not be null")
    public final Long toPlayer;
    @NotNull(message = "Chat content should not be null")
    @Size(min = 1, max = 256, message = "buzz content length should be between 1 and 256")
    public final String content;
    @NotNull(message = "Date/Time sent should not be null")
    @PastOrPresent(message = "Date/Time sent should be either now or in the past")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date timeSent;

    public ScrumBuzz(Long team, Long deck, Long fromPlayer, Long toPlayer, String content, Date timeSent) {
        this.team = team;
        this.deck = deck;
        this.fromPlayer = fromPlayer;
        this.toPlayer = toPlayer;
        this.content = content;
        this.timeSent = timeSent;
    }
}
