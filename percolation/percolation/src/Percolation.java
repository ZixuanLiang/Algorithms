
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // creates n-by-n grid, with all sites initially block, with value 0
    public int[][][] grid;
    public int n;
    public int[] top;
    public int[] bot;
    public int num_open;

    public Percolation(int n){
        grid = new int[n][n][3];
        for (int i = 1; i < n; i++){
            for (int j = 1; j < n; j++){
                grid[i][j][0] = i;
                grid[i][j][1] = j;
            }
        }
        top = new int[]{n + 1, n + 1, 1};
        bot = new int[]{0, 0, 1};
        this.n = n;
    }

    // opens the site (row, col) if it is not open already, site with value 0 is blocked, 1 means open
    public void open(int row, int col){
        int[] SITE = site(row, col);
        if (SITE[3] == 0) {
            SITE[3] = 1;
            //change root
            change_root(row, col, SITE);
            num_open++;
        }
    }
    public void change_root(int row, int col, int[] l){
        if (find_nearest(row, col) != null) {
            int[] from = find_nearest(row, col);
            l[0] = from[0];
            l[1] = from[1];
        }
    }

    public int[] find_nearest(int row, int col){
        if (row == 1) {
            return top;
        } else if (row == n) {
            return bot;
        }
        if (isOpen(row - 1, col)) {
            return site(row - 1, col);
        } else if (isOpen(row, col - 1)) {
            return site(row, col - 1);
        } else if (isOpen(row, col + 1)) {
            return site(row, col + 1);
        } else if (isOpen(row + 1, col) {
            return site(row + 1, col);
        }
        return null;
    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        if (row > n || col > n || row < 1 || col < 1) {
            throw new IllegalArgumentException("");
        }
        return grid[row][col][3] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        return grid[row][col][3] == 0;
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return num_open;
    }

    // does the system percolate?
    public boolean percolates(){
        return connected(top, bot);
    }
    //if two site are connected
    public boolean connected(int[] a, int[] b){
        if (a[0] != b[0]) {
            return false;
        } else if (a[1] != b[1]) {
            return false;
        }
        return true;
    }
    // return the site
    public int[] site(int col, int row){
        return grid[row - 1][col - 1];
    }
    // test client (optional)
    public static void main(String[] args){

    }
}
