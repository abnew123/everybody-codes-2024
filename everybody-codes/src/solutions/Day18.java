package solutions;

import java.util.*;

public class Day18 {
    public String solve(int part, Scanner in) {
        int answer;
        List<String> lines = new ArrayList<>();
        while(in.hasNext()){
            lines.add(in.nextLine());
        }
        int[][] grid = new int[lines.size()][lines.get(0).length()];
        List<Coordinate> palms = new ArrayList<>();
        Set<Coordinate> water = new HashSet<>();
        List<Coordinate> newWater = new ArrayList<>();
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                char c = lines.get(i).charAt(j);
                if(c == '.'){
                    grid[i][j] = -1;
                    if(j == 0 || j == grid[0].length - 1){
                        newWater.add(new Coordinate(i,j));
                        grid[i][j] = 0;
                    }
                }
                if(c == '#'){
                    grid[i][j] = 10000;
                }
                if(c == 'P'){
                    grid[i][j] = -1;
                    palms.add(new Coordinate(i,j));
                }
            }
        }
        if(part != 3){
            answer = runSim(palms, newWater, grid, water, Integer.MAX_VALUE)[0];
        }
        else{
            answer = Integer.MAX_VALUE;
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length;j++){
                    if(grid[i][j] != 10000 && lines.get(i).charAt(j) != 'P'){
                        List<Coordinate> newWaterCopy = new ArrayList<>();
                        newWaterCopy.add(new Coordinate(i,j));
                        Set<Coordinate> waterCopy = new HashSet<>();
                        List<Coordinate> palmsCopy = new ArrayList<>(palms);
                        int[][] gridCopy = new int[grid.length][grid[0].length];
                        for(int k = 0; k < gridCopy.length; k++){
                            System.arraycopy(grid[k], 0, gridCopy[k], 0, gridCopy[0].length);
                        }
                        gridCopy[i][j] = 0;
                        int total = runSim(palmsCopy, newWaterCopy, gridCopy, waterCopy, answer)[1];
                        answer = Math.min(answer, total);
                    }
                }
            }
        }
        return answer + "";
    }

    private boolean safe(int x, int y, int[][] grid){
        return x >=0 && y >=0 && x < grid.length && y < grid[0].length;
    }

    private int get(int x, int y, int[][] grid){
        if(!safe(x,y,grid)){
            return 10000;
        }
        else{
            return grid[x][y];
        }
    }

    private int[] runSim(List<Coordinate> palms, List<Coordinate> newWater, int[][] grid, Set<Coordinate> water, int bestSoFar){
        int answer = 0;
        int total = 0;
        int[] xs = new int[]{1,-1,0,0};
        int[] ys = new int[]{0,0,1,-1};
        while(!palms.isEmpty() && !newWater.isEmpty() && total < bestSoFar){
            List<Coordinate> newPalms = new ArrayList<>();
            List<Coordinate> tmpWater = new ArrayList<>();
            for(Coordinate w: newWater){
                for(int i = 0; i < xs.length; i++){
                    int newx = w.x() + xs[i];
                    int newy = w.y() + ys[i];
                    if(!water.contains(new Coordinate(newx,newy)) && get(newx, newy, grid) != 10000 ){
                        tmpWater.add(new Coordinate(newx, newy));
                        if(grid[newx][newy] != -1){
                            grid[newx][newy] = Math.min(grid[w.x()][w.y()] + 1, grid[newx][newy]);
                        }
                        else{
                            grid[newx][newy] = grid[w.x()][w.y()] + 1;
                        }
                    }
                }
            }
            water.addAll(newWater);
            newWater = new ArrayList<>(tmpWater);
            for(Coordinate p: palms){
                if(!newWater.contains(p)){
                    newPalms.add(p);
                }
                else{
                    answer = Math.max(answer, grid[p.x()][p.y()]);
                    total += grid[p.x()][p.y()];
                }
            }
            palms = newPalms;
        }
        if(!palms.isEmpty()){
            return new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
        }
        return new int[]{answer, total};
    }
}
