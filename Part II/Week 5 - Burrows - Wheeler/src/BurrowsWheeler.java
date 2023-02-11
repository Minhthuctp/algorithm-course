import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    private final static int R = 256;
    public static void transform()
    {
        String s = BinaryStdIn.readString();
        int first = 0;
        CircularSuffixArray st = new CircularSuffixArray(s);
        for (int i = 0;i < st.length();i++)
            if (st.index(i) == 0)
            {
                first = i;
                break;
            }
        BinaryStdOut.write(first);
        for (int i = 0; i < st.length(); i++) {
            int last = (st.index(i) - 1 + st.length()) % st.length();
            BinaryStdOut.write(s.charAt(last));
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform()
    {
        int first = BinaryStdIn.readInt();
        String lastCol = BinaryStdIn.readString();
        char[] firstCol = new char[lastCol.length()];
        int[] next = new int[lastCol.length()];
        int[] count = new int[R+1];
        Arrays.sort(firstCol);

        for (int i = 0; i < lastCol.length(); i++)
        {
            count[lastCol.charAt(i)+1]++;
        }
        for (int i = 0; i < R; i++)
        {
            count[i+1]+=count[i];
        }
        for (int i = 0; i < lastCol.length(); i++)
        {
            int pos = count[lastCol.charAt(i)]++;
            firstCol[pos]=lastCol.charAt(i);
            next[pos]=i;
        }
        for (int i = 0; i < lastCol.length(); i++)
        {
            BinaryStdOut.write(firstCol[first]);
            first = next[first];
        }
        BinaryStdOut.close();

    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args)
    {
        if (args[0].equals("-")) BurrowsWheeler.transform();
        if (args[0].equals("+")) BurrowsWheeler.inverseTransform();
    }

}
