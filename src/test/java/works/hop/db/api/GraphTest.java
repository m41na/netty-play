package works.hop.db.api;

import org.junit.Test;

import java.util.List;

public class GraphTest {

    private Graph graph = new Graph();
    private TestLoader loader = new TestLoader();
    private AdjacencyLinkRecord adjacency = new AdjacencyLinkRecord();

    @Test
    public void testAddingEdgeManually() {
        graph.addEdge(new Node("tbl_user", "user_account", 1L), new Node("tbl_account", "id", 1L));
        graph.addEdge(new Node("tbl_user", "user_account", 2L), new Node("tbl_account", "id", 2L));
        graph.addEdge(new Node("tbl_user", "user_account", 3L), new Node("tbl_account", "id", 3L));
        graph.addEdge(new Node("tbl_user", "user_account", 4L), new Node("tbl_account", "id", 4L));
        graph.addEdge(new Node("tbl_user", "user_account", 5L), new Node("tbl_account", "id", 5L));

        //print graph
        graph.printGraph();
    }

    @Test
    public void testAddingEdgeFromDb() {
        loader.load();
        List<AdjacencyLink> links = adjacency.selectList("select * from tbl_adjacency_links", pst -> {});
        System.out.printf("%d links loaded%n", links.size());
        for(AdjacencyLink link : links){
            graph.addEdge(link.from, link.to);
        }
        graph.printGraph();
    }

    @Test
    public void getAllMembersOfTeamsFromAGivenCity(){
        //link-name::location-address,from
        String query = "select * from tbl_adjacency_links where link_name='location-address'";
    }
}
