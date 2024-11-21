package solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day13 {

    int placeholder = 10000000;
    int[] xs = new int[]{0,0,1,-1};
    int[] ys = new int[]{1,-1,0,0};

    public String solve(int part, Scanner in) {
        List<String> lines = new ArrayList<>();
        while(in.hasNext()){
            lines.add(in.nextLine());
        }
        int[][] grid = new int[lines.getFirst().length()][lines.size()];
        int[][] gridCache = new int[grid.length][grid[0].length];
        Coordinate end = null;
        for(int i = 0; i < lines.getFirst().length(); i++){
            for(int j = 0; j < lines.size(); j++){
                Map<Character, Integer> map = Map.of('#',1000,' ',1000,'S',0, 'E',0);
                gridCache[i][j] = placeholder;
                char c = lines.get(j).charAt(i);
                grid[i][j] = map.getOrDefault(c, c - '0');
                if(c == 'S'){
                    gridCache[i][j] = 0;
                }
                if(c == 'E'){
                    end = new Coordinate(i,j);
                }
            }
        }
        iterate(grid, gridCache);
        return gridCache[end.x()][end.y()] + "";
    }

    private void iterate(int[][] grid, int[][] gridCache){
        boolean somethingChanged = true;
        while(somethingChanged){
            somethingChanged = oneCycle(grid, gridCache);
        }
    }

    private boolean oneCycle(int[][] grid, int[][] gridCache){
        boolean somethingChanged = false;
        for(int i = 0; i < gridCache.length; i++){
            for(int j = 0; j < gridCache[0].length; j++){
                if(gridCache[i][j] != placeholder){
                    somethingChanged = oneCell(i, j, grid, gridCache);
                }
            }
        }
        return somethingChanged;
    }

    private boolean oneCell(int i, int j, int[][] grid, int[][] gridCache){
        boolean somethingChanged = false;
        for(int k = 0; k < xs.length; k++){
            int newx = i + xs[k];
            int newy = j + ys[k];
            if(safe(newx, newy, grid) && grid[newx][newy] != 1000){
                int newVal = gridCache[i][j] + getDifference(i,j,newx,newy, grid) + 1;
                if(gridCache[newx][newy] > newVal){
                    somethingChanged = true;
                    gridCache[newx][newy] = newVal;
                }
            }
        }
        return somethingChanged;
    }

    private boolean safe(int x, int y, int[][] grid){
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
    }

    private int getDifference(int x1, int y1, int x2, int y2, int[][] grid){
        int difference = grid[x2][y2] - grid[x1][y1];
        difference = Math.min(Math.abs(difference), 10- Math.abs(difference));
        return difference;
    }
}
