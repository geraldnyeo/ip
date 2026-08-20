import java.util.Scanner;

public class Cassava {
    private static String name = "Cassava";

    private static Item[] items = new Item[100];
    private static int numItems = 0;

    public static void printIntro() {
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
        String command = scanner.nextLine();
        return command;
    }

    public static boolean handleCommand(String command) {
        printBorder();

        if (command.equals("")) {
            return handleInvalid("You have not entered any text.");
        }

        String[] args = command.split("\s");

        if (args[0].equals("list")) {
            return handleList();
        } else if (args[0].equals("bye")) {
            return handleExit();
        } else if (args[0].equals("mark")) {
            if (args.length <= 1) {
                return handleInvalid("You did not specify an item to mark.");
            }
            return handleMark(args[1]);
        } else if (args[0].equals("unmark")) {
            if (args.length <= 1) {
                return handleInvalid("You did not specify an item to unmark.");
            }
            return handleUnmark(args[1]);
        } else {
            return handleAdd(command);
        }
    }

    public static boolean handleAdd(String description) {
        if (numItems < 100) {
            Item item = new Item(description);
            items[numItems++] = item;
            System.out.println("Added: " + description);
        } else {
            System.out.println("Sorry, there's no more space to add further items...");
        }

        return false;
    }

    public static boolean handleList() {
        if (numItems == 0) {
            System.out.println("No items have been added yet...");
        }

        for (int i = 0; i < numItems; ++i) {
            System.out.println((i+1) + ". " + items[i].toString());
        }

        return false;
    }

    public static boolean handleMark(String index_arg) {
        try {
            int index = Integer.parseInt(index_arg) - 1;
            if (index > numItems) {
                return handleInvalid("There is no such item, I cannot mark it.");
            }
            items[index].mark();

            System.out.println(items[index].toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid("Please use only numbers to specify the item you wish to mark.");
        }
    }

    public static boolean handleUnmark(String index_arg) {
        try {
            int index = Integer.parseInt(index_arg) - 1;
            if (index > numItems) {
                return handleInvalid("There is no such item, I cannot mark it.");
            }
            items[index].unmark();

            System.out.println(items[index].toString());

            return false;
        } catch (NumberFormatException e) {
            return handleInvalid("Please use only numbers to specify the item you wish to mark.");
        }
    }

    public static boolean handleInvalid(String msg) {
        System.out.println(msg);
        return false;
    }

    public static boolean handleExit() {
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
