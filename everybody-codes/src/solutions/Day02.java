package solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Day02 {
    public String solve(int part, Scanner in) {
        int answer = 0;
        List<String> words = new ArrayList<>(List.of(in.nextLine().split(":")[1].split(",")));
        in.nextLine();
        if(part == 1){
            String[] inscription = in.nextLine().replaceAll("[,.]","").split(" ");
            for(String word: inscription){
                for(String powerWord: words){
                    if(word.contains(powerWord)){
                        answer++;
                    }
                }
            }
        }
        if(part == 2){
            while(in.hasNext()){
                answer+= runicSymbols(in.nextLine(), words);
            }
        }
        if(part == 3){
            List<String> backwardsWords = new ArrayList<>();
            for(String word: words){
                backwardsWords.add(new StringBuilder(word).reverse().toString());
            }
            words.addAll(backwardsWords);
            List<String> armor = new ArrayList<>();
            while(in.hasNext()){
                armor.add(in.nextLine());
            }
            char[][] grid = new char[armor.size()][armor.get(0).length()];
            for(int i = 0; i < armor.size(); i++){
                for(int j = 0; j < armor.get(0).length(); j++){
                    grid[i][j] = armor.get(i).charAt(j);
                }
            }
            boolean[][] runic = new boolean[armor.size()][armor.get(0).length()];
            for(int i = 0; i < armor.size(); i++){
                rowHelper(i, grid, runic, words);
            }
            for(int j = 0; j < armor.get(0).length(); j++){
                colHelper(j, grid, runic, words);
            }

            for(boolean[] row: runic){
                for(boolean b: row){
                    if(b){
                        answer++;
                    }
                }
            }

        }

        return answer+"";
    }

    private void rowHelper(int i, char[][] grid, boolean[][] runic, List<String> words){
        for(int j = 0; j < grid[0].length; j++){
            String word = constructWord(grid, i, j, true);
            for(String w: words) {
                if (word.startsWith(w)) {
                    for(int k = 0; k < w.length(); k++){
                        runic[i][(j + k) % grid[0].length] = true;
                    }
                }
            }
        }

    }

    private void colHelper(int j, char[][] grid, boolean[][] runic, List<String> words){
        String word = constructWord(grid, 0, j, false);
        for(int i = 0; i < word.length(); i++){
            String partial = word.substring(i);
            for(String w: words) {
                if (partial.startsWith(w)) {
                    for(int k = 0; k < w.length(); k++){
                        runic[k + i][j] = true;
                    }
                }
            }
        }



    }

    private String constructWord(char[][] grid, int i, int j, boolean horizontal){
        if(horizontal){
            String answer = "";
            for(int k = j; k < grid[0].length + j; k++){
                answer += grid[i][k%grid[0].length];
            }
            return answer;
        }
        else{
            String answer = "";
            for(int k = i; k < grid.length + i; k++){
                answer += grid[k % grid.length][j];
            }
            return answer;
        }
    }



    private int runicSymbols(String line, List<String> words){
        int answer = 0;
        boolean[] runic = new boolean[line.length()];
        String line2 = new StringBuffer(line).reverse().toString();
        boolean[] runic2 = new boolean[line.length()];
        for(int i = 0; i < line.length(); i++){
            for(String word: words){
                if(i + word.length() <= line.length() && line.startsWith(word, i)){
                    for(int j = i; j < i + word.length(); j++){
                        runic[j] = true;
                    }
                }
            }
        }

        for(int i = 0; i < line.length(); i++){
            for(String word: words){
                if(i + word.length() <= line.length() && line2.startsWith(word, i)){
                    for(int j = i; j < i + word.length(); j++){
                        runic2[j] = true;
                    }
                }
            }
        }

        for(int i = 0; i < line.length(); i++){
            if(runic[i] || runic2[line.length() - i - 1]){
                answer++;
            }
        }
        return answer;

    }
}
