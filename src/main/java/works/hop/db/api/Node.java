package works.hop.db.api;

import java.util.Objects;

public class Node<V extends Comparable<V>> implements Comparable<Node<V>>, Visitable {

    public final String table;
    public final String column;
    public final V value;

    public Node(String table, String column, V value) {
        this.table = table;
        this.column = column;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return table.equals(node.table) &&
                column.equals(node.column) &&
                value.equals(node.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(table, column, value);
    }

    @Override
    public int compareTo(Node<V> o) {
        if(this == o) return 0;
        int comparison = table.compareTo(o.table);
        if(comparison != 0){
            return comparison;
        }
        comparison = column.compareTo(o.column);
        if(comparison != 0){
            return comparison;
        }
        return value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return "Node{" +
                "table='" + table + '\'' +
                ", column='" + column + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
