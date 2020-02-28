package works.hop.db.graph;

import java.util.*;

public class AdjacencyListDemo {

    public static void main(String[] args) {
        Graph graph = new Graph(6);
        graph.addVertex('A');
        graph.addVertex('B');
        graph.addVertex('C');
        graph.addVertex('D');
        graph.addVertex('E');
        graph.addVertex('F');

        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(0, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        graph.addEdge(1, 4);

        System.out.println("bfs Visits:");
        graph.bfs();
        System.out.println("dfs Visits:");
        //graph.dfs(0);
        System.out.println();
    }

    static class Vertex<T extends Comparable<T>> {

        public T data;
        public Boolean visited;

        public Vertex(T data) {
            this.data = data;
            this.visited = Boolean.FALSE;
        }
    }

    static class Graph {

        private Vertex[] vertices;
        private Map<Integer, Set<Integer>> adjacency;
        private int currIndex;
        private Queue<Integer> queue;

        public Graph(int maxSize) {
            this.vertices = new Vertex[maxSize];
            this.adjacency = new HashMap<>();
            for (int i = 0; i < maxSize; i++) {
                adjacency.put(i, new TreeSet<>());
            }
            currIndex = 0;
            queue = new LinkedList<>();
        }

        public void addVertex(Character label) {
            if (vertices.length > currIndex) {
                vertices[currIndex++] = new Vertex(label);
            } else {
                throw new RuntimeException("graph capacity exceeded");
            }
        }

        public void addEdge(int start, int end) {
            adjacency.get(start).add(end);
            //adjacency.get(end).add(start);
        }

        public void displayVertex(int index) {
            System.out.println(vertices[index].data);
        }

        public int getAdjUnvisitedVertex(int index) {
            Set<Integer> links = adjacency.get(index);
            for (Integer link : links) {
                if (!vertices[link].visited) {
                    return link;
                }
            }
            return -1;
        }

        public void bfs() {
            vertices[0].visited = true;
            displayVertex(0);
            queue.add(0);
            int v2;

            while (!queue.isEmpty()) {
                int v1 = queue.remove();
                while ((v2 = getAdjUnvisitedVertex(v1)) != -1) {
                    vertices[v2].visited = true;
                    displayVertex(v2);
                    queue.add(v2);
                }
            }
        }

        public void dfs(int i) {
            vertices[i].visited = true;
            displayVertex(i);
            if (adjacency.get(i) != null) {
                for (Integer next : adjacency.get(i)) {
                    if (!vertices[next].visited) {
                        dfs(next);
                    }
                }
            }
        }
    }
}
