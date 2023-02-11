import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    // circular suffix array of s
    private int length;
    private char[] val;
    private Integer[] index;
    public CircularSuffixArray(String s)
    {
        if (s == null)
            throw new IllegalArgumentException();
        length=s.length();
        val = new char[length];
        index = new Integer[length];
        for (int i=0; i<length;i++)
        {
            val[i]=s.charAt(i);
            index[i]=i;
        }
        Arrays.sort(index,new Comparator<Integer>() {
            public int compare(Integer idx1, Integer idx2)
            {
                for (int i=0;i<length;i++)
                {
                    int val1 = val[(idx1+i)%length];
                    int val2 = val[(idx2+i)%length];
                    if (val1>val2)
                        return 1;
                    if (val1<val2)
                        return -1;
                }
                return 0;
            };
        });
    }

    // length of s
    public int length()
    {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i)
    {
        if (i>=length || i<0)
            throw new IllegalArgumentException();
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args)
    {

    }
}
