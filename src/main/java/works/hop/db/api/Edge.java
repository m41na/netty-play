package works.hop.db.api;

import java.util.Objects;

public class Edge<T> {

    public final Vertex<T> src;
    public final Vertex<T> dest;

    public Edge(Vertex<T> src, Vertex<T> dest) {
        this.src = src;
        this.dest = dest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge<?> edge = (Edge<?>) o;
        return src.equals(edge.src) &&
                dest.equals(edge.dest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest);
    }

    @Override
    public String toString() {
        return '(' + src.id.toString() + ", " + dest + ')';
    }
}
