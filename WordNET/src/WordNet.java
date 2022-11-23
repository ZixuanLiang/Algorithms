import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {
    private final Digraph wordNet;
    private final Graph undirectedWordNet;
    private final List<String> synSets; // list of each lines
    private final HashMap<String,Integer> nounMapToIndex; // map each word to its index
    private final int size;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        argumentCheck(synsets);
        argumentCheck(hypernyms);
        synSets = new ArrayList<>();
        nounMapToIndex = new HashMap<>();
        constructSynSets(synsets);
        size = synSets.size();
        wordNet = new Digraph(size);
        undirectedWordNet = new Graph(size);
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
        if (rootCounter != 1) {
            throw new IllegalArgumentException();
        }
    }
    private void constructSynSets (String filename){
        In in = new In(filename);
        String secondString;
        String[] synonyms, line;
        int index;
        while(!in.isEmpty()) {
            line = in.readLine().split(",");
            index = Integer.parseInt(line[0]);
            secondString = line[1];
            synonyms = line[1].split(" ");
            synSets.add(secondString);
            for (String s : synonyms) {
                nounMapToIndex.put(s, index);
            }
        }
    }
    private void buildWordNet (String hypernyms) {
        In in = new In(hypernyms);
        String[] w;
        for (int i = 0; i < size; i++) {
            w = in.readLine().split(",");
            for (int j = 1; j < w.length; j++) {
                wordNet.addEdge(i,Integer.parseInt(w[j]));
                undirectedWordNet.addEdge(i,Integer.parseInt(w[j]));
            }
        }
    }
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMapToIndex.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        argumentCheck(word);
        if (getIndexOfNoun(word) < 0) {
            return false;
        } else {
            return true;
        }
    }
    private int getIndexOfNoun(String word) {
        Object index = nounMapToIndex.get(word);
        if (index == null) {
            return -1;
        } else {
            return (int) index;
        }
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return sap.length(getIndexOfNoun(nounA), getIndexOfNoun(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return synSets.get(sap.ancestor(getIndexOfNoun(nounA), getIndexOfNoun(nounB)));
    }
    private void argumentCheck(Object args) {
        if (args == null) {
            throw new IllegalArgumentException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
    }
}