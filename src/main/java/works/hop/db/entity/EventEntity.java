package works.hop.db.entity;

import works.hop.db.event.ValidatableEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "jpa_event_entities")
public class EventEntity extends ValidatableEntity {

    @Id
    @NotNull(message = "Event id should not be null")
    @Size(max = 128, message = "Event is max length is 128")
    public final String id;
    @Size(max = 128, message = "Event name max length is 128")
    @NotNull(message = "Event name should not be null")
    public final String name;
    @Size(max = 128, message = "Event sender max length is 128")
    @NotNull(message = "Event sender should not be null")
    public final String sender;
    @NotNull(message = "Event body should not be null")
    public final String body; //persist in serialized format
    @Size(max = 8, message = "Event version max length is 128")
    @NotNull(message = "Event version should not be null")
    public final String version;
    @NotNull(message = "Event date should not be null")
    @PastOrPresent(message = "Event date should be now or in the past")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date dateTime;

    public EventEntity(String id, String name, String sender, String body, String version, Date dateTime) {
        this.id = id;
        this.name = name;
        this.sender = sender;
        this.body = body;
        this.version = version;
        this.dateTime = dateTime;
    }

    public EventEntity(String name, String sender, String body) {
        this(UUID.fromString(Long.toString(new Date().getTime())).toString(), name, sender, body, "v1.0.0", new Date());
    }
}
