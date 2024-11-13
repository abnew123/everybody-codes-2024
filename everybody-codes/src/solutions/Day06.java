package solutions;
import java.util.*;

public class Day06 {
    public String solve(int part, Scanner in){
        List<Tree> trees = new ArrayList<>();
        Tree root = null;
        while(in.hasNext()){
            String line = in.nextLine();
            trees.add(new Tree(line, part));
            if(trees.getLast().name.equals("RR")){
                root = trees.getLast();
            }
        }
        List<Tree> bfs = new ArrayList<>();
        bfs.add(root);
        while(!bfs.isEmpty()){
            List<Tree> newBFS = new ArrayList<>();
            String fullName = "";
            int numFruits = 0;
            for(Tree tree: bfs){
                if(tree.propagate(trees, newBFS, part)){
                    numFruits++;
                    fullName = tree.fullName + "@";
                }
            }
            if(numFruits == 1){
                return fullName;
            }
            bfs = newBFS;
        }
        return "";
    }
}

class Tree{
    String name;
    String fullName;
    List<String> children = new ArrayList<>();
    boolean fruit;

    public Tree(String line, int part){
        fruit = line.contains("@");
        name = line.split(":")[0];
        children.addAll(Arrays.asList(line.split(":")[1].split(",")));
        if(name.equals("RR")){
            fullName = (part == 1?"RR":"R");
        }
    }

    public boolean propagate(List<Tree> trees, List<Tree> newTrees, int part){
        for(Tree tree: trees){
            for(String child: children){
                if(tree.name.equals(child)){
                    tree.fullName = fullName + (part == 1?tree.name:tree.name.substring(0,1));
                    newTrees.add(tree);
                }
            }
        }
        return fruit;
    }
}
