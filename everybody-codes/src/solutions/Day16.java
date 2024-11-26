package solutions;

import java.util.*;

public class Day16 {
    public String solve(int part, Scanner in) {
        String[] turnLine = in.nextLine().split(",");
        int[] turns = new int[turnLine.length];

        in.nextLine();
        List<Wheel> wheels = new ArrayList<>();
        for(int i = 0; i < turns.length; i++){
            turns[i] = Integer.parseInt(turnLine[i]);
            wheels.add(new Wheel());
        }
        while(in.hasNext()){
            String line = in.nextLine();
            for(int i = 0; i < wheels.size(); i++){
                if(line.length() >= i * 4 + 3 &&  !line.substring(i * 4, i * 4 + 3).trim().isEmpty()){
                    wheels.get(i).positions.add(line.substring(i * 4, i * 4 + 3));
                }
            }
        }
        if(part == 1){
            int spins = 100;
            StringBuilder result = new StringBuilder();
            while(spins > 0){
                spins--;
                for(int i = 0; i < wheels.size(); i++){
                    wheels.get(i).apply(turns[i]);
                }
                result.append(wheelState(wheels, part));
            }
            return result.toString();
        }
        if(part == 2){
            long spins = 202420242024L;
            long lcm = 1;
            for(Wheel wheel: wheels){
                lcm = (lcm * wheel.positions.size()) / gcd(lcm, wheel.positions.size());
            }
            long byteCoins = 0;
            for(long i = 0; i < lcm; i++){
                for(int j = 0; j < wheels.size(); j++){
                    wheels.get(j).apply(turns[j]);
                }
                String wheelState = wheelState(wheels, part);
                byteCoins += getCoins(wheelState);
            }
            byteCoins *= (spins / lcm);
            for(long i = 0; i < spins%lcm; i++){
                for(int j = 0; j < wheels.size(); j++){
                    wheels.get(j).apply(turns[j]);
                }
                String wheelState = wheelState(wheels, part);
                byteCoins += getCoins(wheelState);
            }
            return byteCoins + "";
        }
        if(part == 3){
            int spins = 256;
            int[][] potentialMaxResults = new int[spins + 1][spins * 2 + 1];
            int[][] potentialMinResults = new int[spins + 1][spins * 2 + 1];
            for(int i = 0; i < spins + 1; i++){
                for(int j = 0; j < spins * 2 + 1; j++){
                    potentialMaxResults[i][j] = Integer.MIN_VALUE;
                    potentialMinResults[i][j] = Integer.MAX_VALUE;
                }
            }
            potentialMaxResults[0][spins] = 0;
            potentialMinResults[0][spins] = 0;
            for(int i = 0; i < spins; i++){
                for(int j = 0; j < wheels.size(); j++){
                    wheels.get(j).apply(turns[j]);
                }
                for(int j = 0; j < spins * 2 + 1; j++){
                    if(potentialMaxResults[i][j] != Integer.MIN_VALUE){
                        for(Wheel wheel: wheels){
                            wheel.apply(j - spins);
                        }
                        int none = getCoins(wheelState(wheels, part));
                        for (Wheel wheel : wheels) {
                            wheel.apply(1);
                        }
                        int higher = getCoins(wheelState(wheels, part));
                        for (Wheel wheel : wheels) {
                            wheel.apply(-2);
                        }
                        int lower = getCoins(wheelState(wheels, part));
                        for (Wheel wheel : wheels) {
                            wheel.apply(1);
                        }
                        for(Wheel wheel: wheels){
                            wheel.apply(spins - j);
                        }
                        potentialMaxResults[i + 1][j] = Math.max(potentialMaxResults[i][j] + none, potentialMaxResults[i + 1][j]);
                        potentialMaxResults[i + 1][j + 1] = Math.max(potentialMaxResults[i][j] + higher, potentialMaxResults[i + 1][j+1]);
                        potentialMaxResults[i + 1][j - 1] = Math.max(potentialMaxResults[i][j] + lower, potentialMaxResults[i + 1][j-1]);
                    }

                    if(potentialMinResults[i][j] != Integer.MAX_VALUE){
                        for(Wheel wheel: wheels){
                            wheel.apply(j - spins);
                        }
                        int none = getCoins(wheelState(wheels, part));
                        for (Wheel wheel : wheels) {
                            wheel.apply(1);
                        }
                        int higher = getCoins(wheelState(wheels, part));
                        for (Wheel wheel : wheels) {
                            wheel.apply(-2);
                        }
                        int lower = getCoins(wheelState(wheels, part));
                        for (Wheel wheel : wheels) {
                            wheel.apply(1);
                        }
                        for(Wheel wheel: wheels){
                            wheel.apply(spins - j);
                        }
                        potentialMinResults[i + 1][j] = Math.min(potentialMinResults[i][j] + none, potentialMinResults[i + 1][j]);
                        potentialMinResults[i + 1][j + 1] = Math.min(potentialMinResults[i][j] + higher, potentialMinResults[i + 1][j+1]);
                        potentialMinResults[i + 1][j - 1] = Math.min(potentialMinResults[i][j] + lower, potentialMinResults[i + 1][j-1]);
                    }
                }
            }
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;
            for(int i = 0; i < spins * 2 + 1; i++){
                max = Math.max(max, potentialMaxResults[spins][i]);
            }

            for(int i = 0; i < spins * 2 + 1; i++){
                min = Math.min(min, potentialMinResults[spins][i]);
            }
            return max + " " + min;
        }

        return "";
    }

    private long gcd(long a, long b){
        if (a<b) return gcd(b,a);
        if (a%b==0) return b;
        else return gcd(b, a%b);
    }

    private String wheelState(List<Wheel> wheels, int part){
        StringBuilder result = new StringBuilder();
        for(Wheel wheel: wheels){
            if(part == 1){
                result.append(wheel.toString());
            }
            else{
                result.append(wheel.toString().charAt(0)).append(wheel.toString().charAt(2));
            }

        }
        return result.toString();
    }

    private int getCoins(String wheelState){
        int result = 0;
        Map<Character, Integer> frequencies = new HashMap<>();
        char[] letters = wheelState.toCharArray();
        for(char c: letters){
            frequencies.put(c, frequencies.getOrDefault(c, 0) + 1);
        }
        for(Integer val: frequencies.values()){
            result += val > 2? val - 2: 0;
        }
        return result;
    }
}

class Wheel{
    List<String> positions = new ArrayList<>();

    int position = 0;

    public void apply(int turn){
        position+=turn;
        position %= positions.size();
        position += positions.size();
        position %= positions.size();
    }

    public String toString(){
        return positions.get(position);
    }

}
