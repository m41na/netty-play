package works.hop.db.api;

import java.util.Objects;

public class Vertex<T> {

    public final T value;
    public final Comparable id;

    public Vertex(T value, Comparable id) {
        this.value = value;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;
        Vertex<?> vertex = (Vertex<?>) o;
        return id.equals(vertex.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return '(' + id.toString() + ", " + value + ')';
    }
}
