package solutions;

import java.util.*;

public class Day05 {
    public String solve(int part, Scanner in){
        List<List<Integer>> clappers = new ArrayList<>();
        while(in.hasNext()){
            String[] line = in.nextLine().split(" ");
            for(int i = 0; i < line.length; i++){
                if(clappers.size() <= i){
                    clappers.add(new ArrayList<>());
                }
                clappers.get(i).add(Integer.parseInt(line[i]));
            }
        }
        int counter = 0;
        if(part == 1){
            while(counter < 10){
                oneRound(clappers, counter);
                counter++;
            }
        }
        else if (part == 2){
            Map<String, Integer> shouted = new HashMap<>();
            while(true){
                oneRound(clappers, counter);
                String shout = shout(clappers);
                shouted.putIfAbsent(shout, 0);
                shouted.put(shout, shouted.get(shout) + 1);
                counter++;
                if(shouted.get(shout) == 2024){
                    return (Integer.parseInt(shout) * (long)counter) + "";
                }
            }
        }
        else if(part == 3){
            long max = 0;
            while(true){
                oneRound(clappers, counter);
                String shout = shout(clappers);
                if(Long.parseLong(shout) > max){
                    max = Long.parseLong(shout);
                    System.out.println(max);
                }
                counter++;
                if(counter > Integer.MAX_VALUE - 1){
                    return max + "";
                }
            }
        }

        return shout(clappers);
    }

    private void oneRound(List<List<Integer>> clappers, int counter){
        int clapper = clappers.get(counter%clappers.size()).removeFirst();
        List<Integer> newCol = clappers.get((counter + 1)% clappers.size());
        int clapperIndex = clapper%(2*newCol.size());

        //1>0, 2>1, 3>2, 4>3, 5>2, 6>1
        if(clapperIndex == 0){
            clapperIndex = 2*newCol.size();
        }
        if( clapperIndex <= newCol.size()){
            newCol.add(clapperIndex - 1, clapper);
        }
        else{
            newCol.add(2 * newCol.size() - clapperIndex + 1, clapper);
        }
    }

    private String shout(List<List<Integer>> clappers){
        StringBuilder result = new StringBuilder();
        for(List<Integer> column: clappers){
            result.append(column.getFirst());
        }
        return result.toString();
    }
}
