package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Parser {

    public static final List<String> valid_cmds = new ArrayList<String>(
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
                option.append(option.isEmpty() ? tokens[i] : " " + tokens[i]);
            }
        }
        args.put(param, option.toString());

        return args;
    }

}
