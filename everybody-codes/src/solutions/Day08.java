package solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day08 {
    public String solve(int part, Scanner in){
        int value = in.nextInt();
        if(part == 1){
            int sqrt = (int)Math.sqrt(value);
            return (sqrt * 2 + 1) * ((sqrt + 1) * (sqrt + 1) - value) + "";
        }
        if(part == 2){
            int acolytes = 1111;
            int blocks = 20240000;
            int layer = 1;
            int layers = 0;
            while(blocks > 0){
                layers++;
                blocks -= layer * (layers * 2 - 1);
                layer = (layer * value) %acolytes;
            }
            return (blocks * -1) * (layers * 2 - 1) + "";
        }
        if(part == 3){
            int acolytes = 10;
            value %= acolytes;
            long blocks = 202400000000L;
            int layer = 1;
            int layers = 0;
            long currTot = 0;
            List<Integer> heights = new ArrayList<>();
            //height roughly grows at 15x columns, and each column loses around 5 blocks
            //so column = sqrt(blocks/15), and empty bricks = 5 * sqrt(blocks/15)
            while((currTot - ((int)Math.sqrt(blocks/15.0) * 5)) < blocks ){
                heights.add(layer);
                layers++;
                currTot += layer * (layers * 2L - 1);
                layer = ((layer * value) % acolytes) + acolytes;
            }
            int cumHeight = 0;
            int multiplier = ((layers * 2 - 1) * value) % acolytes;
            for(int i = heights.size() - 1; i >= 0; i--){
                cumHeight += heights.get(i);
                int removal = (multiplier * cumHeight) % acolytes;
                if(i == heights.size() - 1){
                    continue;
                }
                if(i == 0){
                    currTot -= removal;
                }
                else{
                    currTot -= 2 * removal;
                }
            }
            return currTot - blocks + "";
        }

        return "";
    }
}
