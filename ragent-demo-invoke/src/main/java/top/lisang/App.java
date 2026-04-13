package top.lisang;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello, World from Maven!");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        
        if (args.length > 0) {
            System.out.println("Welcome, " + args[0] + "!");
        } else {
            System.out.println("Tip: Pass your name as argument to see a greeting!");
        }
    }
    
    public String greet(String name) {
        return "Hello, " + name + "!";
    }
}
