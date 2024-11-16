package solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day09 {

    public String solve(int part, Scanner in) {
        List<Integer> sparkballs = new ArrayList<>();
        int beetles = 0;
        while(in.hasNext())
            sparkballs.add(in.nextInt());
        if(part == 1){
            int[] values = {0,1,2,1,2,1,2,3,2,3};
            beetles += sparkballs.stream().mapToInt(a -> values[a%10] + a/10).sum();
        }
        if(part == 2){
            Map<Integer,Integer> values = Map.of(0,0,29,2,28,2,27,2,26,2,23,2,22,2,21,2,17,2);
            beetles += sparkballs.stream().mapToInt(a -> a/30 + values.getOrDefault(a % 30, 1)).sum();
        }
        if(part == 3) {
            beetles += sparkballs.stream().mapToInt(a -> a / 101 + (a % 202 == 0 ? 0 : 1)).sum();
        }
        return beetles+"";
    }
}
