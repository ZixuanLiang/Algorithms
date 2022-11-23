import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;
    public Outcast(WordNet wordnet){
        this.wordNet = wordnet;
    }         // constructor takes a WordNet object
    public String outcast(String[] nouns) {
        int maxDistance = -1;
        int index = -1;
        for (int h = 0; h < nouns.length; h++) {
            int sumDistance = 0;
            for (int i = 0; i < nouns.length; i++) {
                if (h == i) {
                    continue;
                }
                sumDistance += wordNet.distance(nouns[h], nouns[i]);
            }
            if (maxDistance < sumDistance) {
                maxDistance = sumDistance;
                index = h;
            }
        }
        return nouns[index];
    }   // given an array of WordNet nouns, return an outcast
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}