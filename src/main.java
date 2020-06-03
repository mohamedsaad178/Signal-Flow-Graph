import java.util.ArrayList;
import java.util.Scanner;


public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Number of Nodes in the graph :");
        int numNodes = scanner.nextInt();
        System.out.print("Enter Number of Branches in the graph :");
        int numBranches = scanner.nextInt();
        System.out.print("Enter the Number of the input (source) Node :");
        int startNode = scanner.nextInt();
        System.out.print("Enter the Number of the output (end) Node :");
        int endNode = scanner.nextInt();
        //
        int [] xbranch=new int[numBranches];
        int [] ybranch=new int[numBranches];
        double [] wbranch=new double[numBranches];
        for (int i=1; i<=numBranches; i++){
            System.out.print("Enter branch "+i+" :");
            xbranch[i-1]=scanner.nextInt();
            ybranch[i-1]=scanner.nextInt();
            wbranch[i-1]=scanner.nextDouble();
        }
        SFG g = new SFG(numNodes);
        for (int i=1; i<=numNodes; i++){
            Node y = new Node("y"+i);
            g.addNode(y);
        }
        for (int i=0; i<numBranches; i++){
            g.getNode("y"+xbranch[i]).addEdge(new Edge(wbranch[i], g.getNode("y"+ybranch[i]) ) );
        }

        ArrayList<ArrayList<String>> forwardPaths = g.forwardPaths(g.getNode("y"+startNode), g.getNode("y"+endNode));
        System.out.println("Following are the " + forwardPaths.size() +  " different forward paths from y1 to y7:");
        g.print(forwardPaths);

        ArrayList<ArrayList<Double>> forwardGains = g.forwardGains();
        System.out.println("Following are the corresponding forward gains from y"+startNode+" to y"+endNode+ " :");
        g.print(forwardGains);

        ArrayList<ArrayList<String>> loops = g.loops();
        System.out.println("Following are the " + loops.size() +  " different loops in the SFG:");
        g.print(loops);

        ArrayList<ArrayList<Double>> loopGains = g.loopGains();
        System.out.println("Following are the corresponding loops gains in the SFG:");
        g.print(loopGains);

        MasonSolver solver = new MasonSolver(forwardPaths, forwardGains, loops, loopGains);
        System.out.println("Delta = " + solver.calculateDeltaTotal());

        int i = 0;
        for (ArrayList<String> forwardPath : forwardPaths)
            System.out.println("Delta-" + i++ + " = " + solver.calculateDelta(forwardPath));

        System.out.println("TF = " + solver.calculateTF());
    }

}
