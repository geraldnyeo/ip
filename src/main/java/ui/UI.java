package ui;

import java.util.Scanner;

public class UI {

    public static void printIntro(String name) {
        String banner = "  ____                               \n" +
                " / ___|__ _ ___ ___  __ ___   ____ _\n" +
                "| |   / _` / __/ __|/ _` \\ \\ / / _` |\n" +
                "| |__| (_| \\__ \\__ \\ (_| |\\ V / (_| |\n" +
                " \\____\\__,_|___/___/\\__,_| \\_/ \\__,_|";
        System.out.println(banner);

        System.out.println("__________________________________________________");
        System.out.println("Hi! I'm " + name + ".");
        System.out.println("How can I help you today?");
    }

    public static void printBorder() {
        System.out.println("__________________________________________________");
    }

    public static String scanUserInput() {
        Scanner scanner = new Scanner(System.in);

        printBorder();
        String input = scanner.nextLine();
        return input;
    }
}
