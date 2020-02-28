package works.hop.db.api;

import java.util.*;
import java.util.function.Function;

public class Graphs {

    private Graphs() {
        throw new Error("New instance not allowed");
    }

    public static Graph createWeightedGraph() {
        return new Graph();
    }

    public static <T> String printGraph(Graph<T> graph) {
        StringBuilder builder = new StringBuilder();
        for (Vertex<T> vertex : graph.vertices()) {
            for (Vertex<T> child : graph.neighbours(vertex)) {
                builder.append("(" + child.id.toString() + " --> " + child.value + ")\t");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public static void printTagsDFS(Vertex<TagEl> vertex, Graph<TagEl> graph, Visitor<Vertex<TagEl>> visitor) {
        if (!visitor.visit(vertex)) {
            visitor.render(vertex, (v -> "<" + v.value.tag + ">"));
            graph.forEachEdge(vertex, edge -> printTagsDFS(edge.dest, graph, visitor));
        }
    }

    public static <T> void printDFS(Vertex<T> vertex, Graph<T> graph, Visitor<Vertex<T>> visitor, Function<Vertex<T>, String> consumer) {
        if (!visitor.visit(vertex)) {
            visitor.render(vertex, consumer);
            graph.forEachEdge(vertex, edge -> printDFS(edge.dest, graph, visitor, consumer));
        }
    }

    public static void printTagsBFS(Vertex<TagEl> vertex, Graph<TagEl> graph, Visitor<Vertex<TagEl>> visitor) {
        Queue<Vertex<TagEl>> queue = new LinkedList<>();
        if (!visitor.visit(vertex)) {
            queue.add(vertex);
            visitor.render(vertex, v -> "<" + v.value.tag + ">");
        }
        while (!queue.isEmpty()) {
            Vertex<TagEl> v = queue.poll();
            for (Vertex<TagEl> child : graph.neighbours(v)) {
                if (!visitor.visit(child)) {
                    queue.add(child);
                    visitor.render(child, c -> "<" + c.value.tag + ">");
                }
            }
        }
    }

    public static <T> void printBFS(Vertex<T> vertex, Graph<T> graph, Visitor<Vertex<T>> visitor, Function<Vertex<T>, String> consumer) {
        Queue<Vertex<T>> queue = new LinkedList<>();
        if (!visitor.visit(vertex)) {
            queue.add(vertex);
            visitor.render(vertex, consumer);
        }
        while (!queue.isEmpty()) {
            Vertex<T> v = queue.poll();
            for (Vertex<T> child : graph.neighbours(v)) {
                if (!visitor.visit(child)) {
                    queue.add(child);
                    visitor.render(child, consumer);
                }
            }
        }
    }

    public interface Visitor<T> {

        Boolean visit(T node);

        void render(T node, Function<T, String> renderer);

        String result();
    }

    public interface Visitable {

        void accept(Visitor visitor);
    }

    public static class PrintOutVisitor<T> implements Graphs.Visitor<Vertex<T>> {

        private Set<Vertex<T>> nodes = new TreeSet<>((o1, o2) -> {
            if (o1 == o2) return 0;
            return o1.id.compareTo(o2.id);
        });

        private StringBuilder builder = new StringBuilder();

        @Override
        public Boolean visit(Vertex<T> node) {
            if (!nodes.contains(node)) {
                nodes.add(node);
                System.out.println("(" + node.id + ")");
                return false;
            }
            return true;
        }

        @Override
        public void render(Vertex<T> node, Function<Vertex<T>, String> renderer) {
            this.builder.append(renderer.apply(node));
        }

        @Override
        public String result() {
            return this.builder.toString();
        }
    }

    public static class TagEl implements Visitable {

        public final String tag;
        public final Boolean close;
        public final String text;
        public final Attrs attributes;

        public TagEl(String tag, Boolean close, String text, Attrs attributes) {
            this.tag = tag;
            this.close = close;
            this.text = text;
            this.attributes = attributes;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }

        @Override
        public String toString() {
            return tag;
        }
    }

    public static class Attr {

        public final String key;
        public final String value;

        public Attr(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static class Attrs {

        public final List<Attr> properties;

        public Attrs(List<Attr> properties) {
            this.properties = properties;
        }
    }

    public static class AttrsBuilder {

        final List<Attr> properties = new ArrayList<>();

        private AttrsBuilder() {
        }

        public static AttrsBuilder newBuilder() {
            return new AttrsBuilder();
        }

        public static Attrs emptyAttrs() {
            return new Attrs(Collections.emptyList());
        }

        public AttrsBuilder attr(String key, String value) {
            this.properties.add(new Attr(key, value));
            return this;
        }

        public AttrsBuilder attr(Attr attr) {
            this.properties.add(attr);
            return this;
        }

        public Attrs build() {
            return new Attrs(this.properties);
        }
    }
}
