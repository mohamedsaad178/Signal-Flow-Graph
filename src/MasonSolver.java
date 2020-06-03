import java.util.ArrayList;

public class MasonSolver implements IMasonSolver {

    private ArrayList<Integer> combination;
    /* ArrayList to hold "indexes" of valid non touching loops in ascending order..
        eg. : all 2 non touching loops then all 3 non touching loops and so on.. */
    private ArrayList<ArrayList<Integer>> nonTouchingLoops;
    private ArrayList<ArrayList<String>> forwardPaths;
    private ArrayList<ArrayList<Double>> forwardGains;
    private ArrayList<ArrayList<String>> loops;
    private ArrayList<ArrayList<Double>> loopGains;
    ArrayList<Double> deltas;

    public MasonSolver(ArrayList<ArrayList<String>> forwardPaths, ArrayList<ArrayList<Double>> forwardGains,
                       ArrayList<ArrayList<String>> loops, ArrayList<ArrayList<Double>> loopGains) {
        this.forwardPaths = forwardPaths;
        this.forwardGains = forwardGains;
        this.loops = loops;
        this.loopGains = loopGains;
        combination = new ArrayList<>();
        nonTouchingLoops = new ArrayList<>();
        deltas=new ArrayList<>();
        getCombinations();
    }

    /* adds non touching loops ""INDEXES"" to an ArrayList to be used in calculations later*/
    private void addNonTouching(int length) {
        ArrayList<Integer> nonTouchingComb = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            nonTouchingComb.add(combination.get(i) - 1);
        }
        nonTouchingLoops.add(nonTouchingComb);
    }

    /*length is the length of the combination*/
    private void loopsCombination(int x, int length) {
        if (combination.size() == length) {
            if (isNonTouching(combination)) {
                addNonTouching(length);
            }
            return;
        }
        for (int i = x; i <= loops.size(); i++) {
            combination.add(i);
            loopsCombination(i + 1, length);
            combination.remove(combination.size() - 1);
        }
    }

    //checks if a group of loops are non touching to each other given their index in loops array.
    //7ngeb el index deh b2a mn el combinations ely 3mltlhom generation
    private boolean isNonTouching(ArrayList<Integer> loopsIndex) {
        boolean nonTouching = true;
        for (int i = 0; i < loopsIndex.size() && nonTouching == true; i++) {
            for (int j = i + 1; j < loopsIndex.size() && nonTouching == true; j++) {
                for (int k = 0; k < loops.get(loopsIndex.get(j) - 1).size(); k++) {
                    String letter = loops.get(loopsIndex.get(j) - 1).get(k);
                    if (loops.get(loopsIndex.get(i) - 1).indexOf(letter) >= 0) {
                        nonTouching = false;
                        break;
                    }
                }

            }
        }
        return nonTouching;

    }

    // loops to get combinations of different lengths
    private void getCombinations() {
        for (int i = 2; i <= loops.size(); i++) {
            loopsCombination(1, i);
        }
    }

    // returns an ArrayList of "INDEXES" of loops non touching with a certain forward path.
    private ArrayList<Integer> nonTouchingPath(ArrayList<String> path) {
        ArrayList<Integer> nonTouchingLoops = new ArrayList<>();
        for (int i = 0; i < loops.size(); i++) {
            boolean nonTouching = true;
            for (int j = 0; j < path.size(); j++) {
                if (loops.get(i).indexOf(path.get(j)) >= 0) {
                    nonTouching = false;
                    break;
                }
            }
            if (nonTouching)
                nonTouchingLoops.add(i);
        }
        return nonTouchingLoops;
    }

    @Override
    public Double calculateDeltaTotal() {
        double delta = 1.0;
        double loopGain = 1.0;
        int sign = -1;
        for (int i = 0; i < loops.size(); i++) {
            loopGain = 1.0;
            /*inner loop calculates gain of each loop*/
            for (int j = 0; j < loopGains.get(i).size(); j++) {
                loopGain *= loopGains.get(i).get(j);
            }
            delta += (loopGain * sign);
        }
        int previousSize = 1;
        for (int i = 0; i < nonTouchingLoops.size(); i++) {
            if (nonTouchingLoops.get(i).size() != previousSize) {
                sign *= -1;
            }
            previousSize = nonTouchingLoops.get(i).size();
            double gainProduct = 1.0;
            for (int j = 0; j < nonTouchingLoops.get(i).size(); j++) {
                loopGain = 1.0;
                /*inner loop calculates gain of each loop*/
                for (int k = 0; k < loopGains.get(nonTouchingLoops.get(i).get(j)).size(); k++) {
                    loopGain *= loopGains.get(nonTouchingLoops.get(i).get(j)).get(k);
                }
                gainProduct*=loopGain;

            }
            delta += (gainProduct* sign);
        }
        return delta;
    }

    /*calculates delta for a certain forward path*/
    @Override
    public Double calculateDelta(ArrayList<String> forwardPath) {
        double delta = 1.0;
        ArrayList<Integer> nonTouching = nonTouchingPath(forwardPath);
        for (int i = 0; i < nonTouching.size(); i++) {
            double loopGain = 1.0;
            /*inner loop calculates gain of each loop*/
            for (int j = 0; j < loopGains.get(nonTouching.get(i)).size(); j++) {
                loopGain *= loopGains.get(nonTouching.get(i)).get(j);
            }
            delta -= loopGain;
        }
        deltas.add(delta);
        return delta;
    }

    @Override
    public Double calculateTF() {
        double TF = 0.0;
        for (int i = 0; i < forwardPaths.size(); i++) {
            double pathGain = 1.0;
            for (int j = 0; j < forwardGains.get(i).size(); j++) {
                pathGain *= forwardGains.get(i).get(j);
            }
            TF += (pathGain * calculateDelta(forwardPaths.get(i)));
        }
        TF = TF / calculateDeltaTotal();
        return TF;

    }

    @Override
    public ArrayList<ArrayList<Integer>> getNonTouchingLoops() {
        return nonTouchingLoops;
    }

    @Override
    public ArrayList<Double> getDeltas() {
        return deltas;
    }
}
