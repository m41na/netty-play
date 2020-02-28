package works.hop.db.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "jpa_users")
public class User {

    @Id
    @GeneratedValue
    @NotNull(message = "Id value should not be null")
    public final Long id;
    @Size(min = 8, max = 50, message = "First name should be between 8 to 50 letters")
    @NotNull(message = "First name should not be null")
    public final String firstName;
    @Size(min = 8, max = 50, message = "Last name should be between 8 to 50 letters")
    @NotNull(message = "Last name should not be null")
    public final String lastName;
    @Size(min = 8, max = 50, message = "Nickname should be between 8 to 50 letters")
    @Column(unique = true)
    public final String nickName;
    @ManyToOne(targetEntity = Account.class)
    @NotNull(message = "User account id should not be null")
    public final Long account;
    @NotNull(message = "Date created should not be null")
    @PastOrPresent(message = "Date created should be either now or in the past")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date dateCreated;

    public User(Long id, String firstName, String lastName, String nickName, Long account, Date dateCreated) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.account = account;
        this.dateCreated = dateCreated;
    }
}
