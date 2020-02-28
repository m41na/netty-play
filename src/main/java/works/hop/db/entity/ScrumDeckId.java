package works.hop.db.entity;

import java.io.Serializable;
import java.util.Objects;

public class ScrumDeckId implements Serializable {

    private Long team;
    private Long deck;

    public ScrumDeckId() {
        super();
    }

    public ScrumDeckId(Long team, Long deck) {
        this.team = team;
        this.deck = deck;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScrumDeckId)) return false;
        ScrumDeckId that = (ScrumDeckId) o;
        return team.equals(that.team) &&
                deck.equals(that.deck);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, deck);
    }
}
