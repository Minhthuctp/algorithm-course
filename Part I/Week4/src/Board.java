import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)

    private int[][] tiles;

    private int[][] copy(int[][] temp)
    {
        int[][] res = new int[temp.length][temp.length];
        for (int i=0;i<temp.length;i++)
            for (int j=0;j<temp.length;j++)
            {
                res[i][j]=temp[i][j];
            }
        return res;
    }

    public Board(int[][] tiles)
    {
        this.tiles=copy(tiles);
    }

    // string representation of this board
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension()
    {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming()
    {
        int count = 0;
        for (int i=0;i< tiles.length;i++)
            for (int j=0;j<tiles.length;j++)
            {
                if (tiles[i][j]==0)
                    continue;
                if (tiles[i][j]!=i*tiles.length+j+1)
                    count++;
            }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan()
    {
        int res = 0;
        for (int i=0;i<tiles.length;i++)
            for (int j=0;j< tiles.length;j++)
            {
                if (tiles[i][j]==0)
                    continue;
                int dist_x = Math.abs(((tiles[i][j])-1)/dimension()-i);
                int dist_y = Math.abs(((tiles[i][j])-1)%dimension()-j);
                res += dist_x + dist_y;
            }
        return res;
    }

    // is this board the goal board?
    public boolean isGoal()
    {
        return (hamming()==0);
    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return Arrays.deepEquals(this.tiles, that.tiles);
    }
    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        List<Board> res = new ArrayList<>();
        int[] r = {1,0,-1,0};
        int[] c = {0,1,0,-1};

        int loca_x=-1,loca_y=-1;
        for (int i=0;i<tiles.length;i++)
            for (int j=0;j<tiles.length;j++)
                if (tiles[i][j]==0)
                {
                    loca_x=i;
                    loca_y=j;
                    break;
                }
        for (int i = 0;i<r.length;i++)
        {
            int row = loca_x + r[i];
            int col = loca_y + c[i];
            if ((row>=0) && (row<tiles.length) && (col>=0) && (col<tiles.length))
            {
                Board neighbor = new Board(this.tiles);
                int[][] Arr_copy = neighbor.tiles;
                Arr_copy[loca_x][loca_y]=tiles[row][col];
                Arr_copy[row][col]=tiles[loca_x][loca_y];
                res.add(neighbor);
            }
        }
        return res;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin()
    {
        Board temp = new Board(this.tiles);
        int[][] Arr_copy = temp.tiles;
        if ((tiles[0][0]==0) || (tiles[0][1]==0))
        {
            Arr_copy[1][0]=tiles[1][1];
            Arr_copy[1][1]=tiles[1][0];
        }
        else
        {
            Arr_copy[0][0]=tiles[0][1];
            Arr_copy[0][1]=tiles[0][0];
        }
        return temp;
    }

    // unit testing (not graded)
    public static void main(String[] args)
    {

    }
}
