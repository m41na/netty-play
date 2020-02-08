package works.hop.db.api;

public interface Visitable {

    void accept(Visitor visitor);
}
