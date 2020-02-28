package works.hop.db.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "jpa_locations", uniqueConstraints = {@UniqueConstraint(columnNames = {"title", "loc_address"})})
public class Location {

    @Id
    @GeneratedValue
    @NotNull(message = "Id value should not be null")
    public final Long id;
    @NotNull(message = "Location title should not be null")
    @Size(min = 8, max = 64, message = "Location title length is between 8 and 64")
    public final String title;
    @OneToOne(targetEntity = Address.class)
    @JoinColumn(name = "loc_address")
    @NotNull(message = "Location address id should not be null")
    public final Long address;
    @NotNull(message = "Date created should not be null")
    @PastOrPresent(message = "Date created should be either now or in the past")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date dateCreated;

    public Location(Long id, String title, Long address, Date dateCreated) {
        this.id = id;
        this.address = address;
        this.title = title;
        this.dateCreated = dateCreated;
    }
}
