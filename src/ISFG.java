import java.util.ArrayList;

public interface ISFG {

    public void addNode(Node node);

    public Node getNode(String name);

    public Node[] getNodes();

    public ArrayList<ArrayList<String>> forwardPaths(Node source, Node destination);

    public ArrayList<ArrayList<Double>> forwardGains();

    public ArrayList<ArrayList<String>> loops();

    public ArrayList<ArrayList<Double>> loopGains();

}
