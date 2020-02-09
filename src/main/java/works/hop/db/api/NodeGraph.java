package works.hop.db.api;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class NodeGraph {

    static class Vertex<T extends Comparable<T>> {

        public final T data;
        public final Long id;
        public Boolean visited;

        public Vertex(Long id, T data) {
            this.id = id;
            this.data = data;
            this.visited = Boolean.FALSE;
        }
    }

    static class Graph {

        private Map<Long, Vertex> vertices;
        private Map<Long, Set<Long>> adjacency;
        private AtomicLong index = new AtomicLong(0L);

        public Graph() {
            this.vertices = new HashMap<>();
            this.adjacency = new HashMap<>();
        }

        public Long addVertex(Node node) {
            Long vIndex = index.getAndIncrement();
            Vertex vertex = new Vertex(vIndex, node);
            this.vertices.put(vIndex, vertex);
            return vIndex;
        }

        public void addEdge(Long start, Long end) {
            if(!adjacency.containsKey(start)){
                adjacency.put(start, new TreeSet<>());
            }
            adjacency.get(start).add(end);
            if(!adjacency.containsKey(end)) {
                adjacency.put(end, new TreeSet<>());
            }
            adjacency.get(end).add(start);
        }

        public void addEdge(Node start, Node end) {
            Long from = addVertex(start);
            Long to = addVertex(end);
            addEdge(from, to);
        }

        public void printVertex(Long index) {
            Vertex vertex = vertices.get(index);
            System.out.printf("index: %d, node: %s%n", vertex.id, vertex.data);
        }

        public Long getAdjUnvisitedVertex(Long index) {
            Set<Long> links = adjacency.get(index);
            if(links != null) {
                for (Long link : links) {
                    if (!vertices.get(link).visited) {
                        return link;
                    }
                }
            }
            return -1L;
        }

        public void bfs(Long start) {
            Queue<Long> queue = new LinkedList<>();
            vertices.get(start).visited = true;
            printVertex(start);
            queue.add(start);
            Long next;

            while (!queue.isEmpty()) {
                Long current = queue.remove();
                while ((next = getAdjUnvisitedVertex(current)) != -1) {
                    vertices.get(next).visited = true;
                    printVertex(next);
                    queue.add(next);
                }
            }
        }

        public void dfs(Long index) {
            vertices.get(index).visited = true;
            printVertex(index);
            if (adjacency.get(index) != null) {
                for (Long next : adjacency.get(index)) {
                    if (!vertices.get(next).visited) {
                        dfs(next);
                    }
                }
            }
        }
    }
}
