package solutions;

import java.util.*;

public class Day20 {
    public String solve(int part, Scanner in) {
        int answer = 0;
        List<String> lines = new ArrayList<>();
        while(in.hasNext()){
            lines.add(in.nextLine());
        }
        int[] xs = new int[]{1,-1,0,0};
        int[] ys = new int[]{0,0,1,-1};
        int[][] grid = new int[lines.size()][lines.get(0).length()];
        if(part == 1){
            int altitude = 1000;
            int time = 100;
            int[][][][] dp = new int[lines.size()][lines.get(0).length()][time + 1][4];
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    char c = lines.get(i).charAt(j);
                    for(int k = 0; k < dp[0][0].length; k++){
                        dp[i][j][k][0] = -1;
                        dp[i][j][k][1] = -1;
                        dp[i][j][k][2] = -1;
                        dp[i][j][k][3] = -1;
                    }
                    if(c == '.'){
                        grid[i][j] = -1;
                    }
                    if(c == '#'){
                        grid[i][j] = -10000;
                    }
                    if(c == '+'){
                        grid[i][j] = 1;
                    }
                    if(c == '-'){
                        grid[i][j] = -2;
                    }
                    if(c == 'S'){
                        grid[i][j] = -1;
                        dp[i][j][0][0] = altitude;
                        dp[i][j][0][1] = altitude;
                        dp[i][j][0][2] = altitude;
                        dp[i][j][0][3] = altitude;
                    }
                }
            }
            for(int t = 0; t < time; t++){
                for(int i = 0; i < grid.length; i++){
                    for(int j = 0; j < grid[0].length; j++){
                        for(int k = 0; k < 4; k++){
                            if(dp[i][j][t][k] != -1){
                                for(int l = 0; l < xs.length; l++){
                                    if(l + k != 1 && l + k != 5){
                                        if(safe(i + xs[l], j + ys[l], dp)){
                                            dp[i + xs[l]][j + ys[l]][t+1][l] = Math.max(dp[i + xs[l]][j + ys[l]][t+1][l], dp[i][j][t][k] + grid[i + xs[l]][j + ys[l]]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    for(int k = 0; k < 4; k++){
                        answer = Math.max(answer, dp[i][j][time][k]);
                    }
                }
            }
        }

        if(part == 2){
            int altitude = 10000;
            int time = 1000;
            //indices are x,y,time,direction, checkpoint status
            int[][][][][] dp = new int[lines.size()][lines.get(0).length()][time + 1][4][4];
            Coordinate aCheckpoint = new Coordinate(0,0);
            Coordinate bCheckpoint = new Coordinate(0,0);
            Coordinate cCheckpoint = new Coordinate(0,0);
            Coordinate start = new Coordinate(0,0);
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    char c = lines.get(i).charAt(j);
                    for(int k = 0; k < dp[0][0].length; k++){
                        for(int l = 0; l < dp[0][0][0].length; l++){
                            dp[i][j][k][l][0] = -1;
                            dp[i][j][k][l][1] = -1;
                            dp[i][j][k][l][2] = -1;
                            dp[i][j][k][l][3] = -1;
                        }
                    }
                    if(c == '.'){
                        grid[i][j] = -1;
                    }
                    if(c == '#'){
                        grid[i][j] = -10000;
                    }
                    if(c == '+'){
                        grid[i][j] = 1;
                    }
                    if(c == '-'){
                        grid[i][j] = -2;
                    }
                    if(c == 'S'){
                        grid[i][j] = -1;
                        start = new Coordinate(i,j);
                        dp[i][j][0][0][0] = altitude;
                        dp[i][j][0][1][0] = altitude;
                        dp[i][j][0][2][0] = altitude;
                        dp[i][j][0][3][0] = altitude;
                    }
                    if(c == 'A'){
                        grid[i][j] = -1;
                        aCheckpoint = new Coordinate(i,j);
                    }
                    if(c == 'B'){
                        grid[i][j] = -1;
                        bCheckpoint = new Coordinate(i,j);
                    }
                    if(c == 'C'){
                        grid[i][j] = -1;
                        cCheckpoint = new Coordinate(i,j);
                    }
                }
            }
            for(int t = 0; t < time; t++){
                for(int i = 0; i < grid.length; i++){
                    for(int j = 0; j < grid[0].length; j++){
                        for(int k = 0; k < 4; k++){
                            for(int checkpoint = 0; checkpoint < 4; checkpoint++){
                                int newCheckpoint = checkpoint;
                                if(newCheckpoint == 0 && i == aCheckpoint.x() && j == aCheckpoint.y()){
                                    newCheckpoint = 1;
                                }
                                if(newCheckpoint == 1 && i == bCheckpoint.x() && j == bCheckpoint.y()){
                                    newCheckpoint = 2;
                                }
                                if(newCheckpoint == 2 && i == cCheckpoint.x() && j == cCheckpoint.y()){
                                    newCheckpoint = 3;
                                }
                                if(dp[i][j][t][k][checkpoint] != -1){
                                    for(int l = 0; l < xs.length; l++){
                                        if(l + k != 1 && l + k != 5){
                                            if(safe(i + xs[l], j + ys[l], dp)){
                                                dp[i + xs[l]][j + ys[l]][t+1][l][newCheckpoint] = Math.max(dp[i + xs[l]][j + ys[l]][t+1][l][newCheckpoint], dp[i][j][t][k][checkpoint] + grid[i + xs[l]][j + ys[l]]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(t != 0){
                    for(int k = 0; k < 4; k++){
                        if(dp[start.x()][start.y()][t + 1][k][3] >= 10000){
                            return t+1 + "";
                        }
                    }
                }
                int[][] condensedGrid = new int[dp.length][dp[0].length];
                for(int i = 0; i < condensedGrid.length; i++){
                    for(int j = 0; j < condensedGrid[0].length; j++){
                        condensedGrid[i][j] = -1;
                        for(int k = 0; k < 4; k++){
                            condensedGrid[i][j] = Math.max(condensedGrid[i][j], dp[i][j][t][k][0]);
                        }
                    }
                }
            }
        }

        if(part == 3){
            int altitude = 384400;
            Coordinate start = new Coordinate(0,0);
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    char c = lines.get(i).charAt(j);
                    if(c == '.'){
                        grid[i][j] = -1;
                    }
                    if(c == '#'){
                        grid[i][j] = -1000000;
                    }
                    if(c == '+'){
                        grid[i][j] = 1;
                    }
                    if(c == '-'){
                        grid[i][j] = -2;
                    }
                    if(c == 'S'){
                        grid[i][j] = -1;
                        start = new Coordinate(i,j);
                    }
                }
            }
            int counter = getCounter(start, altitude, grid);
            answer = counter;
        }
        return answer + "";
    }

    private int getCounter(Coordinate start, int altitude, int[][] grid) {
        Set<FlowState> states = new HashSet<>();
        states.add(new FlowState(start.x(), start.y(), altitude));
        Set<FlowState> visitedStates = new HashSet<>();
        boolean done = false;
        int[] ydeltas = new int[]{1,-1};
        while(!done){
            done = true;
            List<FlowState> newStates = new ArrayList<>();
            for(FlowState state: states){
                for(int delta: ydeltas){
                    if(safe(state.x(), state.y() + delta, grid)){
                        FlowState next = new FlowState(state.x(), state.y() + delta, state.altitude() + grid[state.x()][state.y() + delta]);
                        if(!visitedStates.contains(next) && next.altitude() > 0){
                            newStates.add(next);
                            done = false;
                        }
                    }
                }
            }
            visitedStates.addAll(newStates);
            states.addAll(newStates);
        }
        int counter = 0;
        int currBest = 1;
        while(currBest > 0){
            currBest = 0;
            counter++;
            Set<FlowState> newStates = new HashSet<>();
            for(FlowState state: states){
                FlowState next = new FlowState((state.x() + 1), state.y(), state.altitude() + grid[(state.x() + 1)% grid.length][state.y()]);
                if(!visitedStates.contains(next) && next.altitude() > 0){
                    newStates.add(next);
                    currBest = Math.max(currBest, next.altitude());
                }
            }
            states = newStates;
        }
        return counter;
    }

    private boolean safe(int x, int y, int[][] grid){
        return x >=0 && y >=0 && x < grid.length && y < grid[0].length;
    }
    private boolean safe(int x, int y, int[][][] grid){
        return x >=0 && y >=0 && x < grid.length && y < grid[0].length;
    }
    private boolean safe(int x, int y, int[][][][] grid){
        return x >=0 && y >=0 && x < grid.length && y < grid[0].length;
    }
    private boolean safe(int x, int y, int[][][][][] grid){
        return x >=0 && y >=0 && x < grid.length && y < grid[0].length;
    }
}

record FlowState(int x, int y, int altitude){
    @Override
    public String toString(){
        return x + " " + y + " " + altitude;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof FlowState o){
            return (x == o.x && y == o.y);
        }
        return false;
    }

    @Override
    public int hashCode(){
        return 10000 * x + y;
    }
}
