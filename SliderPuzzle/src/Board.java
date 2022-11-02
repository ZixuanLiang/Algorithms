import java.util.*;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import static java.lang.Math.abs;

public class Board {
    private final int n;
    private final int[] board;
    private int indexOfEmpty;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)

    public Board(int[][] tiles){
        this.n = tiles.length;
        board = new int[n*n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[toIndex(i+1, j+1)] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    indexOfEmpty = toIndex(i+1, j+1);
                }
            }
        }

    }
    private Board(Board another){
        this.n = another.n;
        this.board = new int[n*n];
        System.arraycopy(another.board,0, this.board, 0, n*n);
        this.indexOfEmpty = another.indexOfEmpty;
    }
    // change (row, col) to the index in the board array
    private int toIndex(int row, int col){
         return (row - 1) * n + col - 1;
    }
    // get which row (row counts from 1, not 0)
    private int getRow(int index){
        return (index / n) + 1;
    }
    private int getCol(int index){
        return (index % n) + 1;
    }
    // string representation of this board
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                s.append(String.format("%2d ", board[toIndex(i, j)]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension(){
        return n;
    }

    // number of tiles out of place
    public int hamming(){
        int counts = 0;
        for (int i = 0; i < board.length - 1; i++) {
            if (board[i] != i + 1) {
                counts++;
            }
        }
        return counts;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        int distances = 0;
        for (int i = 0; i < board.length; i++) {
            if (i != indexOfEmpty && board[i] != i + 1) {
                distances += calculateManhattan(i);
            }
        }
        return distances;
    }
    // calculate the Manhattan distance of the value in board[index]
    private int calculateManhattan(int index){
        int value = board[index];
        int rightIndex = value - 1;

        int rightRow = getRow(rightIndex);
        int rightCol = getCol(rightIndex);
        int currRow = getRow(index);
        int currCol = getCol(index);
        return abs(rightCol - currCol) + abs(rightRow - currRow);
    }

    // is this board the goal board?
    public boolean isGoal(){
        if (manhattan() == 0) {
            return true;
        } else {
            return false;
        }
    }

    // does this board equal y?
    public boolean equals(Object y){
        Board other = (Board) y;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != other.board[i]) {
                return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors(){
        List<Board> boardList = new ArrayList<>();
        List<Integer> indexArray = new ArrayList<>();
        // meaning the empty cell is not at the last row, and it can move down
        if (getRow(indexOfEmpty) < n) {
            indexArray.add(indexOfEmpty + n);
        }
        // the empty cell is not at the first row, it can move up
        if (getRow(indexOfEmpty) > 1) {
            indexArray.add(indexOfEmpty - n);
        }
        // it can move right
        if (getCol(indexOfEmpty) < n) {
            indexArray.add(indexOfEmpty + 1);
        }
        // it can move left
        if (getCol(indexOfEmpty) > 1) {
            indexArray.add(indexOfEmpty - 1);
        }
        for (int i : indexArray) {
            Board newBoard = new Board(this);
            newBoard.exchange(i, indexOfEmpty);
            newBoard.indexOfEmpty = i;
            boardList.add(newBoard);
        }
        return boardList;
    }
    // exchange the board[i] with board[indexOfEmpty]
    private void exchange(int indexA, int indexB){
        int tmp = board[indexA];
        board[indexA] = board[indexB];
        board[indexB] = tmp;

    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        Board newBoard = new Board(this);
        newBoard.exchange(this.randomIntGenerator(), this.randomIntGenerator());
        return newBoard;
    }
    // generate an integer that is not equal to indexOfEmpty
    private int randomIntGenerator(){
        Random rand = new Random();
        int randInt = rand.nextInt(n*n);
        while (randInt == this.indexOfEmpty) {
            randInt = rand.nextInt(n*n);
        }
        return randInt;
    }

    // unit testing (not graded)
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