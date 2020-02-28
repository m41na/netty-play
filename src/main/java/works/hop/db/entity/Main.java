package works.hop.db.entity;

import org.rapidoid.annotation.Valid;
import org.rapidoid.jpa.JPA;
import org.rapidoid.setup.App;
import org.rapidoid.setup.On;

public class Main {

    public static void main(String[] args) {
        App.bootstrap(args).jpa(); // bootstrap JPA

        On.get("/size").json((String msg) -> msg.length());

        On.page("/hi").mvc("Hello <b>world</b>!");

        On.get("/register").json(() -> JPA.of(Account.class).all()); // get all books

        On.post("/register").json((@Valid Account b) -> JPA.save(b)); // insert new book if valid
    }
}
