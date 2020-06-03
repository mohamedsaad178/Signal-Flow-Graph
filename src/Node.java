import java.util.ArrayList;

public class Node {

    private String name;
    private ArrayList<Edge> edges;

    public Node(String name) {
        this.name = name;
        edges = new ArrayList<Edge>();
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public String name() {
        return name;
    }

    public ArrayList<Edge> edges() {
        return edges;
    }
}