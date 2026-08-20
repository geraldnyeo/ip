import java.util.Scanner;

public class Cassava {
    private static String name = "Cassava";

    public static void printIntro() {
        String banner = "  ____                               \n" +
                " / ___|__ _ ___ ___  __ ___   ____ _\n" +
                "| |   / _` / __/ __|/ _` \\ \\ / / _` |\n" +
                "| |__| (_| \\__ \\__ \\ (_| |\\ V / (_| |\n" +
                " \\____\\__,_|___/___/\\__,_| \\_/ \\__,_|\n";
        System.out.println(banner);

        System.out.println("__________________________________________________");
        System.out.println("Hi! I'm " + name + ".");
        System.out.println("How can I help you today?");
    }

    public static String scanUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("__________________________________________________");
        String command = scanner.nextLine();
        return command;
    }

    public static boolean handleCommand(String command) {
        if (command.equals("bye")) {
            return handleExit();
        } else {
            System.out.println("__________________________________________________");
            System.out.println(command);
            return false;
        }
    }

    public static boolean handleExit() {
        System.out.println("__________________________________________________");
        System.out.println("Bye! See you again soon.");
        return true;
    }

    public static void main(String[] args) {
        printIntro();

        boolean exit = false;
        while (!exit) {
            String command = scanUserInput();
            exit = handleCommand(command);
        }
    }
}
