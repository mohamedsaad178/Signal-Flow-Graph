public class Edge {

    private Double gain;
    private Node toNode;

    public Edge(Double gain, Node toNode) {
        this.gain = gain;
        this.toNode = toNode;
    }

    public Double gain() {
        return gain;
    }

    public Node to() {
        return toNode;
    }
}