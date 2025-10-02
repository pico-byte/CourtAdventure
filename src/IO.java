import java.util.Scanner;

public class IO {
    private static Scanner scanner = new Scanner(System.in);

    public static String ask(String question) {
        out(question);
        return scanner.nextLine();
    }

    public static void out(String s) {
        System.out.println(s);
    }
}
