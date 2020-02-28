package works.hop.db.api;

import org.junit.Test;
import works.hop.db.api.Graphs.*;

import java.util.List;

import static works.hop.db.api.Graphs.*;

public class GraphTest {

    private TestLoader loader = new TestLoader();
    private NodeLinkRecord adjacency = new NodeLinkRecord();

    @Test
    public void testAddingEdgeManually() {
        Graph graph = new Graph();

        //for each table in use, link to the root "db" node
        Node tables = new Node("db", "database tables", 0);
        Vertex<Node> dbTables = graph.createVertex(tables);
        Node tbl_account = new Node("tbl_account", "database account table", 0);
        Vertex<Node> accountsTable = graph.createVertex(tbl_account);
        graph.addEdge(dbTables, accountsTable);
        Node tbl_user = new Node("tbl_user", "database users table", 0);
        Vertex<Node> usersTable = graph.createVertex(tbl_user);
        graph.addEdge(dbTables, usersTable);
        Node tbl_address = new Node("tbl_address", "database addresses table", 0);
        Vertex<Node> addressesTable = graph.createVertex(tbl_address);
        graph.addEdge(dbTables, addressesTable);
        Node tbl_team = new Node("tbl_team", "database teams table", 0);
        Vertex<Node> teamsTable = graph.createVertex(tbl_team);
        graph.addEdge(dbTables, teamsTable);
        Node tbl_location = new Node("tbl_location", "database locations table", 0);
        Vertex<Node> locationsTable = graph.createVertex(tbl_location);
        graph.addEdge(dbTables, locationsTable);
        Node tbl_team_members = new Node("tbl_team_members", "database team members table", 0);
        Vertex<Node> teamMembersTable = graph.createVertex(tbl_team_members);
        graph.addEdge(dbTables, teamMembersTable);
        Node tbl_users_network = new Node("tbl_users_network", "database users network table", 0);
        Vertex<Node> usersNetworkTable = graph.createVertex(tbl_users_network);
        graph.addEdge(dbTables, usersNetworkTable);

        //for each table in use, create a vertex for its primary and foreign key columns
        Node acc_id = new Node("account_id", "id", 0);
        Vertex<Node> accId = graph.createVertex(acc_id);
        graph.addEdge(accountsTable, accId);
        Node user_id = new Node("user_id", "id", 0);
        Vertex<Node> userId = graph.createVertex(user_id);
        graph.addEdge(usersTable, userId);
        Node address_id = new Node("address_id", "id", 0);
        Vertex<Node> addressId = graph.createVertex(address_id);
        graph.addEdge(addressesTable, addressId);
        Node team_id = new Node("team_id", "id", 0);
        Vertex<Node> teamId = graph.createVertex(team_id);
        graph.addEdge(teamsTable, teamId);
        Node fk_captain_id = new Node("fk_captain_id", "captain", 0);
        Vertex<Node> fkCaptainId = graph.createVertex(fk_captain_id);
        graph.addEdge(teamsTable, fkCaptainId);
        Node fk_location_id = new Node("fk_location_id", "location", 0);
        Vertex<Node> fkLocationId = graph.createVertex(fk_location_id);
        graph.addEdge(teamsTable, fkLocationId);
        Node loc_id = new Node("location_id", "id", 0);
        Vertex<Node> locationId = graph.createVertex(loc_id);
        graph.addEdge(locationsTable, locationId);
        Node fk_team_member_player = new Node("fk_team_member_player", "player", 0);
        Vertex<Node> fkPlayerId = graph.createVertex(fk_team_member_player);
        graph.addEdge(teamMembersTable, fkPlayerId);
        Node fk_team_members_team = new Node("fk_team_members_team", "team", 0);
        Vertex<Node> fkTeamId = graph.createVertex(fk_team_members_team);
        graph.addEdge(teamMembersTable, fkTeamId);
        Node fk_users_network_follower = new Node("fk_users_network_follower", "follower", 0);
        Vertex<Node> fkFollowerId = graph.createVertex(fk_users_network_follower);
        graph.addEdge(usersNetworkTable, fkFollowerId);
        Node fk_users_network_followee = new Node("fk_users_network_followee", "followee", 0);
        Vertex<Node> fkFolloweeId = graph.createVertex(fk_users_network_followee);
        graph.addEdge(usersNetworkTable, fkFolloweeId);

        //associate accounts an accountId
        Node a1 = new Node("tbl_account", "id", 1L);
        Vertex<Node> account1 = graph.createVertex(a1);
        graph.addEdge(account1, accId);
        Node a2 = new Node("tbl_account", "id", 2L);
        Vertex<Node> account2 = graph.createVertex(a2);
        graph.addEdge(account2, accId);
        Node a3 = new Node("tbl_account", "id", 3L);
        Vertex<Node> account3 = graph.createVertex(a3);
        graph.addEdge(account3, accId);

        //associate users with a userId
        Node u1 = new Node("tbl_user", "id", 1L);
        Vertex<Node> user1 = graph.createVertex(u1);
        graph.addEdge(user1, userId);
        Node u2 = new Node("tbl_user", "id", 2L);
        Vertex<Node> user2 = graph.createVertex(u2);
        graph.addEdge(user2, userId);
        Node u3 = new Node("tbl_user", "id", 3L);
        Vertex<Node> user3 = graph.createVertex(u3);
        graph.addEdge(user3, userId);

        //associate addresses with an addressId
        Node ad1 = new Node("tbl_address", "id", 1L);
        Vertex<Node> address1 = graph.createVertex(ad1);
        graph.addEdge(address1, addressId);
        Node ad2 = new Node("tbl_address", "id", 2L);
        Vertex<Node> address2 = graph.createVertex(ad2);
        graph.addEdge(address2, addressId);
        Node ad3 = new Node("tbl_address", "id", 3L);
        Vertex<Node> address3 = graph.createVertex(ad3);
        graph.addEdge(address3, addressId);

        //associate locations with a locationId
        Node l1 = new Node("tbl_location", "id", 1L);
        Vertex<Node> location1 = graph.createVertex(l1);
        graph.addEdge(location1, locationId);

        Node l2 = new Node("tbl_location", "id", 2L);
        Vertex<Node> location2 = graph.createVertex(l2);
        graph.addEdge(location2, locationId);

        //associate teams with a teamId
        Node t1 = new Node("tbl_team", "id", 1L);
        Vertex<Node> team1 = graph.createVertex(t1);
        graph.addEdge(team1, teamId);

        Node t2 = new Node("tbl_team", "id", 2L);
        Vertex<Node> team2 = graph.createVertex(t2);
        graph.addEdge(team2, teamId);

        //associate team locations with a fkLocationId
        Node t1l = new Node("tbl_team", "location", 1L);
        Vertex<Node> team1Location = graph.createVertex(t1l);
        graph.addEdge(team1Location, fkLocationId);

        Node t2l = new Node("tbl_team", "location", 2L);
        Vertex<Node> team2Location = graph.createVertex(t2l);
        graph.addEdge(team2Location, fkLocationId);

        //associate team captains with a fkCaptainId
        Node t1c = new Node("tbl_team", "captain", 1L);
        Vertex<Node> team1Captain = graph.createVertex(t1c);
        graph.addEdge(team1Captain, fkCaptainId);

        //associate users with their accounts
        graph.addEdge(user1, account1);
        graph.addEdge(user2, account2);
        graph.addEdge(user3, account3);

        //associate locations with an address
        graph.addEdge(location1, address1);
        graph.addEdge(location2, address2);

        //associate team's location to a location
        graph.addEdge(team1, team1Location);
        graph.addEdge(team1Location, location1);
        graph.addEdge(team2, team2Location);
        graph.addEdge(team2Location, location2);
        //associate team's captain to a user
        graph.addEdge(team1, team1Captain);
        graph.addEdge(team1Captain, user1);

        //associate a team with team-members
        Node tm_t1 = new Node("tbl_team_members", "team", 1L);
        Vertex<Node> team_members_team1 = graph.createVertex(tm_t1);
        graph.addEdge(team_members_team1, fkTeamId);
        graph.addEdge(team_members_team1, team1);

        //associate a player with team-members
        Node tm_p1 = new Node("tbl_team_members", "player", 1L);
        Vertex<Node> team_members_player1 = graph.createVertex(tm_p1);
        graph.addEdge(team_members_player1, fkPlayerId);
        graph.addEdge(team_members_player1, team_members_team1);
        graph.addEdge(team_members_player1, user1);

        Node tm_p2 = new Node("tbl_team_members", "player", 2L);
        Vertex<Node> team_members_player2 = graph.createVertex(tm_p2);
        graph.addEdge(team_members_player2, fkPlayerId);
        graph.addEdge(team_members_player2, team_members_team1);
        graph.addEdge(team_members_player2, user2);

        Node tm_p3 = new Node("tbl_team_members", "player", 3L);
        Vertex<Node> team_members_player3 = graph.createVertex(tm_p3);
        graph.addEdge(team_members_player3, fkPlayerId);
        graph.addEdge(team_members_player3, team_members_team1);
        graph.addEdge(team_members_player3, user3);

        //associate follower with users_network
        Node fl1 = new Node("tbl_users_network", "follower", 1L);
        Vertex<Node> follower1 = graph.createVertex(fl1);
        graph.addEdge(follower1, fkFollowerId);
        graph.addEdge(follower1, user1);

        Node fl2 = new Node("tbl_users_network", "follower", 2L);
        Vertex<Node> follower2 = graph.createVertex(fl2);
        graph.addEdge(follower2, fkFollowerId);
        graph.addEdge(follower2, user2);

        Node fl3 = new Node("tbl_users_network", "follower", 3L);
        Vertex<Node> follower3 = graph.createVertex(fl3);
        graph.addEdge(follower3, fkFollowerId);
        graph.addEdge(follower3, user3);

        //associate followee with users_networks
        Node fr1 = new Node("tbl_users_network", "followee", 1L);
        Vertex<Node> followee1 = graph.createVertex(fr1);
        graph.addEdge(followee1, fkFolloweeId);
        graph.addEdge(followee1, user1);

        Node fr2 = new Node("tbl_users_network", "followee", 2L);
        Vertex<Node> followee2 = graph.createVertex(fr2);
        graph.addEdge(followee2, fkFolloweeId);
        graph.addEdge(followee2, user2);

        Node fr3 = new Node("tbl_users_network", "followee", 3L);
        Vertex<Node> followee3 = graph.createVertex(fr3);
        graph.addEdge(followee3, fkFolloweeId);
        graph.addEdge(followee3, user2);

        //associate followers with followees
        graph.addEdge(follower1, followee2);
        graph.addEdge(follower1, followee3);
        graph.addEdge(follower2, followee1);
        graph.addEdge(follower2, followee3);

        //print graph
        printGraph(graph);

        System.out.println("bfs Visits:");
        PrintOutVisitor visitor = new PrintOutVisitor();
        printBFS(dbTables, graph, visitor, v -> v.value.table + v.value.value + ",");
        System.out.println(visitor.result());

        System.out.println("dfs Visits:");
        visitor = new PrintOutVisitor();
        printDFS(dbTables, graph, visitor, v -> v.value.table + v.value.value + ",");
        System.out.println(visitor.result());
    }

