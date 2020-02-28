package works.hop.db.entity;

import works.hop.db.event.ValidatableEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "jpa_accounts")
public class Account extends ValidatableEntity {

    @Id
    @GeneratedValue
    @NotNull(message = "Id value should not be null")
    public final Long id;
    @Column(unique = true)
    @NotNull(message = "Username should not be null")
    @Size(min = 8, max = 50, message = "Username should be between 8 and 50 characters")
    public final String username;
    @NotNull(message = "Password should not be null")
    @Size(min = 8, max = 256, message = "Password should be between 8 and 256 characters")
    public final String password;
    @Column(unique = true)
    @NotNull(message = "Email should not ba a null value")
    @Size(min = 8, max = 100, message = "Email should be between 8 and 100 characters")
    @Email(message = "Email value should be a valid email address")
    public final String emailAddress;
    @NotNull(message = "Account type should not be null")
    public final Type type;
    @NotNull(message = "Date created should not be null")
    @PastOrPresent(message = "Date created should be either now or in the past")
    @Temporal(TemporalType.TIMESTAMP)
    public final Date dateCreated;

    public Account(Long id, String username, String password, String emailAddress, Type type, Date dateCreated) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.type = type;
        this.dateCreated = dateCreated;
    }

    public enum Type {admin, user, guest}
}
