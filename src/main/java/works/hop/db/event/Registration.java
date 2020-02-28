package works.hop.db.event;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Registration extends ValidatableEntity {

    @NotNull(message = "Username should not be null")
    @Size(min = 8, max = 14, message = "Password should be between 8 and 14 characters")
    public final String username;
    @NotNull(message = "Password should not be null")
    @Size(min = 8, max = 14, message = "Password should be between 8 and 14 characters")
    public final String password;
    @NotNull(message = "Email should not ba a null value")
    @Email(message = "Email value should be a valid email address")
    public final String emailAddress;
    @NotNull(message = "First name should not be null")
    public final String firstName;
    @NotNull(message = "Last name should not be null")
    public final String lastName;

    public Registration(String username, String password, String emailAddress, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
