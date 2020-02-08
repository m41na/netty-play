package works.hop.db.api;

public class AdjacencyLink {

    public final Node<Long> from;
    public final Node<Long> to;
    public final String linkName;

    public AdjacencyLink(String tableFrom, String columnFrom, Long valueFrom, String tableTo, String columnTo, Long valueTo, String linkName) {
        this.from = new Node(tableFrom, columnFrom, valueFrom);
        this.to = new Node(tableTo, columnTo, valueTo);
        this.linkName = linkName;
    }

    public AdjacencyLink(Node from, Node to, String linkName) {
        this.from = from;
        this.to = to;
        this.linkName = linkName;
    }

    @Override
    public String toString() {
        return "AdjacencyLink{" +
                "from=" + from +
                ", to=" + to +
                ", linkName='" + linkName + '\'' +
                '}';
    }
}
