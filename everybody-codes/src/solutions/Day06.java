package solutions;

import java.util.*;

public class Day06 {
    public String solve(int part, Scanner in){
        String answer = "";
        List<Tree> trees = new ArrayList<>();
        Tree root = null;
        while(in.hasNext()){
            String line = in.nextLine();
            trees.add(new Tree(line, part));
            if(trees.getLast().name.equals("RR")){
                root = trees.getLast();
            }
        }
        Map<Integer, List<String>> lengths = new HashMap<>();
        List<Tree> ordered = new ArrayList<>();
        ordered.add(root);
        int counter = 10000;
        while(!ordered.isEmpty() && counter > 0){
            Tree tree = ordered.removeFirst();
            List<Tree> potential = tree.propagate(trees, lengths, part);
            for(Tree p: potential){
                boolean newTree = true;
                for(Tree o: ordered){
                    if(o.name.equals(p.name)){
                        newTree = false;
                    }
                }
                if(newTree){
                    ordered.add(p);
                }
            }
            counter--;
        }
        for(Map.Entry<Integer, List<String>> entry : lengths.entrySet()){
            List<String> value = entry.getValue();
            if(value.size() == 1 && (answer.length() > value.getFirst().length() || answer.isEmpty())){
                answer = value.getFirst();
            }
        }
        return answer;
    }
}

class Tree{
    String name;

    String fullName;
    List<String> children = new ArrayList<>();

    int distance;

    boolean fruit;

    public Tree(String line, int part){
        //JG:CX,PK
        fruit = line.contains("@");
        String[] parts = line.split(":");
        name = parts[0];
        String[] branches = parts[1].split(",");
        children.addAll(Arrays.asList(branches));
        if(name.equals("RR")){
            fullName = (part == 1?"RR":"R");
        }
    }

    public List<Tree> propagate(List<Tree> trees, Map<Integer, List<String>> lengths, int part){
        List<Tree> result = new ArrayList<>();
        for(Tree tree: trees){
            for(String child: children){
                if(tree.name.equals(child)){
                    tree.distance = distance + 1;
                    tree.fullName = fullName + (part == 1?tree.name:tree.name.substring(0,1));
                    result.add(tree);
                }
            }
        }
        if(fruit){
            lengths.putIfAbsent(distance, new ArrayList<>());
            List<String> competitors = lengths.get(distance);
            competitors.add(fullName + "@");
            lengths.put(distance, competitors);
        }
        return result;
    }

}
