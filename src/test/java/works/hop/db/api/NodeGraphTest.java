package works.hop.db.api;

import org.junit.Test;

import java.util.*;

public class NodeGraphTest {

    private TestLoader loader = new TestLoader();
    private NodeLinkRecord adjacency = new NodeLinkRecord();

    @Test
    public void testAddingEdgeManually() {
        NodeGraph.Graph graph = new NodeGraph.Graph();

        List<Node> links = Arrays.asList(new Node("tbl_user", "user_account", 1L), new Node("tbl_account", "id", 1L),
                new Node("tbl_user", "user_account", 2L), new Node("tbl_account", "id", 2L),
                new Node("tbl_user", "user_account", 3L), new Node("tbl_account", "id", 3L),
                new Node("tbl_user", "user_account", 4L), new Node("tbl_account", "id", 4L),
                new Node("tbl_user", "user_account", 5L), new Node("tbl_account", "id", 5L));

        Long from = null;
        for(int i = 0; i < links.size(); i++) {
            if (i % 2 > 0) {
                Long to = graph.addVertex(links.get(i));
                graph.addEdge(from, to);
            } else {
                from = graph.addVertex(links.get(i));
            }
        }

        //print graph
        graph.bfs(0L);
        System.out.println("bfs <------------> dfs");
        graph.dfs(0L);
    }

    @Test
    public void testAddingEdgeFromDb() {
        loader.load();
        List<LongNodeLink> links = adjacency.selectList("select * from tbl_node_links", pst -> {
        });
        System.out.printf("%d links loaded%n", links.size());
        NodeGraph.Graph graph = new NodeGraph.Graph();
        for (NodeLink link : links) {
            graph.addEdge(link.from, link.to);
        }
        //graph.printGraph();
        //PrintVisitor printer = new PrintVisitor();
        graph.bfs(0L);
        //System.out.println("visits made -> " + printer.counter);
        System.out.println("bfs <------------> dfs");
        graph.dfs(0L);
    }

    @Test
    public void getAllMembersOfTeamsFromAGivenCity() {
        //link-name::location-address,from
        String query = "select * from tbl_node_links where link_name='location-address'";
    }

    private class PrintVisitor implements Visitor {

        public Integer counter = 0;
        private Set<Node> visited = new TreeSet<>();

        @Override
        public Boolean isVisited(Node node) {
            return visited.contains(node);
        }

        @Override
        public Set<Node> getVisited() {
            return Collections.unmodifiableSet(visited);
        }

        @Override
        public void visit(Node node) {
            if (!isVisited(node)) {
                counter++;
                System.out.println("(" + counter + ") visited node -> " + node);
                visited.add(node);
            }
        }
    }
}
