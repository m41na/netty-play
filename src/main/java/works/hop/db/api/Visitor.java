package works.hop.db.api;

import java.util.Set;

public interface Visitor {

    Boolean isVisited(Node node);

    Set<Node> getVisited();

    void visit(Node node);
}
