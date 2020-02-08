package works.hop.db.entity;

import java.util.Date;

public class Account {

    public enum Type {admin, user, guest}

    public final Long id;
    public final String username;
    public final String password;
    public final String emailAddress;
    public final Type type;
    public final Date dateCreated;

    public Account(Long id, String username, String password, String emailAddress, Type type, Date dateCreated) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        this.type = type;
        this.dateCreated = dateCreated;
    }
}
