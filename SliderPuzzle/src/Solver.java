import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;
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
        public int compareTo(Node another){
            return this.priority - another.priority;
        }
    }
    // find a solution to the initial board (using the A* algorithm)
    private boolean ifSolved = false;
    private Node finalNode = null;
    public Solver(Board initial) {
        MinPQ<Node> queueA = new MinPQ<Node>();
        MinPQ<Node> queueB = new MinPQ<Node>();
        Node nodeA = new Node(initial, 0, initial.manhattan(), null);
        Node nodeB = new Node(initial.twin(), 0, initial.manhattan(), null);
        queueA.insert(nodeA);
        queueB.insert(nodeB);
        alternatelySolveTwoQueues(queueA,queueB, 1);
    }
    private void solve(MinPQ<Node> queue, int which){
        Node min = queue.delMin();
        Iterable<Board> boardList = min.board.neighbors();
        for (Board b : boardList) {
            if (b == min.parent.board){
                continue;
            }
            queue.insert(new Node(b, min.moves + 1, b.manhattan() + min.moves + 1, min));
        }
        if (queue.min().board.isGoal()) {
            if (which > 0) {
                this.ifSolved = true;
                this.finalNode = queue.delMin();
            } else {
                this.
            }
        }
    }
    private void alternatelySolveTwoQueues(MinPQ<Node> queueA, MinPQ<Node> queueB, int which){
        if (which > 0) {
            solve(queueA, which);
            if (!this.ifSolved) {
                alternatelySolveTwoQueues(queueA,queueB, which * (-1));
            }
        } else if (which < 0) {
            solve(queueB, which);
            if (!this.ifSolved){
                alternatelySolveTwoQueues(queueA, queueB, which * (-1));
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return ifSolved;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (ifSolved){
            return finalNode.moves;
        } else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!ifSolved) {
            return null;
        }
        List<Board> boardList = new ArrayList<>();
        Node n = finalNode;
        boardList.add(n.board);
        while (n.parent != null) {
            n = n.parent;
            boardList.add(n.board);
        }
        return boardList;
    }

}
