package solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day09 {

    public String solve(int part, Scanner in) {
        List<Integer> sparkles = new ArrayList<>();
        int answer = 0;
        while(in.hasNext())
            sparkles.add(in.nextInt());
        if(part == 1){
            int[] values = {0,1,2,1,2,1,2,3,2,3};
            for(int sparkle: sparkles)
                answer += values[sparkle%10] + sparkle/10;
        }
        if(part == 2){
            Map<Integer,Integer> values = Map.of(0,0,29,2,28,2,27,2,26,2,23,2,22,2,21,2,17,2);
            for(int sparkle: sparkles)
                answer += sparkle/30 + (values.getOrDefault(sparkle % 30, 1));
        }
        if(part == 3) {
            for (int sparkle : sparkles)
                answer += sparkle / 101 + (sparkle % 202 == 0 ? 0 : 1);
        }
        return answer+"";
    }
}
