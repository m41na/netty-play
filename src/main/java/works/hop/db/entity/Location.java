package works.hop.db.entity;

import java.util.Date;

public class Location {

    public final Long id;
    public final String title;
    public final Long address;
    public final Date dateCreated;

    public Location(Long id, String title, Long address, Date dateCreated) {
        this.id = id;
        this.address = address;
        this.title = title;
        this.dateCreated = dateCreated;
    }
}
