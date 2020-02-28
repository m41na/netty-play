package works.hop.db.api;

import java.sql.Connection;
import java.sql.SQLException;

public interface DbConnector {

    DbConnection connection();

    class DbConnection {

        final Connection connection;

        static DbConnection get(Connection conn){
            System.out.println("Connecting to database...");
            return new DbConnection(conn);
        }

        private DbConnection(Connection connection) {
            this.connection = connection;
        }

        Connection connect(){
            try {
                connection.setAutoCommit(false);
                return this.connection;
            } catch (SQLException e) {
                System.out.println("Could not connect to database...");
                e.printStackTrace();
                return null;
            }
        }

        void commit(){
            try {
                connection.commit();
            } catch (SQLException e) {
                System.out.println("Could not commit transaction...");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        void rollback(){
            try {
                connection.rollback();
            } catch (SQLException e) {
                System.out.println("Could not rollback transaction...");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        public void close() {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Could not close the connection...");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
