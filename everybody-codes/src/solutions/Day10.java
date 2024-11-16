package solutions;

import java.util.*;

public class Day10 {

    static char[][] input;
    public String solve(int part, Scanner in) {
        long answer = 0;
        List<String> lines = new ArrayList<>();
        while(in.hasNext()){
            lines.add(in.nextLine());
        }
        if(part == 1){
            Grid grid = new Grid(new char[lines.size()][lines.getFirst().length()], 0, 0);
            for(int i = 0; i < lines.size(); i++){
                for(int j = 0; j < lines.getFirst().length(); j++){
                    grid.grid[j][i] = lines.get(i).charAt(j);
                }
            }
            return runicWord(grid, 1);
        }
        if(part == 2){
            List<Grid> grids = new ArrayList<>();
            List<List<String>> tablets = new ArrayList<>();
            for(int i = 0; i < (lines.getFirst().length() + 1) / 9; i++){
                tablets.add(new ArrayList<>());
            }
            for(String line: lines){
                if(line.trim().isEmpty()){
                    continue;
                }
                String[] parts = line.split(" ");
                for(int i = 0; i < parts.length; i++){
                    tablets.get(i).add(parts[i]);
                }
            }

            for(List<String> tablet: tablets){
                grids.addAll(constructGrids(tablet, 8, 0));
            }

            for(Grid grid: grids){
                answer += power(runicWord(grid, 2));
            }
        }
        if(part == 3){
            input = new char[lines.size()][lines.get(0).length()];
            for(int i = 0; i < lines.size(); i++){
                for(int j = 0; j < lines.get(0).length(); j++){
                    input[i][j] = lines.get(i).charAt(j);
                }
            }
            List<Grid> grids = new ArrayList<>();
            List<List<String>> tablets = new ArrayList<>();
            for(int i = 0; i < lines.getFirst().length() / 6; i++){
                tablets.add(new ArrayList<>());
            }
            for(String line: lines){
                for(int j = 0; j < line.length()-3; j+=6){
                    tablets.get(j/6).add(line.substring(j, j+8));
                }
            }

            for(int i = 0; i < tablets.size(); i++){
                grids.addAll(constructGrids(tablets.get(i), 6, i));
            }

            for(Grid grid: grids){
                answer += power(runicWord(grid,3 ));
            }
            for(char[] inputRow: input){
                for(char c: inputRow){
                    System.out.print(c);
                }
                System.out.println();
            }
        }

        return answer+"";
    }

    private String runicWord(Grid grid, int part){
        StringBuilder answer = new StringBuilder();
        for(int i = 2; i < 6; i++){
            for(int j = 2; j < 6; j++){
                answer.append(findMatch(grid, i, j));
            }
        }
        if(part == 3){
            if(answer.toString().contains("-")){
                return "0";
            }
            if(answer.toString().contains("0")){
                String newAnswer = rowHelper(grid);
                if(newAnswer.contains("0")){
                    answer = new StringBuilder(colHelper(grid, newAnswer));
                }
                else{
                    answer = new StringBuilder(newAnswer);
                }
            }
        }
        return answer.toString();
    }

    private String rowHelper(Grid grid){
        String newAnswer = "";
        for(int i = 2; i < 6; i++){
            Map<Character, Integer> map = new HashMap<>();
            StringBuilder rowWord = new StringBuilder();
            for(int j = 0; j < 8; j++){
                if(grid.grid[j][i] != '.'){
                    map.put(grid.grid[j][i], map.getOrDefault(grid.grid[j][i], 0) + 1);
                }
            }
            for(int j = 2; j < 6; j++){
                char match = findMatch(grid, i, j);
                map.put(match, map.getOrDefault(match, 0) - 1);
                rowWord.append(match);
            }
            if(rowWord.toString().contains("0")){
                for(Character key: map.keySet()){
                    if(map.get(key) == -1 && key != '0'){
                        return "-";
                    }
                    if(map.get(key) == 1 && key != '?' && map.get('0') == -1){
                        int row = grid.i;
                        int col = grid.j;
                        int delta = rowWord.toString().indexOf('0') + 2;
                        List<Integer> qIndices = new ArrayList<>();
                        for(int k = col; k < col + 8; k++){
                            if(input[k][row + delta] == '?' || input[k][row + delta] == key) {
                                qIndices.add(k);
                            }
                        }
                        if(qIndices.size() == 1){
                            input[qIndices.getFirst()][row + delta] = key;
                        }
                        rowWord = new StringBuilder(rowWord.toString().replace('0', key));
                    }
                }
            }
            newAnswer += rowWord;
        }
        return newAnswer;
    }

