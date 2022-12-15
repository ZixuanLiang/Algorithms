import edu.princeton.cs.algs4.*;

import java.util.*;
import java.util.regex.Pattern;

public class BaseballElimination {
    private final int teamNum;
    private final String[] teams;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;
    private final Map<String, Integer> teamIndex;
    private final List<String> certificateList;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        teamNum = Integer.parseInt(in.readLine());
        teams = new String[teamNum];
        w = new int[teamNum];
        l = new int[teamNum];
        r = new int[teamNum];
        g = new int[teamNum][teamNum];
        teamIndex = new HashMap<>();
        certificateList = new ArrayList<>();
        Pattern p = Pattern.compile(" *");
        for (int i = 0; i < teamNum; i++) {
            String line = in.readLine();
            String[] split = line.split(" +");
            teams[i] = split[0];
            teamIndex.put(split[0], i);
            w[i] = Integer.parseInt(split[1]);
            l[i] = Integer.parseInt(split[2]);
            r[i] = Integer.parseInt(split[3]);
            for (int j = 0; j < teamNum; j++) {
                if (i == j) {
                    continue;
                }
                g[i][j] = Integer.parseInt(split[j]);
            }
        }
    }
    public int numberOfTeams() {
        return teamNum;
    }
    public Iterable<String> teams() {
        return Arrays.stream(teams).toList();
    }
    public int wins(String team) {
        return w[teamIndex.get(team)];
    }
    public int losses(String team) {
        return l[teamIndex.get(team)];
    }
    public int remaining(String team) {
        return r[teamIndex.get(team)];
    }
    public int against(String team1, String team2) {
        return g[teamIndex.get(team1)][teamIndex.get(team2)];
    }
    public boolean isEliminated(String team) {
        certificateList.clear();
        // trivial case
        if (isTrivialEliminated(team) == true) {
            return true;
        } else if (isNonTrivialEliminated(team) == true) {
            return true;
        } else {
            return false;
        }
    }
    private boolean isTrivialEliminated(String team) {
        int index = teamIndex.get(team);
        int maxWin = w[index] + r[index];
        for (int i = 0; i < teamNum; i++) {
            if (w[i] > maxWin) {
                certificateList.add(teams[i]);
                return true;
            }
        }
        return false;
    }
    private boolean isNonTrivialEliminated(String team){
        boolean ifEliminated = false;
        int v = teamNum * (teamNum - 2) / 2 + teamNum; // how many vertices in the network
        // Build network
        FlowNetwork fn = new FlowNetwork(v);
        int indexS = teamIndex.get(team); // index of `team`, also the index of `s` in the FlowNetwork
        int gameVerticeIndex = teamNum;
        int indexT = v - 1; // index of `t` in the FlowNetwork
        for (int i = 0; i < teamNum - 1; i++) {
            if (i == indexS) {
                continue;
            }
            fn.addEdge(new FlowEdge(i, indexT, w[indexS] + r[indexS] - w[i]));
            for (int j = i + 1; j < teamNum; j++) {
                fn.addEdge(new FlowEdge(indexS, gameVerticeIndex, g[i][j]));
                fn.addEdge(new FlowEdge(gameVerticeIndex, i, Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(gameVerticeIndex,j, Double.POSITIVE_INFINITY));
            }
        }
        // After building the network, run FordFulkerson algorithm to check if `team` has been mathematically eliminated
        FordFulkerson ff = new FordFulkerson(fn, indexS, indexT);
        for (FlowEdge e : fn.adj(indexS)) {
            if (e.flow() != e.capacity()) {
                // some is not full, eliminated
                ifEliminated = true;
                certificateList.add(teams[e.from()]);
                certificateList.add(teams[e.to()]);
            }
        }
        return ifEliminated; // all full, not eliminated
    }
    public Iterable<String> certificateOfElimination(String team) {
        return certificateList;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
//        for (String team : division.teams()) {
//            if (division.isEliminated(team)) {
//                StdOut.print(team + " is eliminated by the subset R = { ");
//                for (String t : division.certificateOfElimination(team)) {
//                    StdOut.print(t + " ");
//                }
//                StdOut.println("}");
//            }
//            else {
//                StdOut.println(team + " is not eliminated");
//            }
//        }
    }
}
