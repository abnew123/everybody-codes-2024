package solutions;

import java.util.*;

public class Day15 {

    int[] xs = new int[]{1,-1,0,0};
    int[] ys = new int[]{0,0,1,-1};

    public String solve(int part, Scanner in) {
        long answer = Long.MAX_VALUE;
        List<String> lines = new ArrayList<>();
        while(in.hasNext()){
            lines.add(in.nextLine());
        }
        Coordinate start = new Coordinate(0,0);
        int[][] grid = new int[lines.size()][lines.get(0).length()];
        List<List<Coordinate>> herbs = new ArrayList<>();
        for(int i = 0; i < 26; i++){
            herbs.add(new ArrayList<>());
        }

        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.get(i).length(); j++){
                char c = lines.get(i).charAt(j);

                if(c == '.'){
                    if(i == 0){
                        start = new Coordinate(i, j);
                    }
                    grid[i][j] = 0;
                }
                else if (c == '#' || c == '~'){
                    grid[i][j] = -1;
                }
                else{
                    grid[i][j] = c - 'A';
                    herbs.get(grid[i][j]).add(new Coordinate(i,j));
                }
            }
        }
        if(part == 1){
            answer = 2 * bfs(start, grid);
        }
        if(part == 2){
            StringBuilder input = new StringBuilder();
            for(int i = 0; i < herbs.size(); i++){
                if(!herbs.get(i).isEmpty()){
                    char c = (char) ('a' + i);
                    input.append(c);
                }
            }
            List<String> resultList = new ArrayList<>();
            generatePermutations("", input.toString(), resultList);
            for(String permutation: resultList){
                answer = Math.min(answer, path(permutation, herbs, grid, start));
            }
        }
        if(part == 3){
            long pivot1 = path("e", herbs, grid, start);
            long pivot2 = path("r", herbs, grid, start);
            long left = path("abcd", herbs, grid, start);
            long right = path("nopq", herbs, grid, start);
            long center = path("ghijer", herbs, grid, start);
            List<String> resultList = new ArrayList<>();
            generatePermutations("", "abcd", resultList);
            for(String permutation: resultList){
                long possibility = path(permutation, herbs, grid, start);
                if(possibility < left){
                    left = possibility;
                }
            }
            resultList = new ArrayList<>();
            generatePermutations("", "nopq", resultList);
            for(String permutation: resultList){
                long possibility = path(permutation, herbs, grid, start);
                if(possibility < right){
                    right = possibility;
                }
            }
            resultList = new ArrayList<>();
            generatePermutations("", "ghijer", resultList);
            for(String permutation: resultList){
                long possibility = path(permutation, herbs, grid, start);
                if(possibility < center){
                    center = possibility;
                }
            }
            answer = left - pivot1 + right - pivot2 + center;
        }
        return answer + "";
    }

    private long path(String herbOrder, List<List<Coordinate>> herbs, int[][] grid, Coordinate start){
        Map<Coordinate, Integer> beginCache = new HashMap<>();
        beginCache.put(start, 0);
        for(int i = 0; i < herbOrder.length(); i++){
            List<Coordinate> targets = herbs.get(herbOrder.charAt(i) - 'a');
            beginCache = oneCycle(beginCache, grid, targets);
        }
        beginCache = oneCycle(beginCache, grid, List.of(start));
        long min = Long.MAX_VALUE;
        for(Map.Entry<Coordinate, Integer> e: beginCache.entrySet()){
            min = Math.min(min, e.getValue());
        }
        return min;
    }

    private Map<Coordinate, Integer> oneCycle(Map<Coordinate, Integer> beginCache, int[][] grid, List<Coordinate> targets){
        Map<Coordinate, Integer> result = new HashMap<>();
        for(Map.Entry<Coordinate, Integer> e: beginCache.entrySet()){
            Coordinate start = e.getKey();
            Set<Coordinate> map = new HashSet<>();
            Set<Coordinate> visited = new HashSet<>();
            map.add(start);
            visited.add(start);
            int distance = e.getValue();
            while(!map.isEmpty()){
                distance++;
                Set<Coordinate> next = new HashSet<>();
                for(Coordinate coordinate: map){
                    for(int i = 0; i < xs.length; i++){
                        Coordinate newCoordinate = new Coordinate(coordinate.x() + xs[i], coordinate.y() + ys[i]);
                        if(targets.contains(newCoordinate)){
                            result.put(newCoordinate, Math.min(distance, result.getOrDefault(newCoordinate, Integer.MAX_VALUE)));
                        }
                        if(!visited.contains(newCoordinate) && safe(newCoordinate.x(),newCoordinate.y(),grid)){
                            if(grid[newCoordinate.x()][newCoordinate.y()] != -1){
                                next.add(newCoordinate);
                            }
                        }
                    }
                }
                visited.addAll(next);
                map = next;
            }
        }
        return result;
    }

    private void generatePermutations(String prefix, String remaining, List<String> result) {
        if (remaining.length() == 0) {
            result.add(prefix);
            return;
        }

        for (int i = 0; i < remaining.length(); i++) {
            String newPrefix = prefix + remaining.charAt(i);
            String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
            generatePermutations(newPrefix, newRemaining, result);
        }
    }

    private long bfs(Coordinate start, int[][] grid){
        int distance = 0;
        if(grid[start.x()][start.y()] == 1){
            return 0;
        }
        Set<Coordinate> map = new HashSet<>();
        Set<Coordinate> visited = new HashSet<>();
        map.add(start);
        visited.add(start);
        while(!map.isEmpty()){
            distance++;
            Set<Coordinate> next = new HashSet<>();
            for(Coordinate coordinate: map){
                for(int i = 0; i < xs.length; i++){
                    Coordinate newCoordinate = new Coordinate(coordinate.x() + xs[i], coordinate.y() + ys[i]);
                    if(!visited.contains(newCoordinate) && safe(newCoordinate.x(),newCoordinate.y(),grid)){
                        if(grid[newCoordinate.x()][newCoordinate.y()] == 'H' - 'A'){
                            return distance;
                        }
                        if(grid[newCoordinate.x()][newCoordinate.y()] == 0){
                            next.add(newCoordinate);
                        }
                    }
                }
            }
            map = next;
        }
        return distance;
    }

    private boolean safe(int x, int y, int[][] grid){
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
    }

}
