import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;

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


    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
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
        return count(pathTo(nounA, nounB)) - 1;
    }
    private Iterable<Integer> pathTo(String nounA, String nounB) {
        BreadthFirstPaths paths = new BreadthFirstPaths(undirectedWordNet, binarySearch(nounA));
        return paths.pathTo(binarySearch(nounB));
    }
    private int count(Iterable<Integer> path) {
        int counter = 0;
        for (int i : path) {
            counter++;
        }
        return counter;
    }
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        Iterable<Integer> pathFromAToB = pathTo(nounA, nounB);
        Iterator<Integer> iterator = pathFromAToB.iterator();
        int v = iterator.next();
        while (true) {
            int next = iterator.next();
            int counter = 0;
            int numOfAdj = count(wordNet.adj(v));
            for (int w : wordNet.adj(v)) {
                if (w == next) {
                    break;
                } else if (counter == numOfAdj - 1) {
                    return synSets[v];
                }
                counter++;
            }
            v = next;
        }
    }

    // do unit testing of this class
    public static void main(String[] args)
}