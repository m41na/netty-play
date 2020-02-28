package works.hop.db.api;

import works.hop.db.api.Graphs.AttrsBuilder;
import works.hop.db.api.Graphs.TagEl;
import works.hop.db.api.Graphs.PrintOutVisitor;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static works.hop.db.api.Graphs.*;

public class TestGraphs {

    @Test
    public void testInitializeGraph(){
        Graph graph = createWeightedGraph();
        assertEquals("Expecting to have and empty collection of edges", graph.adj().size(), 0);
        String str = printGraph(graph);
        assertEquals("Expecting no content", "", str);
    }

    @Test
    public void testInitializeGraphWithEdges(){
        Graph<Character> graph = createWeightedGraph();
        List<Vertex<Character>> vertices = Arrays.asList('A','B','C','D','E','F').stream().map(graph::createVertex).collect(Collectors.toList());
        graph.addEdge(vertices.get(0),vertices.get(1));
        graph.addEdge(vertices.get(1),vertices.get(2));
        graph.addEdge(vertices.get(2),vertices.get(0));
        graph.addEdge(vertices.get(2),vertices.get(1));
        graph.addEdge(vertices.get(3),vertices.get(2));
        graph.addEdge(vertices.get(4),vertices.get(5));
        graph.addEdge(vertices.get(5),vertices.get(4));
        assertEquals("Expecting to have 6 of edges", graph.adj().size(), 6);
        String str = printGraph(graph);
        assertEquals("Expecting content generated from weighted graph nodes", "" +
                "(0 --> A)\t(2 --> C)\t(2 --> C)\t\n" +
                "(1 --> B)\t(0 --> A)\t(1 --> B)\t(3 --> D)\t\n" +
                "(2 --> C)\t\n" +
                "(5 --> F)\t(5 --> F)\t\n" +
                "(4 --> E)\t(4 --> E)\t\n" +
                "(1 --> B)\t(2 --> C)\t\n", str);
    }

    @Test
    public void testInitializeGraphWithVertices() {
        Graph graph = createWeightedGraph();
        Vertex div = graph.createVertex(new TagEl("div", true, "", AttrsBuilder.emptyAttrs()));
        Vertex form = graph.createVertex(new TagEl("form", true, "", AttrsBuilder.emptyAttrs()));
        graph.addEdge(div, form);
        Vertex p1 = graph.createVertex(new TagEl("p", true, "", AttrsBuilder.emptyAttrs()));
        graph.addEdge(form, p1);
        Vertex input1 = graph.createVertex(new TagEl("input", false, "", AttrsBuilder.newBuilder().attr("name", "username").attr("type", "text").build()));
        graph.addEdge(p1, input1);
        Vertex p2 = graph.createVertex(new TagEl("p", true, "", AttrsBuilder.emptyAttrs()));
        graph.addEdge(form, p2);
        Vertex input2 = graph.createVertex(new TagEl("input", false, "", AttrsBuilder.newBuilder().attr("name", "password").attr("type", "password").build()));
        graph.addEdge(p2, input2);
        Vertex p3 = graph.createVertex(new TagEl("p", true, "", AttrsBuilder.emptyAttrs()));
        graph.addEdge(form, p3);
        Vertex input3 = graph.createVertex(new TagEl("input", false, "", AttrsBuilder.newBuilder().attr("value", "Send").attr("type", "submit").build()));
        graph.addEdge(p3, input3);

        assertEquals("Expecting to have 8 of edges", 8, graph.adj().size());
        String str = printGraph(graph);
        assertEquals("Expecting content generated from weighted graph nodes", "" +
                "(0 --> div)\t(2 --> p)\t(4 --> p)\t(6 --> p)\t\n" +
                "(1 --> form)\t(3 --> input)\t\n" +
                "(2 --> p)\t\n" +
                "(1 --> form)\t(5 --> input)\t\n" +
                "(4 --> p)\t\n" +
                "(1 --> form)\t(7 --> input)\t\n" +
                "(6 --> p)\t\n" +
                "(1 --> form)\t\n", str);

        //test printing dsf
        PrintOutVisitor printer = new PrintOutVisitor();
        printTagsDFS(div, graph, printer);
        assertEquals("<div><form><p><input><p><input><p><input>", printer.result());

        //test printing bfs
        printer = new PrintOutVisitor();
        printTagsBFS(div, graph, printer);
        assertEquals("<div><form><p><p><p><input><input><input>", printer.result());

        //test get all edges
        List<Edge<TagEl>> edges = graph.edges();
        assertEquals("Expecting 14", 14, edges.size());
    }

    @Test
    public void testInitializeGraphWithVertices2() {
        Graph graph = createWeightedGraph();
        Vertex v0 = graph.createVertex(0);
        Vertex v1 = graph.createVertex(1);
        Vertex v2 = graph.createVertex(2);
        Vertex v3 = graph.createVertex(3);
        Vertex v4 = graph.createVertex(4);
        Vertex v5 = graph.createVertex(5);
        Vertex v6 = graph.createVertex(6);
        Vertex v7 = graph.createVertex(7);
        Vertex v8 = graph.createVertex(8);
        Vertex v9 = graph.createVertex(9);
        Vertex v10 = graph.createVertex(10);
        Vertex v11 = graph.createVertex(11);
        Vertex v12 = graph.createVertex(12);
        graph.addEdge(v0, v1);
        graph.addEdge(v0, v9);
        graph.addEdge(v1, v8);
        graph.addEdge(v8, v7);
        graph.addEdge(v8, v9);
        graph.addEdge(v7, v10);
        graph.addEdge(v7, v11);
        graph.addEdge(v10, v11);
        graph.addEdge(v7, v6);
        graph.addEdge(v7, v3);
        graph.addEdge(v6, v5);
        graph.addEdge(v3, v5);
        graph.addEdge(v3, v4);
        graph.addEdge(v3, v2);

        assertEquals("Expecting to have 14 of edges", 13, graph.adj().size());
        String str = printGraph(graph);
        assertEquals("Expecting content generated from weighted graph nodes", "" +
                "(0 --> 0)\t(8 --> 8)\t\n" +
                "(3 --> 3)\t\n" +
                "(7 --> 7)\t(5 --> 5)\t(4 --> 4)\t(2 --> 2)\t\n" +
                "(3 --> 3)\t\n" +
                "(6 --> 6)\t(3 --> 3)\t\n" +
                "(7 --> 7)\t(5 --> 5)\t\n" +
                "(8 --> 8)\t(10 --> 10)\t(11 --> 11)\t(6 --> 6)\t(3 --> 3)\t\n" +
                "(1 --> 1)\t(7 --> 7)\t(9 --> 9)\t\n" +
                "(0 --> 0)\t(8 --> 8)\t\n" +
                "(7 --> 7)\t(11 --> 11)\t\n" +
                "(7 --> 7)\t(10 --> 10)\t\n" +
                "\n" +
                "(1 --> 1)\t(9 --> 9)\t\n", str);

        //test printing dsf
        PrintOutVisitor printer = new PrintOutVisitor();
        printDFS(v0, graph, printer, node -> node.value.toString().concat(","));
        assertEquals( "0,1,8,7,10,11,6,5,3,4,2,9,", printer.result());

        //test printing bfs
        printer = new PrintOutVisitor();
        printBFS(v0, graph, printer, node -> node.value.toString().concat(","));
        assertEquals("0,1,9,8,7,10,11,6,3,5,4,2,", printer.result());

        //test get all edges
        List<Edge<Integer>> edges = graph.edges();
        assertEquals("Expecting 28", 28, edges.size());
    }
}
