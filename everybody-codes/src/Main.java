import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class Main {

    private static final String DATA_DAY = "./data/day";
    private static final String SOLUTIONS_DAY = "solutions.Day";
    private static final String PART = " part ";

    public static void main(String[] args) throws Exception {

        //dynamically set depending on which day+part to run
        int day = 8;
        int part = 1;

        String zeroFilledDay = (day < 10 ? "0" : "") + day;
        File file = new File(DATA_DAY + zeroFilledDay + ".txt");
        try (Scanner in = new Scanner(file)) {
            Class<?> cls = Class.forName( SOLUTIONS_DAY+ zeroFilledDay);
            Method m = cls.getDeclaredMethod("solve", int.class, Scanner.class);
            String answer = (String) m.invoke(cls.getDeclaredConstructor().newInstance(), part, in);
            System.out.println(
                    "Day " + zeroFilledDay + PART + part + " solution: " + answer);
        }

    }
}