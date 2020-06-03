import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

public class SFG implements ISFG {

    private int size;
    private int counter;
    private Node nodes[];
    private HashMap<String, Integer> indices;
    private ArrayList<ArrayList<String>> forwardPaths;
    private ArrayList<ArrayList<Double>> forwardGains;
    private ArrayList<ArrayList<String>> loops;
    private ArrayList<ArrayList<Double>> loopGains;

    public SFG(int size) {
        this.size = size;
        counter = 0;
        nodes = new Node[size];
        indices = new HashMap<String, Integer>();
        forwardPaths = new ArrayList<ArrayList<String>>();
        forwardGains = new ArrayList<ArrayList<Double>>();
        loops = new ArrayList<ArrayList<String>>();
        loopGains = new ArrayList<ArrayList<Double>>();
    }

    @Override
    public void addNode(Node node) {
        indices.put(node.name(), counter);
        nodes[counter] = node;
        counter++;
    }

    @Override
    public Node getNode(String name) {
        return nodes[indices.get(name)];
    }

    @Override
    public Node[] getNodes() {
        return nodes;
    }

    @Override
    public ArrayList<ArrayList<String>> forwardPaths(Node source, Node destination) {
        boolean visited[] = new boolean[size]; // initially false
        forwardPaths(source, destination, visited, new ArrayList<String>(), new ArrayList<Double>());
        return forwardPaths;
    }

    @Override
    public ArrayList<ArrayList<Double>> forwardGains() {
        return forwardGains;
    }

    @Override
    public ArrayList<ArrayList<String>> loops() {
        boolean visited[] = new boolean[size]; // initially false
        for (int i = 0; i < nodes.length; i++)
            loops(nodes[i], nodes[i], false, visited, new ArrayList<String>(), new ArrayList<Double>());
        return loops;
    }

    @Override
    public ArrayList<ArrayList<Double>> loopGains() {
        return loopGains;
    }


    private void forwardPaths(Node curNode, Node destination, boolean visited[],
                              ArrayList<String> currentPath, ArrayList<Double> currentGain) {
        int nodeIndex = (int) indices.get(curNode.name());
        visited[nodeIndex] = true;
        currentPath.add(curNode.name());
        if (curNode.name().equals(destination.name())) { // Destination reached
            forwardPaths.add(new ArrayList<>(currentPath));
            forwardGains.add(new ArrayList<>(currentGain));
        }
        else {
            Iterator<Edge> i = nodes[nodeIndex].edges().listIterator();
            while (i.hasNext()) {
                Edge edge = i.next();
                Node nextNode = edge.to();
                if (!visited[(int) indices.get(nextNode.name())]) {
                    currentGain.add(edge.gain());
                    forwardPaths(nextNode, destination, visited, currentPath, currentGain);
                }
            }
        }
        currentPath.remove(currentPath.size() - 1);
        if (!currentGain.isEmpty())
            currentGain.remove(currentGain.size() - 1);
        visited[nodeIndex] = false;
    }

    private void loops(Node curNode, Node destination, boolean visit, boolean visited[],
                       ArrayList<String> currentLoop, ArrayList<Double> currentGain) {
        int nodeIndex = (int) indices.get(curNode.name());
        visited[nodeIndex] = visit;
        currentLoop.add(curNode.name());
        if (curNode.name().equals(destination.name()) && visit) // Destination reached
            addLoop(currentLoop, currentGain);
        else {
            Iterator<Edge> i = nodes[nodeIndex].edges().listIterator();
            while (i.hasNext()) {
                Edge edge = i.next();
                Node nextNode = edge.to();
                if (!visited[(int) indices.get(nextNode.name())]) {
                    currentGain.add(edge.gain());
                    loops(nextNode, destination, true, visited, currentLoop, currentGain);
                }
            }
        }
        currentLoop.remove(currentLoop.size() - 1);
        if (!currentGain.isEmpty())
            currentGain.remove(currentGain.size() - 1);
        visited[nodeIndex] = false;
    }

    private void addLoop(ArrayList<String> currentLoop, ArrayList<Double> currentGain) {
        int counter;
        boolean exist = false;
        for (ArrayList<String> loop : loops) {
            counter = 0;
            if (loop.size() == currentLoop.size()) {
                for (String node : loop)
                    if (currentLoop.contains(node))
                        counter++;
                if (counter == loop.size())
                    exist = true;
            }
            if (exist)
                break;
        }
        if (!exist) {
            loops.add(new ArrayList<>(currentLoop));
            loopGains.add(new ArrayList<>(currentGain));
        }
    }
    public static <T> void print(ArrayList<ArrayList<T>> arr) {
        for (int i = 0; i < arr.size(); i++) {
            for (int j = 0; j < arr.get(i).size(); j++)
                System.out.print(arr.get(i).get(j) + " ");
            System.out.println();
        }
    }
}
