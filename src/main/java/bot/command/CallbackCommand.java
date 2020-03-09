package bot.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CallbackCommand {
    DISMISS("dismiss"),
    SNOOZE("snooze");

    private static final Map<String, CallbackCommand> lookup = new HashMap<>();

    static {
        for (CallbackCommand command : CallbackCommand.values()) {
            lookup.put(command.text, command);
        }
    }

    private String text;
    private List<String> parameters;

    CallbackCommand(String text) {
        this.text = text;
    }

    public static CallbackCommand parse(String message) {
        List<String> parameters = Arrays.asList(message.split(" "));
        String textCommand = parameters.get(0);

        CallbackCommand command = lookup.get(textCommand);

        command.parameters = parameters;

        return command;
    }

    public List<String> getParameters() {
        return Collections.unmodifiableList(parameters);
    }
}
