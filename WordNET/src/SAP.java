import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


import java.util.Iterator;

public class SAP {
    private final Digraph wordNet;

    private int ancestor;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        wordNet = G;
        ancestor = Integer.MAX_VALUE;
    }

    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths fromV = new BreadthFirstDirectedPaths(wordNet, v);
        BreadthFirstDirectedPaths fromW = new BreadthFirstDirectedPaths(wordNet, w);
        int size = wordNet.V();
        int minDistance = size + 1;
        int distance;
        for (int i = 0; i < size; i++) {
            if (fromV.hasPathTo(i) && fromW.hasPathTo(i)) {
                distance = fromV.distTo(i) + fromW.distTo(i);
                if (distance < minDistance) {
                    ancestor = i;
                    minDistance = distance;
                }
            }
        }
        return minDistance == (size + 1) ? -1 : minDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path

    public int ancestor(int v, int w) {
        if (length(v, w) == -1) {
            return -1;
        }
        return ancestor;
    }
    private int count(Iterable<Integer> path) {
        if (path == null) {
            return -1;
        }
        int counter = 0;
        for (int i : path) {
            counter++;
        }
        return counter;
    }
    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (count(v) == 0 || count(w) == 0) {
            throw new IllegalArgumentException();
        }
        BreadthFirstDirectedPaths vPath = new BreadthFirstDirectedPaths(wordNet, v);
        BreadthFirstDirectedPaths wPath = new BreadthFirstDirectedPaths(wordNet, w);
        int size = wordNet.V(), shortestPathSoFar = size + 1;
        for (int i = 0; i < size; i++) {
            if (vPath.hasPathTo(i) && wPath.hasPathTo(i)) {
                if (vPath.distTo(i) + wPath.distTo(i) < shortestPathSoFar) {
                    this.ancestor = i;
                    shortestPathSoFar = vPath.distTo(i) + wPath.distTo(i);
                }
            }
        }
        return shortestPathSoFar == size + 1 ? -1 : shortestPathSoFar;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (length(v, w) == -1) {
            return -1;
        } else {
            return ancestor;
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
