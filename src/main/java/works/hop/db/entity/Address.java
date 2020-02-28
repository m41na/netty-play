package works.hop.db.entity;

import works.hop.db.event.ValidatableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "jpa_addresses", uniqueConstraints={@UniqueConstraint(columnNames = {"city", "state"})})
public class Address extends ValidatableEntity {

    @Id
    @GeneratedValue
    @NotNull(message = "Id value should not be null")
    public final Long id;
    @Size(max = 50, message = "Address street max length is 50")
    public final String street;
    @Size(max = 50, message = "Address unit max length is 50")
    public final String unit;
    @Size(max = 50, message = "Address city max length is 50")
    @NotNull(message = "City should not be null")
    public final String city;
    @Size(max = 3, message = "Address state max length is 3")
    @NotNull(message = "State should not be null")
    public final String state;
    @NotNull(message = "Date created should not be null")
    @PastOrPresent(message = "Date created should be either now or in the past")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date dateCreated;

    public Address(Long id, String street, String unit, String city, String state, Date dateCreated) {
        this.id = id;
        this.street = street;
        this.unit = unit;
        this.city = city;
        this.state = state;
        this.dateCreated = dateCreated;
    }
}
