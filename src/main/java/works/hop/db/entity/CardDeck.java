package works.hop.db.entity;

import works.hop.db.event.ValidatableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "jpa_card_decks")
public class CardDeck extends ValidatableEntity {

    @Id
    @GeneratedValue
    @NotNull(message = "Id value should not be null")
    public final Long id;
    @NotNull(message = "Card deck name should not be null")
    @Size(min = 8, max = 32, message = "Card deck name be between 8 and 32 characters")
    @Column(unique = true)
    public final String name;
    @NotNull(message = "Card deck values should not be null")
    public final String[] values;
    @NotNull(message = "Date created should not be null")
    @PastOrPresent(message = "Date created should be either now or in the past")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date dateCreated;

    public CardDeck(Long id, String name, String[] values, Date dateCreated) {
        this.id = id;
        this.name = name;
        this.values = values;
        this.dateCreated = dateCreated;
    }
}
