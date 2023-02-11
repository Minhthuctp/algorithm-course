import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] keys = new char[R];
        for (int i = 0; i < R; i++)
            keys[i] = (char) i;
        while (!BinaryStdIn.isEmpty())
        {
            int pos = 0;
            char t = BinaryStdIn.readChar();
            for (int j = 0;j < R; j++)
                if (t == keys[j])
                {
                    pos = j;
                    break;
                }
            BinaryStdOut.write((char) pos);
            for(int j=pos;j>=1;j--)
                keys[j]=keys[j-1];
            keys[0]=t;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
        char[] keys = new char[R];
        for (int i = 0; i < R;i++)
            keys[i] = (char) i;

        while (!BinaryStdIn.isEmpty())
        {
            int t = BinaryStdIn.readInt(8);
            BinaryStdOut.write(keys[t]);
            char temp = keys[t];
            for(int i=t;i>=1;i--)
                keys[i]=keys[i-1];
            keys[0]=temp;
        }
        BinaryStdOut.close();
    }
    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args)
    {
        if (args[0].equals("-")) MoveToFront.encode();
        if (args[0].equals("+")) MoveToFront.decode();
    }
}