package works.hop.db.api;

import java.util.Date;

public class LinkName {

    public final String name;
    public final String description;
    public final Date dateCreated;

    public LinkName(String name, String description, Date dateCreated) {
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
    }
}
