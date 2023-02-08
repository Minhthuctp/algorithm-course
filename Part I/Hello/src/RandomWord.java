
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {

    public static void main(String[] args) {
        String res = "";
        int n = 1;

        while (!StdIn.isEmpty()) {
            String cur = StdIn.readString();
            if(StdRandom.bernoulli((double) 1 / n)) res = cur;
            n++;
        }

        StdOut.println(res);
    }

}