import java.util.Scanner;

public class IO {
    private static Scanner scanner = new Scanner(System.in);

    public static String ask(String question) {
        outLine(question);
        return scanner.nextLine();
    }

    public static void out(String s) {
        System.out.println(s);
    }

    public static void outLine(String s){
        System.out.print(s);
    }
}
