package works.hop.db.rethink;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Db;
import com.rethinkdb.net.Connection;

import java.util.function.Function;

public class RethinkDs {

    public final RethinkDB r;

    public RethinkDs() {
        this(RethinkDB.r);
    }

    public RethinkDs(RethinkDB r) {
        this.r = r;
    }

    public Connection connection() {
        return r.connection().hostname("localhost").port(28015).connect();
    }

    public void init() {
        r.db("scrummage").tableCreate("players").run(connection());
    }

    public void execute(Function<Db, Db> consumer) {
        consumer.apply(r.db("scrummage")).run(connection());
    }
}
