import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {
    private boolean[][] grid;
    private int length;
    private int top = 0;
    private int bottom;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF uf_check;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n)
    {
        if (n<=0)
            throw new IllegalArgumentException();
        grid = new boolean[n][n];
        bottom = n*n + 1;
        length = n;
        uf = new WeightedQuickUnionUF(n * n + 2);
        uf_check = new WeightedQuickUnionUF(n * n + 2);
        for (int i=0;i<length;i++)
            for (int j=0;j<length;j++)
                grid[i][j]=false;
    }

    private int getIndex(int row, int col)
    {
        return (row-1)*length + col;
    }
    // opens the site (row, col) if it is not open already
    public void open(int row, int col)
    {
        if (row < 1 || row > length || col < 1 || col > length) throw new IllegalArgumentException("Index is out of bounds");
        grid[row - 1][col - 1] = true;
        if (row == 1) {
            uf.union(getIndex(row,col),top);
            uf_check.union(getIndex(row,col),top);
        }
        if (row == length)
            uf.union(getIndex(row,col),bottom);
        if (row > 1 && isOpen(row - 1, col)) {
            uf.union(getIndex(row, col), getIndex(row - 1, col));
            uf_check.union(getIndex(row, col), getIndex(row - 1, col));
        }
        if (row < length && isOpen(row + 1, col)) {
            uf.union(getIndex(row, col), getIndex(row + 1, col));
            uf_check.union(getIndex(row, col), getIndex(row + 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) {
            uf.union(getIndex(row, col), getIndex(row, col - 1));
            uf_check.union(getIndex(row, col), getIndex(row, col - 1));
        }
        if (col < length && isOpen(row, col + 1)) {
            uf.union(getIndex(row, col), getIndex(row, col + 1));
            uf_check.union(getIndex(row, col), getIndex(row, col + 1));
        }
    }
    // is the site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        if (row < 1 || row > length || col < 1 || col > length) throw new IllegalArgumentException("Index is out of bounds");
        return grid[row-1][col-1];
    }
    // is the site (row, col) full?
    public boolean isFull(int row, int col)
    {
        if (row < 1 || row > length || col < 1 || col > length)
            throw new IllegalArgumentException("Index is out of bounds");
        return (grid[row-1][col-1]==true) && (uf_check.find(getIndex(row,col)) == uf_check.find(top));
    }
    // returns the number of open sites
    public int numberOfOpenSites()
    {
        int num = 0;
        for (int i=0;i < length;i++)
            for (int j=0;j < length;j++)
                if (grid[i][j] == true)
                {
                    num++;
                }
        return num;
    }

    // does the system percolate?
    public boolean percolates()
    {
        return uf.find(bottom) == uf.find(top);
    }

    // test client (optional)
    public static void main(String[] args)
    {

    }
}
