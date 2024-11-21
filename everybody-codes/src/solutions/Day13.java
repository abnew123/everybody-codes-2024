package solutions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Day13 {

    public String solve(int part, Scanner in) {
        List<String> lines = new ArrayList<>();
        while(in.hasNext()){
            lines.add(in.nextLine());
        }
        int[][] grid = new int[lines.get(0).length()][lines.size()];
        Coordinate start = null;
        Coordinate end = null;
        for(int i = 0; i < lines.get(0).length(); i++){
            for(int j = 0; j < lines.size(); j++){
                char c = lines.get(j).charAt(i);
                if(c == '#' || c == ' ' || c == 'S' || c == 'E'){
                    grid[i][j] = 1000;
                    if(c == 'S'){
                        grid[i][j] = 0;
                        start = new Coordinate(i,j);
                    }
                    if(c == 'E'){
                        grid[i][j] = 0;
                        end = new Coordinate(i,j);
                    }
                }
                else{
                    grid[i][j] = c - '0';
                }
            }
        }
        if(part != 3){
            int placeholder = 10000000;
            int[][] gridCache = new int[grid.length][grid[0].length];
            for(int i = 0; i < gridCache.length; i++){
                for(int j = 0; j < gridCache[0].length; j++){
                    if (i == start.x() && j == start.y()){
                        gridCache[i][j] = 0;
                    }
                    else{
                        gridCache[i][j] = placeholder;
                    }
                }
            }
            boolean somethingChanged = true;
            int[]xs = new int[]{0,0,1,-1};
            int[]ys = new int[]{1,-1,0,0};
            while(somethingChanged){
                iterations++;
                somethingChanged = false;
                for(int i = 0; i < gridCache.length; i++){
                    for(int j = 0; j < gridCache[0].length; j++){
                        int val = gridCache[i][j];
                        if(val != placeholder){
                            for(int k = 0; k < xs.length; k++){
                                int newx = i + xs[k];
                                int newy = j + ys[k];
                                if(safe(newx, newy, grid) && grid[newx][newy] != 1000){
                                    int newVal = val + getDifference(i,j,newx,newy, grid) + 1;
                                    if(gridCache[newx][newy] > newVal){
                                        somethingChanged = true;
                                        gridCache[newx][newy] = newVal;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return gridCache[end.x()][end.y()] + "";
        }
        else{
            int placeholder = 10000000;
            int[][] gridCache = new int[grid.length][grid[0].length];
            for(int i = 0; i < gridCache.length; i++){
                for(int j = 0; j < gridCache[0].length; j++){
                    if (i == 0 || j == 0 || i == gridCache.length - 1 || j == gridCache[0].length - 1){
                        gridCache[i][j] = 0;
                    }
                    else{
                        gridCache[i][j] = placeholder;
                    }
                }
            }
            boolean somethingChanged = true;
            int[]xs = new int[]{0,0,1,-1};
            int[]ys = new int[]{1,-1,0,0};
            int iterations = 0;
            while(somethingChanged){
                iterations++;
                somethingChanged = false;
                for(int i = 0; i < gridCache.length; i++){
                    for(int j = 0; j < gridCache[0].length; j++){
                        int val = gridCache[i][j];
                        if(val != placeholder){
                            for(int k = 0; k < xs.length; k++){
                                int newx = i + xs[k];
                                int newy = j + ys[k];
                                if(safe(newx, newy, grid) && grid[newx][newy] != 1000){
                                    int newVal = val + getDifference(i,j,newx,newy, grid) + 1;
                                    if(gridCache[newx][newy] > newVal){
                                        somethingChanged = true;
                                        gridCache[newx][newy] = newVal;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println(iterations);

            return gridCache[end.x()][end.y()] + "";
        }

    }

    private int findBest(Coordinate start, Coordinate end, int currTime, int[][] grid, boolean[][] eliminated, HashMap<Coordinate, Integer> bestSoFar){
        if(bestSoFar.getOrDefault(start, 11 * grid.length * grid[0].length) < currTime){
            return 11 * grid.length * grid[0].length;
        }
        if(start.x() == end.x() && start.y() == end.y()){
            if(bestSoFar.get(start) > currTime){
                System.out.println(currTime);
            }
            return currTime;
        }

        int level = grid[start.x()][start.y()];
        int result = 11 * grid.length * grid[0].length;

        int[]xs = new int[]{0,0,1,-1};
        int[]ys = new int[]{1,-1,0,0};
        List<Coordinate> neighbors = new ArrayList<>();
        for(int i = 0; i < xs.length; i++){
            int newx = start.x() + xs[i];
            int newy = start.y() + ys[i];
            if(safe(newx, newy, grid) && !eliminated[newx][newy] && grid[newx][newy] != 1000){
                neighbors.add(new Coordinate(newx, newy));
            }
        }
        boolean[][] newEliminated = new boolean[eliminated.length][eliminated[0].length];
        for(int i = 0; i < newEliminated.length; i++){
            for(int j = 0; j < newEliminated[0].length; j++){
                newEliminated[i][j] = eliminated[i][j];
            }
        }
        mark(start, newEliminated);
        for(Coordinate neighbor: neighbors){
            int difference = getLevel(neighbor, grid) - level;
            difference = Math.min(Math.abs(difference), 10- Math.abs(difference));

            for(Coordinate c: neighbors){
                mark(c, newEliminated);
            }
            result = Math.min(result, findBest(neighbor, end, currTime + difference + 1, grid, newEliminated, bestSoFar));
        }
        bestSoFar.put(start, currTime);
        return result;
    }

    private boolean safe(int x, int y, int[][] grid){
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
    }

    private int getLevel(Coordinate c, int[][] grid){
        return grid[c.x()][c.y()];
    }

    private void mark(Coordinate c, boolean[][] eliminated){
        eliminated[c.x()][c.y()] = true;
    }

    private int getDifference(int x1, int y1, int x2, int y2, int[][] grid){
        int difference = grid[x2][y2] - grid[x1][y1];
        difference = Math.min(Math.abs(difference), 10- Math.abs(difference));
        return difference;
    }
}
