import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class PercolationStats {
    private int numOftrials;
    private double[] data;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials)
    {
        if (n<=0 || trials <=0)
            throw new IllegalArgumentException("n<= 0 || trials <=0");
        numOftrials = trials;
        data = new double[trials];
        for (int num = 0; num < trials; num++)
        {
            Percolation per = new Percolation(n);
            int opensite = 0;
            while (!per.percolates())
            {
                int row = StdRandom.uniform(1,n+1);
                int col = StdRandom.uniform(1,n+1);
                if (!per.isOpen(row,col))
                {
                    per.open(row,col);
                    opensite++;
                }
            }
            double res = (double) opensite/(n*n);
            data[num] = res;
        }
    }

    // sample mean of percolation threshold
    public double mean()
    {
        return StdStats.mean(data);
    }

    // sample standard deviation of percolation threshold
    public double stddev()
    {
        return StdStats.stddev(data);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo()
    {
        return mean() - (1.96 * stddev() / Math.sqrt(numOftrials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi()
    {
        return mean() + (1.96 * stddev() / Math.sqrt(numOftrials));
    }

    // test client (see below)
    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats pStats = new PercolationStats(n, trials);

        String confidence = "[" + pStats.confidenceLo() + ", " + pStats.confidenceHi() + "]";
        StdOut.println("mean                    = " + pStats.mean());
        StdOut.println("stddev                  = " + pStats.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }
}