    private String colHelper(Grid grid, String rowHelper){
        StringBuilder newAnswer = new StringBuilder();
        for(int i = 2; i < 6; i++){
            Map<Character, Integer> map = new HashMap<>();
            StringBuilder colWord = new StringBuilder();
            for(int j = 0; j < 8; j++){
                if(grid.grid[i][j] != '.'){
                    map.put(grid.grid[i][j], map.getOrDefault(grid.grid[i][j], 0) + 1);
                }
            }
            for(int j = 2; j < 6; j++){
                char match = findMatch(grid, j, i);
                map.put(match, map.getOrDefault(match, 0) - 1);
                colWord.append(match);
            }
            if(colWord.toString().contains("0")){
                for(Character key: map.keySet()){
                    if(map.get(key) == 1 && key != '?' && map.get('0') == -1){
                        int row = grid.i;
                        int col = grid.j;
                        int delta = colWord.toString().indexOf('0') + 2;
                        List<Integer> qIndices = new ArrayList<>();
                        for(int k = row; k < row + 8; k++){
                            if(input[delta + col][k] == '?' || input[delta + col][k] == key) {
                                qIndices.add(k);
                            }
                        }
                        if(qIndices.size() == 1){
                            input[delta + col][qIndices.getFirst()] = key;
                        }
                        colWord = new StringBuilder(colWord.toString().replace('0', key));
                    }
                }
            }
            newAnswer.append(colWord);
        }
        StringBuilder realAnswer = new StringBuilder();
        for(int i = 0; i < 4; i++){
            for(int j = 0;  j < 4; j++){
                realAnswer.append(newAnswer.substring(4 * j + i, 4 * j + i + 1));
            }
        }
        for(int i = 0; i < 16; i++){
            if(realAnswer.substring(i, i+1).equals("0")){
                realAnswer = new StringBuilder(realAnswer.substring(0, i) + rowHelper.charAt(i) + realAnswer.substring(i + 1));
            }
        }
        return realAnswer.toString();
    }

    private List<Grid> constructGrids(List<String> gridList, int bump, int row){
        List<Grid> grids = new ArrayList<>();
        for(int k = 0; k < gridList.size() - bump + 1; k+=bump){
            Grid grid = new Grid(new char[8][8], row * 6, k);
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    grid.grid[j][i] = gridList.get(i + k).charAt(j);
                }
            }
            grids.add(grid);
        }
        return grids;
    }

    private int power(String runicWord){
        if(runicWord.contains("0") || runicWord.contains("?")){
            return 0;
        }
        int answer = 0;
        for(int i = 0; i < runicWord.length(); i++){
            answer+= (runicWord.charAt(i) - 'A' + 1) * (i + 1);
        }
        return answer;
    }

    public char findMatch(Grid grid, int i, int j){
        Set<Character> seen = new HashSet<>();
        Set<Character> rowSet = new HashSet<>();
        Set<Character> colSet = new HashSet<>();
        boolean question = false;
        List<Character> potential = new ArrayList<>();
        for (char[] chars : grid.grid) {
            if(chars[i] == '?'){
                question = true;
            }
            if (chars[i] != '.' && chars[i] != '?') {
                seen.add(chars[i]);
                if(!rowSet.add(chars[i])){
                    return '-';
                }

            }
        }
        for(int k = 0; k < grid.grid[0].length; k++){
            if(grid.grid[j][k] == '?'){
                question = true;
            }
            if(grid.grid[j][k] != '.' && grid.grid[j][k] != '?') {
                if(!seen.add(grid.grid[j][k])){
                    potential.add(grid.grid[j][k]);
                }
                if(!colSet.add(grid.grid[j][k])){
                    return '-';
                }

            }
        }
        if(potential.size() == 1){
            return potential.get(0);
        }
        if(potential.size() > 1){
            return '-';
        }

        return question?'0':'-';
    }
}

class Grid{
    char[][] grid;

    int i;
    int j;

    public Grid(char[][] g, int row, int col){
        grid = g;
        i = row;
        j = col;
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder(i + " " + j);
        result.append("\n");
        for(char[] row: grid){
            result.append(Arrays.toString(row)).append("\n");
        }
        return result.toString();
    }
}