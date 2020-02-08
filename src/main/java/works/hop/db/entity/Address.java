package works.hop.db.entity;

import java.util.Date;

public class Address {

    public final Long id;
    public final String street;
    public final String unit;
    public final String city;
    public final String state;
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
