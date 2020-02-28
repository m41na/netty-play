package works.hop.db.api;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Graph<T> {

    private final Map<Vertex<T>, List<Vertex<T>>> adj;
    private final AtomicLong ids = new AtomicLong(0L);

    public Graph() {
        this.adj = new HashMap<>();
    }

    public int size(){
        return this.adj().size();
    }

    public Vertex<T> vertex(Integer id) {
        return adj.keySet().stream().filter(v -> v.id.equals(id)).findFirst().get();
    }

    public Map<Vertex<T>, List<Vertex<T>>> adj() {
        return this.adj;
    }

    public Vertex<T> createVertex(T data) {
        Vertex<T> vertex = new Vertex<>(data, ids.getAndIncrement());
        adj.putIfAbsent(vertex, new LinkedList<>());
        return vertex;
    }

    public Set<Vertex<T>> vertices() {
        return adj().keySet();
    }

    public void forEachVertex(Consumer<Vertex<T>> consumer){
        adj().keySet().forEach(consumer);
    }

    public List<Vertex<T>> neighbours(Vertex<T> v){
        return adj().get(v);
    }

    public void forEachEdge(Vertex<T> vertex, Consumer<Edge> consumer){
        for(Vertex<T> v: neighbours(vertex)){
            consumer.accept(new Edge(vertex, v));
        }
    }

    public void forEachEdge(BiConsumer<Vertex<T>, Vertex<T>> action){
        forEachVertex(v -> neighbours(v).forEach(c -> action.accept(v, c)));
    }

    public void addEdge(Vertex<T> src, Vertex<T> dest) {
        adj().get(src).add(dest);
        adj().get(dest).add(src);
    }

    public List<Edge<T>> edges(){
        List<Edge<T>> result = new ArrayList<>();
        forEachEdge((s, d) -> result.add(new Edge<T>(s, d)));
        return result;
    }
}
