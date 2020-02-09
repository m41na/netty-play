package works.hop.db.api;

public class NodeLink<T extends Comparable<T>> {

    public final Node<T> from;
    public final Node<T> to;
    public final String name;
    public final Integer weight;

    public NodeLink(Node from, Node to, String name) {
        this(from, to, name, 0);
    }

    public NodeLink(Node from, Node to, String name, Integer weight) {
        this.from = from;
        this.to = to;
        this.name = name;
        this.weight = weight;
    }

    public NodeLink(String tableFrom, String columnFrom, T valueFrom, String tableTo, String columnTo, T valueTo, String name, Integer weight) {
        this(new Node(tableFrom, columnFrom, valueFrom), new Node(tableTo, columnTo, valueTo), name, weight);
    }

    @Override
    public String toString() {
        return "NodeLink{" +
                "from=" + from +
                ", to=" + to +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}
