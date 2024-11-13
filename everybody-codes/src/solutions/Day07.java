package solutions;

import java.util.*;

public class Day07 {

    public String solve(int part, Scanner in){
        List<Competitor> competitors = new ArrayList<>();
        List<Integer> track = new ArrayList<>();
        List<List<String>> stringTrack = new ArrayList<>();
        boolean swap = false;
        while(in.hasNext()){
            String line = in.nextLine();
            if(line.isEmpty() && part != 1){
                swap = true;
            }
            if(!line.isEmpty() && swap){
                competitors.add(new Competitor(line, 10));
            }
            if(!line.isEmpty() && !swap){
                stringTrack.add(List.of(line.split("")));
            }
        }
        constructTrack(buildGrid(stringTrack), part, track);
        if(part == 3){
            //construct all 11 length strings consisting of +, -, and =
            String base = "+++++===---";
            Set<String> permutations =generatePermutations(base);

            for (String permutation : permutations) {
                StringBuilder line = new StringBuilder(permutation + ":");
                for (int i = 0; i < 10; i++) {
                    line.append(permutation.substring(i, i + 1));
                    line.append(",");
                }
                line.append(permutation.substring(10));
                competitors.add(new Competitor(line.toString(), 10));
            }
        }

        for(Competitor competitor: competitors){
            competitor.runCourse((part == 1)?10:track.size() * (part == 2?10:2024), track);
        }
        Collections.sort(competitors);
        StringBuilder answer = new StringBuilder();
        int winners = 0;
        for(Competitor competitor: competitors){
            if(competitor.name.equals("A") && (part == 3)){
                return winners + "";
            }
            winners++;
            answer.append(competitor.name);
        }
        return answer.toString();
    }

    private Set<String> generatePermutations(String base) {
        Set<String> permutations = new HashSet<>();
        if (base.length() == 1) {
            permutations.add(base);
            return permutations;
        } else {
            for(int i = 0; i < base.length(); i++) {
                String piece = base.substring(i, i + 1);
                Set<String> subPermutations = generatePermutations(base.substring(0, i) + base.substring(i + 1));
                for (String subPermutation : subPermutations) {
                    permutations.add(piece + subPermutation);
                }
            }
        }
        return permutations;
    }

    private void constructTrack(int[][] grid, int part, List<Integer> track){
        Coordinate startCoord = new Coordinate(0, 0);
        Set<Coordinate> seenCoords = new HashSet<>();
        Coordinate currentCoord = startCoord;
        int[] xs = new int[]{-1, 1, 0, 0};
        int[] ys = new int[]{0, 0, -1, 1};
        while (!seenCoords.contains(currentCoord)) {
            seenCoords.add(currentCoord);
            track.add(grid[currentCoord.y()][currentCoord.x()]);
            //look at neighbors
            Coordinate nextCoord = currentCoord;
            for(int i = 3; i >= 0; i--) {
                Coordinate neighbor = new Coordinate(currentCoord.x() + xs[i], currentCoord.y() + ys[i]);
                if (neighbor.x() >= 0 && neighbor.x() < grid[0].length && neighbor.y() >= 0 && neighbor.y() < grid.length) {
                    if (grid[neighbor.y()][neighbor.x()] != -100 && !seenCoords.contains(neighbor)) {
                        nextCoord = neighbor;
                    }
                }
            }
            System.out.print(nextCoord.x() + "," + nextCoord.y() + " ");
            currentCoord = nextCoord;
        }
        track.removeFirst();
        track.add(0);
    }

    private int[][] buildGrid(List<List<String>> stringTrack){
        int[][] grid = new int[stringTrack.size()][stringTrack.getFirst().size()];
        for (int i = 0; i < stringTrack.size(); i++) {
            for (int j = 0; j < stringTrack.get(i).size(); j++) {
                switch (stringTrack.get(i).get(j)){
                    case "-": grid[i][j] = -1; break;
                    case "+": grid[i][j] = 1; break;
                    case "=": grid[i][j] = 0; break;
                    case "S": grid[i][j] = 100; break;
                    default:grid[i][j] = -100;
                }
            }
        }
        return grid;
    }
}

class Competitor implements Comparable<Competitor>{
    int currentPower;
    long totalPower;
    List<Integer> course = new ArrayList<>();

    String name;

    public Competitor(String line, int initialPower){
        //I:=,=,+,=,-,-,+,-,+,+
        name = line.split(":")[0];
        for(String plan: line.split(":")[1].split(",")){
            switch (plan){
                case "-": course.add(-1); break;
                case "+": course.add(1); break;
                default: course.add(0);
            }
        }
        currentPower = initialPower;
    }

    public void runCourse(int courseLength, List<Integer> track){
        for(int i = 0; i < courseLength; i++){
            int trackDelta = track.get(i%track.size());
            int courseDelta = course.get(i%course.size());
            int delta = trackDelta == 0? courseDelta: trackDelta;
            currentPower += delta;
            if(currentPower < 0){
                currentPower = 0;
            }
            totalPower+= currentPower;
        }
    }

    public boolean equals(Object other){
        if (other instanceof Competitor competitor){
            return competitor.totalPower == totalPower;
        }
        else{
            return false;
        }
    }

    public int hashCode(){
        return course.hashCode();
    }

    @Override
    public int compareTo(Competitor o) {
        return (int) (o.totalPower%1e9 - totalPower%1e9);
    }

    public String toString(){
        return name + " " + totalPower;
    }
}

record Coordinate(int x, int y){}