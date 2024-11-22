package solutions;

import java.util.*;

public class Day14 {

    int[]xs = new int[]{1,-1,0,0,0,0};
    int[]ys = new int[]{0,0,1,-1,0,0};
    int[]zs = new int[]{0,0,0,0,1,-1};

    public String solve(int part, Scanner in) {
        long answer = 0;
        List<String> lines = new ArrayList<>();
        while(in.hasNext()){
            lines.add(in.nextLine());
        }
        if(part == 1){
            long height = 0;
            for(String growthStep: lines.getFirst().split(",")){
                if(growthStep.contains("U")){
                    height += Integer.parseInt(growthStep.substring(1));
                    answer = Math.max(answer, height);
                }
                if (growthStep.contains("D")) {
                    height -= Integer.parseInt(growthStep.substring(1));
                }
            }
        }
        if(part == 2){
            Set<Coordinate3D> coordinates = new HashSet<>();
            for(String line: lines){
                addCoords(line, coordinates, new HashSet<>());
            }
            answer = coordinates.size();
        }
        if(part == 3){
            Set<Coordinate3D> trunk = new HashSet<>();
            Set<Coordinate3D> leaves = new HashSet<>();
            for(String line: lines){
                addCoords(line, trunk, leaves);
            }
            answer = computeMurkiness(leaves, trunk);
        }
        return answer + "";
    }

    private long computeMurkiness(Set<Coordinate3D> leaves, Set<Coordinate3D> trunk){
        long answer = Long.MAX_VALUE;
        Set<Integer> mainTrunk = new HashSet<>();
        for(Coordinate3D m: trunk){
            if(m.x() == 0 && m.z() == 0){
                mainTrunk.add(m.y());
            }
        }
        for(Integer m: mainTrunk){
            long potential = 0;
            for(Coordinate3D leaf: leaves){
                potential += distance(leaf, new Coordinate3D(0,m,0), trunk);
            }
            answer = Math.min(answer, potential);
        }
        return answer;
    }

    private int distance(Coordinate3D start, Coordinate3D end, Set<Coordinate3D> trunk){
        Set<Coordinate3D> queue = new HashSet<>();
        Set<Coordinate3D> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);
        int counter = 0;
        while(!queue.isEmpty()){
            if(queue.contains(end)){
                return counter;
            }
            Set<Coordinate3D> nextLayer = new HashSet<>();
            for(Coordinate3D q: queue){
                for(int i = 0; i < 6; i++){
                    Coordinate3D m = new Coordinate3D(q.x() + xs[i], q.y() + ys[i], q.z() + zs[i]);
                    if(trunk.contains(m) && !visited.contains(m)){
                        nextLayer.add(m);
                    }
                }
            }
            counter++;
            visited.addAll(nextLayer);
            queue = nextLayer;
        }
        return Integer.MAX_VALUE;
    }

    private void addCoords(String line, Set<Coordinate3D> coords, Set<Coordinate3D> leaves){
        String[] parts = line.split(",");
        int[] position = new int[]{0,0,0};
        Map<String, Integer> directions = Map.of("R", 0, "L", 1, "U", 2, "D", 3, "F",4, "B", 5);
        for(String p: parts){
            String direction = p.substring(0,1);
            int distance = Integer.parseInt(p.substring(1));
            for(int i = 0; i < distance; i++){
                position[0] += xs[directions.get(direction)];
                position[1] += ys[directions.get(direction)];
                position[2] += zs[directions.get(direction)];
                coords.add(new Coordinate3D(position[0],position[1],position[2]));
            }
        }
        leaves.add(new Coordinate3D(position[0],position[1],position[2]));
    }
}

record Coordinate3D(int x, int y, int z){}