public class IOManager {


    public static String ask(){
        return "q";
    }

    public static String ask(String question){
        out(question);
        return "q";
    }

    /***
     Das printed ein sting s
     @param s ein beliebiger STring
     */

    public static void out(String s){
        System.out.println(s);
    }


}
