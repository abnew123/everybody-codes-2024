package solutions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day17 {
    public String solve(int part, Scanner in) {
        List<String> lines = new ArrayList<>();
        while(in.hasNext()){
            lines.add(in.nextLine());
        }
        List<StarCluster> clusters = new ArrayList<>();
        for(int i = 0; i < lines.size(); i++){
            for(int j = 0; j < lines.getFirst().length(); j++){
                if(lines.get(i).charAt(j) == '*'){
                    clusters.add(new StarCluster(new Coordinate(i,j)));
                }
            }
        }
        if(part != 3){
            while(clusters.size() > 1){
                int minDistance = Integer.MAX_VALUE;
                int index1 = 0;
                int index2 = 0;
                for(int i = 0; i < clusters.size(); i++){
                    for(int j = i + 1; j < clusters.size(); j++){
                        StarCluster one = clusters.get(i);
                        StarCluster two = clusters.get(j);
                        for(Coordinate o: one.stars){
                            for(Coordinate t: two.stars){
                                if(distance(o,t) < minDistance){
                                    minDistance = distance(o,t);
                                    index1 = i;
                                    index2 = j;
                                }
                            }
                        }
                    }
                }
                StarCluster second = clusters.remove(index2);
                StarCluster first = clusters.remove(index1);
                clusters.add(new StarCluster(second, first, minDistance));
            }
            return clusters.getFirst().value() + "";
        }
        else{
            boolean flag = true;
            while(flag){
                flag = false;
                int minDistance = Integer.MAX_VALUE;
                int index1 = 0;
                int index2 = 0;
                for(int i = 0; i < clusters.size(); i++){
                    for(int j = i + 1; j < clusters.size(); j++){
                        StarCluster one = clusters.get(i);
                        StarCluster two = clusters.get(j);
                        for(Coordinate o: one.stars){
                            for(Coordinate t: two.stars){
                                if(distance(o,t) < minDistance && distance(o,t) < 6){
                                    minDistance = distance(o,t);
                                    index1 = i;
                                    index2 = j;
                                    flag = true;
                                }
                            }
                        }
                    }
                }
                if(flag){
                    StarCluster second = clusters.remove(index2);
                    StarCluster first = clusters.remove(index1);
                    clusters.add(new StarCluster(second, first, minDistance));
                }
            }
            Collections.sort(clusters);
            return (long)clusters.getLast().value() * clusters.get(clusters.size() - 2).value() * clusters.get(clusters.size() - 3).value() + "";
        }
    }

    private int distance(Coordinate first, Coordinate second){
        return Math.abs(first.x() - second.x()) + Math.abs(first.y() - second.y());
    }
}

class StarCluster implements Comparable<StarCluster>{
    List<Coordinate> stars;
    int size;

    public StarCluster(Coordinate c){
        stars = new ArrayList<>();
        stars.add(c);
        size = 0;
    }

    public StarCluster(StarCluster first, StarCluster second, int distance){
        stars = new ArrayList<>();
        stars.addAll(first.stars);
        stars.addAll(second.stars);
        size = first.size + second.size + distance;
    }

    public int value(){
        return stars.size() + size;
    }

    @Override
    public int compareTo(StarCluster o) {
        return stars.size() + size - o.stars.size() - o.size;
    }

    @Override
    public boolean equals(Object obj){
        StarCluster o = (StarCluster) obj;
        return stars.size() + size == o.stars.size() + o.size;
    }
}

