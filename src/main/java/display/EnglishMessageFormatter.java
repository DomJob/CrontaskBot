package display;

import static display.FormattingUtils.sanitize;

import bot.entities.ListedTask;
import bot.entities.TaskListing;
import bot.message.MessageFormatter;
import domain.task.Task;
import domain.time.Time;
import domain.time.Timezone;

public class EnglishMessageFormatter implements MessageFormatter {
    @Override
    public String formatTaskTriggeredMessage(Task task) {
        return String.format("<b>Reminder:</b>\n\n%s", sanitize(task.getName()));
    }

    @Override
    public String formatDefaultMessage() {
        return "Use /help for information on how to use this bot.";
    }

    @Override
    public String formatStartMessage() {
        return "Welcome to CrontaskBot! Use /help for information on how to use this bot.";
    }

    @Override
    public String formatUnknownCommandMessage() {
        return "Unknown command. Use /help for a list of available commands.";
    }

    @Override
    public String formatNoOngoingOperationMessage() {
        return "There is no operation to cancel.";
    }

    @Override
    public String formatOperationCancelledMessage() {
        return "Operation cancelled.";
    }

    @Override
    public String formatHelpMessage() {
        return "CrontaskBot lets you set up reminders.\n\n"
            + "/task — Create a new task\n"
            + "/tasks — Manage your scheduled tasks\n"
            + "/timezone — Change your timezone\n"
            + "/help — Display this help message\n"
            + "/cancel — Cancel the ongoing operation\n"
            + "\n"
            + "You can set up tasks to remind you periodically following the unix cron format. Use <a href=\"https://crontab.guru/\">this website</a> for more information on the cron syntax.\n\n"
            + "You can also set up a one time reminder by giving a date or a time, e.g. <i>2020-03-25 16:05</i>, <i>16:05</i> or just <i>2020-03-25</i>.\n\n"
            + "Additionally, you can type \"in 5 minutes\" or \"in 3 days and 5 hours\" to set up a reminder for the future without the exact time.\n"
            + "\n"
            + "This bot checks tasks every minute, and as a result, alerts can be off by up to 30 seconds.\n"
            + "\n"
            + "Source code available <a href=\"https://github.com/DomJob/CrontaskBot/\">here</a>";
    }

    @Override
    public String formatTaskNameRequestMessage() {
        return "Enter the name of the task.";
    }

    @Override
    public String formatInvalidScheduleFormat() {
        return "Invalid format, please try again. Or use /help for more information about schedule format";
    }

    @Override
    public String formatTaskCreatedMessage() {
        return "Task successfully created!";
    }

    @Override
    public String formatScheduleRequestedMessage() {
        return "Enter a schedule for this task. It can follow the cron syntax, or it can be an exact date/time or an amount of time from now.";
    }

    @Override
    public String formatTimezoneOffsetRequestedMessage(Timezone currentTimezone, Time now) {
        return String.format("Enter your timezone's offset from UTC in ±HH:MM format.\n\nFor reference, it is currently <b>%d:%d</b> in UTC time and your timezone is currently set to <b>%s</b>.", now.hour(), now.minute(), currentTimezone.toString());
    }

    @Override
    public String formatTimezoneSetMessage() {
        return "Timezone successfully set!";
    }

    @Override
    public String formatInvalidTimezoneMessage() {
        return "Invalid timezone, please try again.";
    }

    @Override
    public String formatInvalidCommand() {
        return "Invalid command. Use /cancel to exit the listing.";
    }

    @Override
    public String formatTaskListingMessage(TaskListing listing) {
        StringBuilder message = new StringBuilder(String.format("Showing tasks <b>%d—%d</b> of <b>%d</b>\n\n", listing.getStart() + 1, listing.getEnd(), listing.size()));

        for (ListedTask task : listing.getPage()) {
            String name = task.name;
            if (name.length() > 10) {
                name = name.substring(10) + "...";
            }
            message.append(String.format("<b>%d</b>. %s\n", task.index, sanitize(name)));
            message.append(String.format("Scheduled for <b>%s</b>\n\n", task.scheduledFor));
        }

        boolean previous = listing.hasPreviousPage();
        boolean next = listing.hasNextPage();

        message.append("Use /delete followed by the number of the task you want to delete.\n");

        if (previous && next) {
            message.append("\nUse /previous or /next to see more tasks.");
        } else if (previous) {
            message.append("\nUse /previous to see previous tasks.");
        } else if (next) {
            message.append("\nUse /next to see more tasks.");
        }

        return message.toString();
    }

    @Override
    public String formatNoTasksMessage() {
        return "You don't have any tasks at the moment.";
    }
}
