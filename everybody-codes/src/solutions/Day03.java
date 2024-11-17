package solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day03 {
    public String solve(int part, Scanner in) {
        int answer = 0;
        List<String> lines = new ArrayList<>();
        while(in.hasNext()){
            lines.add(in.nextLine());
        }
        int[][] grid = new int[lines.size()][lines.getFirst().length()];
        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(i).length(); j++){
                grid[i][j] = lines.get(i).charAt(j)=='.'?0:1;
            }
        }
        int miningDepth = 1;
        while(buildMines(grid, miningDepth, part)) miningDepth++;
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                answer += grid[i][j];
            }
        }
        return answer + "";
    }

    private boolean buildMines(int[][] grid, int mountainHeight, int part){
        boolean changed = false;
        int[] xs = new int[]{-1, 1, 0, 0};
        int[] ys = new int[]{0, 0, -1, 1};
        if(part == 3){
            xs = new int[]{-1,-1,-1,0,0,1,1,1};
            ys = new int[]{-1,0,1,-1,1,-1,0,1};
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int mines = 0;
                if(grid[i][j] == mountainHeight){
                    for(int k = 0; k < xs.length; k++){
                        if(safeArrayGet(grid, i+xs[k], j+ys[k]) >= mountainHeight){
                            mines++;
                        }
                    }
                    if(mines == xs.length){
                        grid[i][j] = mountainHeight+1;
                        changed = true;
                    }
                }
            }
        }
        return changed;
    }

    private int safeArrayGet(int[][] grid, int i, int j){
        if(i < 0 || i >= grid.length || j < 0 || j >= grid[i].length){
            return -1;
        }
        return grid[i][j];
    }
}
