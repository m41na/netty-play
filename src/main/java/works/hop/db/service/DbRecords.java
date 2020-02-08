package works.hop.db.service;

import works.hop.db.entity.User;
import works.hop.db.record.*;

import java.util.List;

public class DbRecords {

    public final AccountRecord accountRecord;
    public final UserRecord userRecord;
    public final UserNetworkRecord userNetworkRecord;
    public final AddressRecord addressRecord;
    public final LocationRecord locationRecord;
    public final TeamRecord teamRecord;

    public DbRecords() {
        this(new AccountRecord(), new UserRecord(), new UserNetworkRecord(), new AddressRecord(), new LocationRecord(), new TeamRecord());
    }

    public DbRecords(AccountRecord accountRecord, UserRecord userRecord, UserNetworkRecord userNetworkRecord, AddressRecord addressRecord, LocationRecord locationRecord, TeamRecord teamRecord) {
        this.accountRecord = accountRecord;
        this.userRecord = userRecord;
        this.userNetworkRecord = userNetworkRecord;
        this.addressRecord = addressRecord;
        this.locationRecord = locationRecord;
        this.teamRecord = teamRecord;
    }

    public List<User> getTeamMates(User user){
        return null;
    }
}
