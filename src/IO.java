import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class IO {
    public static String ask() {
        return "q";
    }

    public static String ask(String question) {
        out(question);
        return "q";
    }

    /*** Das printed ein sting s @param s ein beliebiger String */
    public static void out(String s) {
        System.out.println(s);
    }
}