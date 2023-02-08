import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture pic;
    private final int width;
    private final int height;
    private static double MAX_ENERGY=1000;
    private int[][] parent;
    private double[][] energy;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture)
    {
        if (picture == null)
            throw new IllegalArgumentException();
        energy = new double[picture.width()][picture.height()];
        parent = new int[picture.width()][picture.height()];
        this.pic = picture;
        width=pic.width();
        height=pic.height();
        for (int y = 0; y < height(); y++)
        {
            for (int x = 0; x < width(); x++)
            {
                energy[x][y] = energy(x, y);
            }
        }
    }

    // current picture
    public Picture picture()
    {
        return pic;
    }

    // width of current picture
    public int width()
    {
        return pic.width();
    }

    // height of current picture
    public int height()
    {
        return pic.height();
    }

    // energy of pixel at column x and row y

    private double delta(Color x, Color y)
    {
        return Math.pow((x.getRed()-y.getRed()),2)+Math.pow((x.getBlue()-y.getBlue()),2)+Math.pow((x.getGreen()-y.getGreen()),2);
    }

    public double energy(int x, int y)
    {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return MAX_ENERGY;
        }
        return Math.sqrt(delta(pic.get(x-1,y),pic.get(x+1,y))+delta(pic.get(x,y-1),pic.get(x,y+1)));

    }
            
    private void transfer()
    {
        Picture transPicture = new Picture(pic.height(),pic.width());
        double[][] temp_energy = new double[pic.height()][pic.width()];
        for (int i=0;i<pic.width();i++)
            for (int j=0;j<pic.height();j++)
            {
                transPicture.set(j,i,pic.get(i,j));
                temp_energy[j][i] = energy[i][j];
            }
        energy = temp_energy;
        pic = transPicture;
        parent = new int[pic.width()][pic.height()];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam()
    {
        transfer();
        int[] seam = findVerticalSeam();
        transfer();
        transfer();
        transfer();
        return seam;
    }

    private void Update(int col,int row,double[] distTo,double[] oldDistTo)
    {
        if (row==0)
        {
            distTo[col]=MAX_ENERGY;
            parent[col][row]=col;
            return;
        }
        if (col==0)
        {
            if (col==width()-1)
            {
                distTo[col]=oldDistTo[col];
                parent[col][row]=col;
                return;
            }
            distTo[col]=Math.min(oldDistTo[col],oldDistTo[col+1]);
            if (distTo[col]==oldDistTo[col])
            {
                parent[col][row]=col;
            }
            else
            {
                parent[col][row]=col+1;
            }
            distTo[col]+=energy[col][row];
            return;
        }
        if (col==width()-1)
        {
            distTo[col]=Math.min(oldDistTo[col],oldDistTo[col-1]);
            if (distTo[col]==oldDistTo[col])
            {
                parent[col][row]=col;
            }
            else
            {
                parent[col][row]=col-1;
            }
            distTo[col]+=energy[col][row];
            return;
        }
        distTo[col]=Math.min(Math.min(oldDistTo[col-1],oldDistTo[col]),oldDistTo[col+1]);
        if (distTo[col]==oldDistTo[col-1])
        {
            parent[col][row]=col-1;
        }
        else
        {
            if (distTo[col]==oldDistTo[col])
            {
                parent[col][row]=col;
            }
            else
            {
                parent[col][row]=col+1;
            }
        }
        distTo[col]+=energy[col][row];
    }
    // sequence of indices for vertical seam
    public int[] findVerticalSeam()
    {
        int[] seam = new int[pic.height()];
        double[] distTo = new double[pic.width()];
        double[] oldDistTo = new double[pic.width()];
        for (int j=0;j<pic.height();j++)
        {
            for (int i=0;i<pic.width();i++)
            {
                Update(i,j,distTo,oldDistTo);
            }
            System.arraycopy(distTo,0,oldDistTo,0,width());
        }
        int temp=0;
        for (int i=1;i<width();i++)
            if (distTo[temp]>distTo[i])
                temp=i;
        seam[height()-1]=temp;
        for (int j=height()-1;j>=1;j--)
        {
            seam[j-1]=parent[temp][j];
            temp=parent[temp][j];
        }
        return seam;
    }

    private void Check_validity(int[] seam,boolean vertical)
    {
        if (width() <= 0 || height() <= 0) {
            throw new IllegalArgumentException("Wrong width and height (>1)");
        }
        if (seam == null){
            throw new IllegalArgumentException();
        }
        if (seam.length == 0) {
            throw new IllegalArgumentException("Wrong seam length (>1)");
        }

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException("Wrong seam distance (abs()<=1)");
            }
        }
        if (vertical)
        {
            for (int i = 0; i < seam.length - 1; i++)
                if (seam[i]>width())
                    throw new IllegalArgumentException();
        }
        else
        {
            for (int i = 0; i < seam.length - 1; i++)
                if (seam[i]>height())
                    throw new IllegalArgumentException();
        }
    }

    private Picture remove_seam(int[] seam,boolean vertical)
    {
        if (vertical)
        {
            Picture temp = new Picture(width() - 1, height());
            for (int i = 0; i < height(); i++)
            {
                int k=0;
                for (int j = 0; j < width(); j++)
                    if (seam[i] != j)
                    {
                        temp.set(k,i,pic.get(j,i));
                        k++;
                    }
            }
            return temp;
        }
        Picture temp = new Picture(width() , height()-1);
        for (int j = 0; j < width(); j++)
        {
            int k=0;
            for (int i = 0; i < height(); i++)
                if (seam[j] != i)
                {
                    temp.set(j,k,pic.get(j,i));
                    k++;
                }
        }
        return temp;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam)
    {
        Check_validity(seam,false);
        if (seam.length!=width())
            throw new IllegalArgumentException("seam length must be equal to width");
        pic = remove_seam(seam,false);
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[x][y] = energy(x, y);
            }
        }

    }
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam)
    {
        Check_validity(seam,true);
        if (seam.length!=height())
            throw new IllegalArgumentException("seam length must be equal to height");
        pic = remove_seam(seam,true);
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[x][y] = energy(x, y);
            }
        }
    }

    //  unit testing (optional)
    public static void main(String[] args)
    {
        //Unit testing

    }

}