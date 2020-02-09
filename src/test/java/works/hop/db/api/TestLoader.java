package works.hop.db.api;

public class TestLoader extends AbstractRecord<Void> {

    private final String[] scripts = new String[]{
            "/db-schema_test.sql",
            "/tbl_account_test.sql",
            "/tbl_user_test.sql",
            "/tbl_users_network_test.sql",
            "/tbl_address_test.sql",
            "/tbl_location_test.sql",
            "/tbl_team_test.sql",
            "/tbl_team_members_test.sql",
            "/tbl_link_names_test.sql",
            "/tbl_node_links_test.sql",
    };

    public TestLoader(){
        this(Void.class, PgDbConnect.instance());
    }

    public TestLoader(Class<Void> type, DbConnect dbc) {
        super(type, dbc);
    }

    public void load(){
        for(int i = 0; i < scripts.length; i++){
            loadSql(scripts[i], 10);
        }
        System.out.println("loaded test data successfully");
    }
}
