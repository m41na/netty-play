package works.hop.db.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PgDbConnect implements DbConnect {

    public static DbConnect cs;

    public final String DB_URL = "jdbc:postgresql://localhost/";
    public final String DB_USER = "postgres";
    public final String DB_PASS = "admins";
    public final String DB_DRIVER = "org.postgresql.Driver";

    private PgDbConnect() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static DbConnect instance() {
        if (cs == null) {
            cs = new PgDbConnect();
        }
        return cs;
    }

    @Override
    public Connection connection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
