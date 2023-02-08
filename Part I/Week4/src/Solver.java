import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
public class Solver {
    // find a solution to the initial board (using the A* algorithm)
    private Stack<Board> solution;
    private static class Node implements Comparable<Node>
    {
        private Board board;
        private int num_move;
        private Node pre;
        private int priority;
        public Node(Board board, int num_move, Node pre)
        {
            this.board = board;
            this.num_move = num_move;
            this.pre = pre;
            this.priority = num_move + board.manhattan();
        }
        public int compareTo(Node other)
        {
            return Integer.compare(this.priority,other.priority);
        }
    }
    public Solver(Board initial)
    {
        if (initial == null)
            throw new IllegalArgumentException();
        MinPQ<Node> pq = new MinPQ<>();
        MinPQ<Node> pq_twin = new MinPQ<>();

        pq.insert(new Node(initial,0,null));

        pq_twin.insert(new Node(initial.twin(),0,null));

        Node cur;
        Node cur_twin;

        do {
            cur = pq.delMin();
            cur_twin = pq_twin.delMin();
            for (Board neighbor: cur.board.neighbors())
            {
                if (cur.pre!=null && neighbor.equals(cur.pre.board))
                    continue;
                else
                    pq.insert(new Node(neighbor,cur.num_move+1,cur));
            }
            for (Board neighbor: cur_twin.board.neighbors())
            {
                if (cur_twin.pre!=null && neighbor.equals(cur_twin.pre.board))
                    continue;
                else
                    pq_twin.insert(new Node(neighbor,cur_twin.num_move+1,cur_twin));
            }
        }
        while ((!cur.board.isGoal()) && (!cur_twin.board.isGoal()));
        solution = new Stack<>();
        if (cur.board.isGoal())
        {
            while (cur!=null)
            {
                solution.push(cur.board);
                cur=cur.pre;
            }
        }
    }
    // is the initial board solvable? (see below)
    public boolean isSolvable()
    {
        return (!solution.isEmpty());
    }
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()
    {
        return solution.size()-1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()
    {
        return  (isSolvable()) ? solution : null;
    }

    // test client (see below)
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
