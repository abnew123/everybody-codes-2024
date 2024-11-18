package solutions;

import java.util.*;

public class Day11 {
    public String solve(int part, Scanner in) {
        long answer = 0;
        Set<String> possible = new HashSet<>();
        Map<String, List<String>> growth = new HashMap<>();
        while(in.hasNext()){
            String line = in.nextLine();
            growth.put(line.split(":")[0], List.of(line.split(":")[1].split(",")));
            possible.addAll(List.of(line.split(":")[1].split(",")));
        }
        if(part == 1){
            answer = numTermites(growth, 4, "A");
        }
        if(part == 2){
            answer = numTermites(growth, 10, "Z");
        }
        if(part == 3){
            long min = Long.MAX_VALUE;
            long max = Long.MIN_VALUE;

            for(String possibility: possible){
                long result = numTermites(growth, 20, possibility);
                min = Math.min(min, result);
                max = Math.max(max, result);
            }
           answer = max - min;
        }



        return answer+"";
    }

    private long numTermites(Map<String, List<String>> growth, int days, String start){
        Map<String, Long> termites = new HashMap<>();
        termites.put(start, 1L);
        int counter = days;
        while(counter > 0){
            counter--;
            Map<String, Long> newTermites = new HashMap<>();
            for(String termite: termites.keySet()){
                for(String spawn: growth.get(termite)){
                    newTermites.put(spawn, newTermites.getOrDefault(spawn,0L) + termites.get(termite));
                }
            }
            termites = newTermites;
        }
        long value = 0;
        for(Long population: termites.values()){
            value+=population;
        }
        return value;
    }
}