    @Test
    public void testYetAnotherSimpleDemo() {
        Graph graph = new Graph();
        Vertex<Node> A = graph.createVertex(new Node("table", "column", 'A')); //0
        Vertex<Node> B = graph.createVertex(new Node("table", "column", 'B')); //1
        Vertex<Node> C = graph.createVertex(new Node("table", "column", 'C')); //2
        Vertex<Node> D = graph.createVertex(new Node("table", "column", 'D')); //3
        Vertex<Node> E = graph.createVertex(new Node("table", "column", 'E')); //4
        Vertex<Node> F = graph.createVertex(new Node("table", "column", 'F')); //5

        graph.addEdge(A, B); //0, 1
        graph.addEdge(B, C); //1, 2
        graph.addEdge(A, D); //0, 3
        graph.addEdge(D, E); //3, 4
        graph.addEdge(E, F); //4, 5
        graph.addEdge(B, E); //1, 4

        printGraph(graph);

        System.out.println("bfs Visits:");
        PrintOutVisitor visitor = new PrintOutVisitor();
        printBFS(A, graph, visitor, v -> v.value + ",");
        System.out.println(visitor.result());

        System.out.println("dfs Visits:");
        visitor = new PrintOutVisitor();
        printDFS(A, graph, visitor, node -> node.value.toString().concat(","));
        System.out.println(visitor.result());
    }

    @Test
    public void testAddingEdgeFromDb() {
        loader.load();
        List<LongNodeLink> links = adjacency.selectList("select * from tbl_node_links", pst -> {
        });
        System.out.printf("%d links loaded%n", links.size());
        Graph graph = new Graph();
//        for (NodeLink link : links) {
//            graph.createEdge(link.from, link.to);
//        }
//        //graph.printGraph();
//        //PrintVisitor printer = new PrintVisitor();
//        Graphs.bfs(graph, 0);
//        //System.out.println("visits made -> " + printer.counter);
//        System.out.println("bfs <------------> dfs");
//        Graphs.dfs(graph, 0);
    }

    @Test
    public void getAllMembersOfTeamsFromAGivenCity() {
        //link-name::location-address,from
        String query = "select * from tbl_node_links where link_name='location-address'";
    }
}
