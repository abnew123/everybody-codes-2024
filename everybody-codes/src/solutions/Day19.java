package solutions;

import java.util.*;

public class Day19 {

    boolean stop = false;
    Map<Coordinate, List<Coordinate>> path = new HashMap<>();

    public String solve(int part, Scanner in) {
        String line = in.nextLine();
        in.nextLine();
        List<String> lines = new ArrayList<>();
        while(in.hasNext()){
            lines.add(in.nextLine());
        }
        String[][] grid = new String[lines.size()][lines.get(0).length()];
        String[][] originGrid = new String[lines.size()][lines.get(0).length()];
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = lines.get(i).charAt(j) + " " + i + " " + j;
                originGrid[i][j] = lines.get(i).charAt(j) + " " + i + " " + j;
            }
        }
        int counter;
        int runs = 0;
        Map<Coordinate, Coordinate> convolution = new HashMap<>();
        Set<Coordinate> cycles = new HashSet<>();
        int goalCycles = 1048576000;
        while(!stop){
            if(runs == 0){
                counter = 0;
                for(int i = 1; i < grid.length - 1; i++) {
                    for (int j = 1; j < grid[0].length - 1; j++) {
                        char operation = line.charAt(counter%line.length());
                        counter++;
                        grid = rotate(grid, i, j, operation);
                    }
                }
                for(int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[0].length; j++) {
                        String[] parts = grid[i][j].split(" ");
                        Coordinate beginning = new Coordinate(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                        path.put(beginning, new ArrayList<>());
                        Coordinate end = new Coordinate(i,j);
                        path.get(beginning).add(end);
                        convolution.put(beginning, end);
                    }
                }
            }
            else{
                grid = helper(grid, convolution, cycles, originGrid, runs);
            }
            if(part == 1){
                for(String[] row: grid){
                    String full = Arrays.toString(row);
                    if(!full.contains("+") && !full.contains("-")){
                        return full;
                    }
                }
            }
            runs++;
        }
        String[][] finalGrid = new String[grid.length][grid[0].length];
        for(int i = 0; i < originGrid.length; i++){
            for(int j = 0; j < originGrid[0].length; j++){
                List<Coordinate> pathCycle = path.get(new Coordinate(i,j));
                Coordinate c = pathCycle.get((goalCycles - 1)%pathCycle.size());
                finalGrid[c.x()][c.y()] = originGrid[i][j];
            }
        }
        for(String[] row: finalGrid){
            String printable = prettyPrint(row);
            if(printable.contains("<")){
                return printable.substring(printable.indexOf('>') + 1, printable.indexOf('<'));
            }
        }
        return "";
    }

    private String prettyPrint(String[] row){
        String result = "";
        for(String val: row){
            result += val.split(" ")[0];
        }
        return result;
    }

    private String[][] helper(String[][] grid, Map<Coordinate, Coordinate> convolution, Set<Coordinate> cycles, String[][] originGrid, int runs){
        String[][] newGrid = new String[grid.length][grid[0].length];
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                Coordinate newPlace = convolution.get(new Coordinate(i,j));
                String[] parts = grid[i][j].split(" ");
                Coordinate originalValue = new Coordinate(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                newGrid[newPlace.x()][newPlace.y()] = grid[i][j];
                if(!cycles.contains(newPlace)){
                    List<Coordinate> get = path.get(originalValue);
                    get.add(newPlace);
                    path.put(originalValue, get);
                    if(newGrid[newPlace.x()][newPlace.y()].equals(originGrid[newPlace.x()][newPlace.y()])){
                        cycles.add(newPlace);
                        if(cycles.size() == newGrid.length * newGrid[0].length) {
                            stop = true;
                        }
                    }
                }
            }
        }
        return newGrid;
    }

    private String[][] rotate(String[][] grid, int x, int y, char operation){
        int delta = operation == 'R'?1:-1;
        int[]xs = new int[]{-1,0,1,1,1,0,-1,-1};
        int[]ys = new int[]{-1,-1,-1,0,1,1,1,0};
        List<String> vals = new ArrayList<>();
        for(int i = 0; i < xs.length; i++){
            vals.add(grid[x + xs[i]][y + ys[i]]);
        }
        String[][] newGrid = new String[grid.length][grid[0].length];
        for(int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                newGrid[i][j] = grid[i][j];
            }
        }
        for(int i = 0; i < xs.length; i++){
            newGrid[x + xs[i]][y + ys[i]] = vals.get((i + delta + vals.size()) %  vals.size());
        }
        return newGrid;
    }
}
