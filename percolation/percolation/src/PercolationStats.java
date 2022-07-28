import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private static final double CI_95 = 1.96;
    private final double[] threshold;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        threshold = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int index = StdRandom.uniform(0, n*n);
                p.open(getRowFromIndex(index, n), getColFromIndex(index, n));
            }
            threshold[i] = (double) p.numberOfOpenSites() / (n*n);
        }
    }
    private int getRowFromIndex(int i, int n) {
        return i / n + 1;
    }
    private int getColFromIndex(int i, int n) {
        return i % n + 1;
    }
    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(threshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(threshold);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double mean = mean();
        double d = CI_95 * stddev() / Math.sqrt(threshold.length);
        return mean - d;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double mean = mean();
        double d = CI_95 * stddev() / Math.sqrt(threshold.length);
        return mean + d;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, trials);
        double[] CI = new double[]{stats.confidenceLo(), stats.confidenceHi()};
        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = " + "[" + CI[0] + "," + CI[1] + "]");
    }
}
