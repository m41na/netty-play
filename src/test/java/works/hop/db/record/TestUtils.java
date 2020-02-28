package works.hop.db.record;

import works.hop.db.api.AbstractRecord;
import works.hop.db.api.DbConnector;
import works.hop.db.api.PgDbConnector;

public class TestUtils {

    static ResetDatabase resetDatabase = new ResetDatabase();

    public static void resetDatabase() {
        resetDatabase.init();
    }

    static class ResetDatabase extends AbstractRecord<Void> {
        public ResetDatabase(Class<Void> type, DbConnector dbc) {
            super(type, dbc);
        }

        public ResetDatabase() {
            this(PgDbConnector.instance());
        }

        public ResetDatabase(DbConnector dbConnector) {
            this(Void.class, dbConnector);
        }

        @Override
        public void init() {
            loadSql("/db-schema_test.sql", 100);
        }
    }
}
