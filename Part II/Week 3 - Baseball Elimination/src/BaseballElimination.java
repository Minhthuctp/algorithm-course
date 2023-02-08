import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;

public class BaseballElimination
{
    private final int numteam;
    private String[] names;
    private int[] win;
    private int[] lose;
    private int[] remain;
    private int[][] remain_other;

    private int capacityT;

    private ArrayList<String> list;
    public BaseballElimination(String filename)                    // create a baseball division from given filename in format specified below
    {
          In in = new In(filename);
          numteam = Integer.parseInt(in.readLine());
          names = new String[numteam];
          win = new int[numteam];
          lose = new int[numteam];
          remain = new int[numteam];
          remain_other = new int[numteam][numteam];
          for (int i=0;i<numteam;i++)
          {
              String data = in.readLine();
              Scanner line = new Scanner(data);
              names[i] = line.next();
              win[i] = line.nextInt();
              lose[i] = line.nextInt();
              remain[i] = line.nextInt();
              for (int j=0;j<numteam;j++)
                  remain_other[i][j] = line.nextInt();
          }

    }
    public int numberOfTeams()                        // number of teams
    {
        return numteam;
    }
    public Iterable<String> teams()                                // all teams
    {
        Stack<String> s = new Stack<String>();
        for (int i=0;i<numteam;i++)
            s.push(names[i]);
        return s;
    }
    public int wins(String team)                      // number of wins for given team
    {
        for (int i = 0; i < numteam; i++)
            if (team.equals(names[i]))
            {
                return win[i];
            }
        throw new IllegalArgumentException();
    }
    public int losses(String team)                    // number of losses for given team
    {
        for (int i = 0; i < numteam; i++)
            if (team.equals(names[i]))
            {
                return lose[i];
            }
        throw new IllegalArgumentException();
    }
    public int remaining(String team)                 // number of remaining games for given team
    {
        for (int i = 0; i < numteam; i++)
            if (team.equals(names[i]))
            {
                return remain[i];
            }
        throw new IllegalArgumentException();
    }
    public int against(String team1, String team2)    // number of remaining games between team1 and team2
    {
        int index_team1 = -1;
        int index_team2 = -1;
        for (int i=0;i<numteam;i++)
        {
            if (team1.equals(names[i]))
            {
                index_team1 = i;
                break;
            }
        }
        for (int i=0;i<numteam;i++)
        {
            if (team2.equals(names[i]))
            {
                index_team2 = i;
                break;
            }
        }
        if ((index_team1 == -1) || (index_team2 == -1))
            throw new IllegalArgumentException();
        return remain_other[index_team1][index_team2];
    }

    private boolean Check_first(int team)
    {
        boolean check = false;
        for (int i=0;i<numteam;i++)
            if ((i!=team) && (win[team] + remain[team] < win[i]))
            {
                list.add(names[i]);
                check = true;
                break;
            }
        return check;
    }

    private FordFulkerson Check_second(int team)
    {
        int numgame = ((numteam-1)*numteam)/2;
        FlowNetwork G = new FlowNetwork(numgame + numteam +2);
        int s = numgame + numteam;
        int t = numgame + numteam + 1;
        int count = 0;
        capacityT = 0;
        for (int i = 0; i < numteam; i++)
            for (int j = i+1; j < numteam; j++)
                if ((i!=team) && (j!=team))
                {
                    G.addEdge(new FlowEdge(s,count,remain_other[i][j]));
                    G.addEdge(new FlowEdge(count,numgame+i,Double.POSITIVE_INFINITY));
                    G.addEdge(new FlowEdge(count,numgame+j,Double.POSITIVE_INFINITY));
                    count++;
                    capacityT+= remain_other[i][j];
                }
        for (int i = 0; i < numteam; i++)
            if (team!=i)
                G.addEdge(new FlowEdge(numgame+i,t,wins(names[team])+remaining(names[team])-wins(names[i])));
        int index = 0;
        FordFulkerson graph = new FordFulkerson(G,s,t);
        for (int i=numgame;i<numgame+numteam;i++)
        {
            if (graph.inCut(i))
            {
                list.add(names[index]);
            }
            index++;
        }
        return graph;
    }

    public boolean isEliminated(String team) // is given team eliminated?
    {
        list = new ArrayList<>();
        int index = -1;
        for (int i = 0; i < numteam; i++) {
            if (team.equals(names[i])){
                index = i;
            }
        }
        if (index!=-1)
        {
            if (Check_first(index))
                return true;
            else
            {
                FordFulkerson graph = Check_second(index);
                if (graph.value()!=capacityT)
                    return true;
                else
                    return false;
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
    {
        list = new ArrayList<>();
        int index = -1;
        for (int i = 0; i < numteam; i++) {
            if (team.equals(names[i])){
                index = i;
            }
        }
        if (index!=-1)
        {
            if (Check_first(index))
                return list;
            FordFulkerson graph = Check_second(index);
            if (list.size()==0)
                return null;
            else
                return list;
        }
        else
        {
            throw new IllegalArgumentException();
        }
    }
}