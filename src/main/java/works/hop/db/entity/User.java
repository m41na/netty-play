package works.hop.db.entity;

import java.util.Date;

public class User {

    public final Long id;
    public final String firstName;
    public final String lastName;
    public final String nickName;
    public final Long account;
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
