package configuration;

public class MessageFormatter {

    public static String getHelpMessage() {
        return "This is the help message";
    }

    public static String getNameRequestMessage() {
        return "Name your task";
    }

    public static String getCronRequestMessage() {
        return "Pick a cron schedule";
    }

    public static String getTaskCreatedMessage() {
        return "Task created!";
    }

    public static String getDefaultMessage() {
        return "You can create a new task with /newtask, or type /help for additional information.";
    }

    public static String getStartMessage() {
        return "Hello! " + getDefaultMessage();
    }
}
