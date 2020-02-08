package works.hop.db.api;

import java.util.*;

public class Graph {

    public final Map<Node, List<Node>> links;

    public Graph() {
        this.links = new TreeMap<>();
    }

    public void addEdge(Node from, Node to) {
        if (!links.containsKey(from)) {
            links.put(from, new LinkedList<>());
        }
        links.get(from).add(0, to);
        if(!links.containsKey(to)){
            links.put(to, new LinkedList<>());
        }
        links.get(to).add(0, from);
    }

    public void printGraph() {
        for (Iterator<Map.Entry<Node, List<Node>>> iter = links.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry<Node, List<Node>> entry = iter.next();
            Node key = entry.getKey();
            System.out.printf("key: %s%n", key);
            for (Node node : entry.getValue()) {
                System.out.println(node);
            }
            System.out.println();
        }
    }

    public void findConnected(Node start){

    }
}
