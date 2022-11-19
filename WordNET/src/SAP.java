import edu.princeton.cs.algs4.*;

import java.util.Iterator;

public class SAP {
    private final Digraph wordNet;
    private final Graph unDirectedGraph;
    private int ancestor;
//    private final DepthFirstOrder topological;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        wordNet = G;
        unDirectedGraph = constructUnDirected(G);
        ancestor = Integer.MAX_VALUE;
//        topological = new DepthFirstOrder(G);
    }
    private Graph constructUnDirected(Digraph G) {
        int size = G.V();
        Graph graph = new Graph(size);
        for (int i = 0; i < size; i++) {
            Iterable<Integer> adj = G.adj(i);
            for (int j : adj) {
                graph.addEdge(i,j);
            }
        }
        return graph;
    }
    private Iterable<Integer> pathTo(int v, int w) {
        BreadthFirstPaths paths = new BreadthFirstPaths(unDirectedGraph, v);
        return paths.pathTo(w);
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
    // length of the shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return count(pathTo(v,w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (pathTo(v, w) == null) {
            return -1;
        }
        Iterable<Integer> pathFromAToB = pathTo(v, w);
        Iterator<Integer> iterator = pathFromAToB.iterator();
        int curr = iterator.next();
        while (true) {
            int next = iterator.next();
            int counter = 0;
            int numOfAdj = count(wordNet.adj(curr));
            for (int i : wordNet.adj(v)) {
                if (i == next) {
                    break;
                } else if (counter == numOfAdj - 1) {
                    return curr;
                }
                counter++;
            }
            curr = next;
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
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

    }
}
