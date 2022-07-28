import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // creates n-by-n grid, with all sites initially block, with value 0
    private final int[][] grid;
    private final int n;
    private int numOpen;
    private final WeightedQuickUnionUF uf;
    private final int indexOfTop;
    private final int indexOfBot;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new int[n][n];
        this.n = n;
        uf = new WeightedQuickUnionUF(n * n + 2);
        indexOfTop = n * n;
        indexOfBot = indexOfTop + 1;
    }
    private int index(int row, int col) {
        return (row - 1) * n + col - 1;
    }


    // opens the site (row, col) if it is not open already, site with value 0 is blocked, 1 means open
    public void open(int row, int col) {
        throwIllegalArgumentException(row, col);
        if (grid[row - 1][col - 1] == 0) {
            grid[row - 1][col - 1] = 1;
            connect(row, col);
            numOpen++;
        }
    }
    // connect the site with its possible neighbors
    private void connect(int row, int col){
        int i = index(row, col);
        if (row == 1) {
            uf.union(i, indexOfTop);
        } else {
            connectTo(i, row - 1, col);
        }
        if (row == n) {
            uf.union(i, indexOfBot);
        } else {
            connectTo(i, row + 1, col);
        }
        if (col < n) {
            connectTo(i, row, col + 1);
        }
        if (col > 1) {
            connectTo(i, row, col - 1);
        }
    }
    // connect a site with a specific site
    private void connectTo(int i, int row, int col) {
        if (isOpen(row, col)) {
            uf.union(i, index(row, col));
        }
    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        throwIllegalArgumentException(row, col);
        return grid[row - 1][col - 1] == 1;
    }

    // is the site (row, col) full(connected to top)?
    public boolean isFull(int row, int col){
        throwIllegalArgumentException(row, col);
        if (isOpen(row, col)) {
            return uf.find(index(row, col)) == uf.find(indexOfTop);
        }
        return false;
    }
    private void throwIllegalArgumentException(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
    }
    // returns the number of open sites
    public int numberOfOpenSites(){
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates(){
        return uf.find(indexOfTop) == uf.find(indexOfBot);
    }

}
