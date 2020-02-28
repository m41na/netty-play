package works.hop.db.entity;

import works.hop.db.event.ValidatableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "jpa_scrum_cards", uniqueConstraints = {@UniqueConstraint(columnNames = {"scrum_team", "scrum_deck", "scrum_player"})})
@IdClass(ScrumDeckId.class)
public class ScrumCard extends ValidatableEntity {

    @Id
    @ManyToOne(targetEntity = Team.class)
    @JoinColumn(name = "scrum_team")
    @NotNull(message = "Team id should not be null")
    public final Long team;
    @Id
    @ManyToOne(targetEntity = CardDeck.class)
    @JoinColumn(name = "scrum_deck")
    @NotNull(message = "Deck id should not be null")
    public final Long deck;
    @NotNull(message = "Player id should not be null")
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "scrum_player")
    public final Long player;
    @NotNull(message = "Card value should not be null")
    @Size(min = 1, max = 8, message = "Card value length should be between 1 and 8")
    public final String value;
    @NotNull(message = "Date/Time submitted should not be null")
    @PastOrPresent(message = "Date/Time submitted should be either now or in the past")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date timeSubmitted;

    public ScrumCard(Long team, Long deck, Long player, String value, Date timeSubmitted) {
        this.team = team;
        this.deck = deck;
        this.player = player;
        this.value = value;
        this.timeSubmitted = timeSubmitted;
    }
}
