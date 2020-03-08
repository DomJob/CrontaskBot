package configuration;

public class Messages {

    public static String helpMessage() {
        return "This is the help message";
    }

    public static String nameRequestedMessage() {
        return "Name your task";
    }

    public static String cronRequestedMessage() {
        return "Pick a cron schedule";
    }

    public static String taskCreatedMessage() {
        return "Task created!";
    }

    public static String defaultMessage() {
        return "You can create a new task with /newtask, or type /help for additional information.";
    }

    public static String startMessage() {
        return "Hello! " + defaultMessage();
    }

    public static String unknownCommand() {
        return "Unknown command. Type /commands for a list of all the commands.";
    }

    public static String invalidCronMessage() {
        return "Invalid cron format. Refer to https://crontab.guru/ for more information on cron expressions.";
    }

    public static String taskTriggeredMessage(String name) {
        return String.format("<b>Task:</b>\n\n%s", name);
    }

    public static String noOngoingOperation() {
        return "No operation to cancel.";
    }
}
