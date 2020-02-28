package works.hop.db.api;

import java.sql.DriverManager;
import java.sql.SQLException;

public class PgDbConnector implements DbConnector {

    public static DbConnector connector;

    public final String DB_URL = "jdbc:postgresql://localhost/";
    public final String DB_USER = "postgres";
    public final String DB_PASS = "admins";
    public final String DB_DRIVER = "org.postgresql.Driver";

    private PgDbConnector() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static DbConnector instance() {
        if (connector == null) {
            connector = new PgDbConnector();
        }
        return connector;
    }

    @Override
    public DbConnection connection() {
        try {
            return DbConnection.get(DriverManager.getConnection(DB_URL, DB_USER, DB_PASS));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
