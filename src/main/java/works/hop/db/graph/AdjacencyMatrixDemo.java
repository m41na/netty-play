package works.hop.db.graph;

import java.util.LinkedList;
import java.util.Queue;

public class AdjacencyMatrixDemo {

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

        System.out.println("Visits:");
        graph.bfs();
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
        private int[][] matrix;
        private int vIndex;
        private Queue<Integer> queue;

        public Graph(int maxSize) {
            vertices = new Vertex[maxSize];
            matrix = new int[maxSize][maxSize];
            vIndex = 0;
            queue = new LinkedList<>();
        }

        public void addVertex(Character label) {
            if(vertices.length > vIndex) {
                vertices[vIndex++] = new Vertex(label);
            }
            else{
                throw new RuntimeException("graph capacity exceeded");
            }
        }

        public void addEdge(int start, int end) {
            matrix[start][end] = 1;
            matrix[end][start] = 1;
        }

        public void displayVertex(int index) {
            System.out.println(vertices[index].data);
        }

        public int getAdjUnvisitedVertex(int index) {
            for (int j = 0; j < vIndex; j++) {
                if (matrix[index][j] == 1 && !vertices[j].visited) {
                    return j;
                }
            }
            return 0;
        }

        public void bfs() {
            vertices[0].visited = true;
            displayVertex(0);
            queue.add(0);
            int v2;

            while (!queue.isEmpty()) {
                int v1 = queue.remove();
                while ((v2 = getAdjUnvisitedVertex(v1)) != 0) {
                    vertices[v2].visited = true;
                    displayVertex(v2);
                    queue.add(v2);
                }
            }
        }
    }
}
