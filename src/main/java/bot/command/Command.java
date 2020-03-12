package bot.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Command {
    START("start"),
    NEW_TASK("task"),
    CANCEL("cancel"),
    HELP("help"),
    SETTINGS("settings"),
    TIMEZONE("timezone"),
    NOT_A_COMMAND,
    UNKNOWN;

    private static final Map<String, Command> lookup = new HashMap<>();

    static {
        for (Command command : Command.values()) {
            lookup.put(command.text, command);
        }
    }

    private String text;
    private List<String> parameters;

    Command(String text) {
        this.text = text;
    }

    Command() {
    }

    public static Command parse(String message) {
        if (!message.startsWith("/")) {
            return NOT_A_COMMAND;
        }

        List<String> parameters = Arrays.asList(message.split(" "));
        String textCommand = parameters.get(0).substring(1);

        Command command = lookup.get(textCommand);
        if (command == null) {
            return UNKNOWN;
        }

        command.parameters = parameters;

        return command;
    }

    @Override
    public String toString() {
        return "/" + text;
    }

    public List<String> getParameters() {
        return Collections.unmodifiableList(parameters);
    }
}
