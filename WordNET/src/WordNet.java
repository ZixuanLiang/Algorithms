import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class WordNet {
    private final Digraph wordNet;
    private final Graph undirectedWordNet;
    private final String[] synSets;
    private final int[] ifAlreadyReturned;//indicate whether the synset at an index has been returned when iterating,
                                          // the default value is 0, 1 means it has been returned
    private final int size;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        argumentCheck(synsets);
        argumentCheck(hypernyms);
        synSets = constructSynSets(synsets);
        try {
            size = (int) Files.lines(Paths.get(synsets)).count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        wordNet = new Digraph(size);
        undirectedWordNet = new Graph(size);
        ifAlreadyReturned = new int[size];
        buildWordNet(hypernyms);
        rooted_DAG_Check();
        sap = new SAP(wordNet);
    }
    private void rooted_DAG_Check() {
        //cycle check
        DirectedCycle cycle = new DirectedCycle(this.wordNet);
        if (cycle.hasCycle()) {
            throw new IllegalArgumentException();
        }
        //root check
        int rootCounter = 0;
        for (int i = 0; i < size; i++) {
            if (wordNet.outdegree(i) == 0) {
                rootCounter++;
            }
        }
        if (rootCounter != 0) {
            throw new IllegalArgumentException();
        }
    }
    private String[] constructSynSets (String filename){
        String[] synSets = new String[size];
        In in = new In(filename);
        for (int i = 0; i < size; i++) {
            synSets[i] = in.readLine().split(",")[1];
        }
        return synSets;
    }
    private void buildWordNet (String hypernyms) {
        In in = new In(hypernyms);
        String[] w;
        for (int i = 0; i < size; i++) {
            w = in.readLine().split(",");
            for (int j = 0; j < w.length; j++) {
                wordNet.addEdge(i,Integer.parseInt(w[j]));
                undirectedWordNet.addEdge(i,Integer.parseInt(w[j]));
            }
        }
    }
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return List.of(synSets);
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        argumentCheck(word);
        if (binarySearch(word) < 0) {
            return false;
        } else {
            return true;
        }
    }
    private int binarySearch(String word) {
        int low = 0, hi = size - 1;
        while (low <= hi) {
            int mid = low + (hi - low) / 2;
            if (word.compareTo(synSets[mid]) < 0) {
                hi = mid - 1;
            } else if (word.compareTo(synSets[mid]) > 0) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return sap.length(binarySearch(nounA), binarySearch(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return synSets[sap.ancestor(binarySearch(nounA), binarySearch(nounB))];
    }
    private void argumentCheck(Object args) {
        if (args == null) {
            throw new IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        
    }
}