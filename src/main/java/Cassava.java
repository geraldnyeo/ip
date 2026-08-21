import task.Task;

import java.util.*;

public class Cassava {
    private static final List<String> valid_cmds = new ArrayList<String>(
            Arrays.asList(
                "list",
                "todo",
                "deadline",
                "event",
                "mark",
                "unmark",
                "delete",
                "bye"
            )
    );

    private static final String name = "Cassava";

    private static final List<Task> tasks = new ArrayList<Task>();

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
        String input = scanner.nextLine();
        return input;
    }

    public static HashMap<String, String> parseUserInput(String input) {
        HashMap<String, String> args = new HashMap<String, String>();
        String[] tokens = input.split("\s");

        args.put("command", tokens[0]);

        String param = tokens[0];
        StringBuilder option = new StringBuilder();
        for (int i = 1; i < tokens.length; ++i) {
            if (tokens[i].charAt(0) == '\\') {
                args.put(param, option.toString());
                param = tokens[i].substring(1);
                option = new StringBuilder();
            } else {
                option.append(tokens[i]);
            }
        }
        args.put(param, option.toString());

        return args;
    }

    public static void main(String[] args) {
        printIntro();

        boolean exit = false;
        while (!exit) {
            String input = scanUserInput();
            HashMap<String, String> input_args = parseUserInput(input);

            printBorder();
            exit = Handlers.handleInput(tasks, input_args, valid_cmds);
        }
    }
}
