package solutions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day04 {

    public String solve(int part, Scanner in) {
        long answer = 0;
        List<Integer> nails = new ArrayList<>();
        while(in.hasNext()){
            int nailLength = in.nextInt();
            nails.add(nailLength);
        }
        if(part == 3){
            //find median of nail lengths
            Collections.sort(nails);
            long median;
            int index = nails.size()/2;
            if(nails.size() % 2 == 0){
                median = (nails.get(index) + nails.get(index - 1))/2;
            }
            else{
                median = nails.get(index);
            }
            for(Integer nail: nails){
                answer += Math.abs(nail - median);
            }
        }
        else{
            long minLength = Integer.MAX_VALUE;
            for(Integer nail: nails){
                minLength = Math.min(minLength, nail);
                answer += nail;
            }
            answer -= minLength * nails.size();
        }
        return answer + "";
    }
}
