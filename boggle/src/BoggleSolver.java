import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BoggleSolver
{
    private final Trie trie;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trie = new Trie();
        for (String s : dictionary) {
            trie.put(s, s.length());
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SolveBoard sb = new SolveBoard(board);
        return sb.validWords;
    }
    private class SolveBoard{
        private int col;
        private int row;
        private char[] boardArray;
        public final Set<String> validWords;
        private List<Integer> marked;

        public SolveBoard(BoggleBoard board)
        {
            row = board.rows();
            col = board.cols();
            boardArray = constructBoardArray(board);
            validWords = new HashSet<>();
            marked = new ArrayList<>();
            for (int i = 0; i < boardArray.length; i++) {
                marked.add(i);
                dfs(i, getCharFromIndex(i));
                marked.remove((Integer)i);
            }
        }
        private String getCharFromIndex(int index) {
            String s = String.valueOf(boardArray[index]);
            if (s.equals("Q")) {
                return "QU";
            } else {
                return s;
            }
        }
        private char[] constructBoardArray(BoggleBoard board)
        {
            char[] boardArray = new char[col * row];
            for (int m = 0; m < row; m++) {
                for (int n = 0; n < col; n++) {
                    boardArray[toIndex(m,n)] = board.getLetter(m,n);
                }
            }
            return boardArray;
        }
        private int toIndex(int row, int col) {return row*this.col + col;}
        private int toRow(int index) {return index / this.col;}
        private int toCol(int index) {return index % this.col;}
        private void dfs(int indexBoard, String s) {
            if (s.length() > 2) {
                if (trie.contains(s)) {
                    validWords.add(s);
                }
            }
            for (int i : adj(indexBoard)) {
                if (!marked.contains(i)) {
                    if (trie.isPath(s + getCharFromIndex(i))){
                        marked.add(i);
                        dfs(i, s + getCharFromIndex(i));
                        marked.remove((Integer)i);
                    }
                }
            }
        }
        private List<Integer> adj(int index) {
            int row = toRow(index);
            int col = toCol(index);
            List<Integer> list = new ArrayList<>();
            if (row - 1 >= 0) {
                list.add(index - this.col);
                if (col - 1 >= 0) list.add(index - this.col - 1);
                if (col+1 < this.col) list.add(index - this.col + 1);
            }
            if (col-1 >= 0) list.add(index-1);
            if (col+1 < this.col) list.add(index+1);
            if (row+1 < this.row) {
                list.add((index+this.col));
                if (col-1 >= 0) list.add(index+this.col-1);
                if (col+1 < this.col) list.add(index+this.col+1);
            }
            return list;
        }
    }



    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!trie.contains(word)) return 0;
        int l = word.length();
        if(l<3) return 0;
        else if (l <= 4) return 1;
        else if (l == 5) return 2;
        else if (l == 6) return 3;
        else if (l == 7) return 5;
        else return 11;
    }

    private static class Trie {
        private static final int R = 26;
        private Node root = new Node();
        private static class Node{
            private int value;
            private Node[] next = new Node[R];
        }
        public void put(String key, int val) {
            root = put(root, key, val, 0);
        }
        private Node put(Node x, String key, int val, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) { x.value = val; return x; }
            int c = key.charAt(d) - 65;
            x.next[c] = put(x.next[c], key, val, d+1);
            return x;
        }
        public boolean isPath(String key) {
            Node x = get(root, key, 0);
            if (x == null) return false;
            return true;
        }
        public boolean contains(String key) {
            return get(key) != 0;
        }
        public int get(String key) {
            Node x = get(root, key, 0);
            if (x == null) return 0;
            return x.value;
        }
        private Node get(Node x, String key, int d)
        {
            if (x == null) return null;
            if (d == key.length()) return x;
            int c = key.charAt(d) - 65;
            return get(x.next[c], key, d+1);
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
