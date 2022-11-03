import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

import java.util.ArrayList;
import java.util.List;


public class Solver {
    private static class Node implements Comparable<Node>{
        public final Board board;
        public final int moves;
        public final int priority;
        public final Node parent;
        public Node(Board b, int m, int priority, Node parent){
            this.board = b;
            this.moves = m;
            this.parent = parent;
            this.priority = priority;
        }
        @Override
        public int compareTo(Node another){
            return this.priority - another.priority;
        }
    }
    // find a solution to the initial board (using the A* algorithm)
    private int whichOneIsSolvable = 0; // 0 means unknown, 1 means the original is solvable,
    // -1 means the twin is solvable
    private Node finalNode = null;
    public Solver(Board initial) {
        if (initial.isGoal()) {
            whichOneIsSolvable = 1;
            finalNode = new Node(initial, 0, 0, null);
        } else {
            // A is the original, B is the twin which is used to know if the A is solvable
            MinPQ<Node> queueA = new MinPQ<Node>();
            MinPQ<Node> queueB = new MinPQ<Node>();
            Node nodeA = new Node(initial, 0, initial.hamming() + initial.manhattan(), null);
            Node nodeB = new Node(initial.twin(), 0, initial.hamming() + initial.manhattan(), null);
            queueA.insert(nodeA);
            queueB.insert(nodeB);
            alternatelySolveTwoQueuesUntilOneIsSolved(queueA,queueB, 1);
        }
    }
    private void solve(MinPQ<Node> queue, int which){
        Node min = queue.delMin();
        Iterable<Board> boardList = min.board.neighbors();
        for (Board b : boardList) {
            if (min.parent != null && b.equals(min.parent.board)){
                continue;
            }
            queue.insert(new Node(b, min.moves + 1, b.hamming() + b.manhattan() + min.moves + 1, min));
        }
        if (queue.min().board.isGoal()) {
            if (which > 0) {
                this.whichOneIsSolvable = 1;
                this.finalNode = queue.delMin();
            } else {
                this.whichOneIsSolvable = -1;
            }
        }
    }
    private void alternatelySolveTwoQueuesUntilOneIsSolved(MinPQ<Node> queueA, MinPQ<Node> queueB, int which){
        while (whichOneIsSolvable == 0) {
            if (which == 1) {
                solve(queueA, which);
            }else{
                solve(queueB, which);
            }
            which *= (-1);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return whichOneIsSolvable == 1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (this.isSolvable()){
            return finalNode.moves;
        } else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (whichOneIsSolvable == -1) {
            return null;
        }
        Stack<Board> boardStack = new Stack<>();
        Node n = finalNode;
        boardStack.push(n.board);
        while (n.parent != null) {
            n = n.parent;
            boardStack.push(n.board);
        }
        return boardStack;
    }
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}