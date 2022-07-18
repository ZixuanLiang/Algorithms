import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // creates n-by-n grid, with all sites initially block, with value 0
    private int[][] grid;
    private int n;
    private int num_open;
    private WeightedQuickUnionUF uf;
    private int index_of_top;
    private int index_of_bot;

    public Percolation(int n){
        grid = new int[n][n];
        this.n = n;
        uf = new WeightedQuickUnionUF(n*n + 2);
        index_of_top = n*n;
        index_of_bot = index_of_top + 1;
    }
    private int index(int row, int col) {
        return (row - 1) * n + col - 1;
    }


    // opens the site (row, col) if it is not open already, site with value 0 is blocked, 1 means open
    public void open(int row, int col){
        throwIllegalArgumentException(row, col);
        if (isFull(row, col)) {
            grid[row - 1][col - 1] = 1;
            connect(row, col);
            num_open++;
        }
    }
    //connect the site with its possible neighbors
    private void connect(int row, int col){
        int i = index(row, col);
        if (row == 1) {
            uf.union(i, index_of_top);
        } else {
            connect_to(i, row - 1, col);
        }
        if (row == n) {
            uf.union(i, index_of_bot);
        } else {
            connect_to(i, row + 1, col);
        }
        if (col < n) {
            connect_to(i, row, col + 1);
        }
        if (col > 1) {
            connect_to(i, row, col - 1);
        }
    }
    //connect a site with a specific site
    private void connect_to(int i, int row, int col) {
        if (isOpen(row, col)) {
            uf.union(i, index(row, col));
        }
    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        throwIllegalArgumentException(row, col);
        return grid[row - 1][col - 1] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        throwIllegalArgumentException(row, col);
        return grid[row - 1][col - 1] == 0;
    }
    private void throwIllegalArgumentException(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
    }
    // returns the number of open sites
    public int numberOfOpenSites(){
        return num_open;
    }

    // does the system percolate?
    public boolean percolates(){
        return uf.find(index_of_top) == uf.find(index_of_bot);
    }

}
