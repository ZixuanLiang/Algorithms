import edu.princeton.cs.algs4.In;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void manhattanDistanceTest() {
        // create the board from puzzle04
        Board a = getBoardFromFile("test/puzzle04.txt");
        assertEquals(4, a.manhattan());

        Board b = getBoardFromFile("puzzle00.txt");
        assertEquals(0, b.manhattan());
    }

    @Test
    public void neighborsTest() {
        Board original = getBoardFromFile("test/original.txt");
        for (Board i : original.neighbors()) {
            System.out.println(i.toString());
        }

    }

    private static Board getBoardFromFile(String filepath){
        In in = new In(filepath);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        return new Board(tiles);
    }

}