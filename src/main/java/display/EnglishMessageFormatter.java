package display;

import static display.FormattingUtils.sanitize;

import bot.message.MessageFormatter;
import bot.models.ListedTask;
import bot.models.TaskListing;
import domain.task.Task;
import domain.time.Time;
import domain.time.Timezone;
import domain.user.Language;

public class EnglishMessageFormatter implements MessageFormatter {
    @Override
    public String formatTaskTriggeredMessage(Task task) {
        return sanitize(task.getName());
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
        return "Unknown command. Use /help for a list of commands.";
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
            + "/task \u2014 Create a new task\n"
            + "/tasks \u2014 Manage your scheduled tasks\n"
            + "/timezone \u2014 Change your timezone settings\n"
            + "/language \u2014 Change the language\n"
            + "/help \u2014 Display this help message\n"
            + "/cancel \u2014 Cancel the ongoing operation\n"
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
        return String.format("Enter your timezone's offset from UTC in \u00b1HH:MM format.\n\nFor reference, it is currently <b>%d:%d</b> in UTC time.\n\nYour timezone is currently set to <b>%s</b>.", now.hour(), now.minute(), currentTimezone.toString());
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
        return "Invalid command.";
    }

    @Override
    public String formatTaskListingMessage(TaskListing listing) {
        StringBuilder message = new StringBuilder(String.format("Showing tasks <b>%d\u2014%d</b> of <b>%d</b>\n\n", listing.getStart() + 1, listing.getEnd(), listing.size()));

        for (ListedTask task : listing.getPage()) {
            String name = task.name;
            if (name.length() > 15) {
                name = name.substring(0, 15) + "...";
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

    @Override
    public String formatTaskDeletedMessage() {
        return "Task deleted.";
    }

    @Override
    public String formatInvalidCommandDuringListing() {
        return "Invalid command. Use /cancel to exit the listing.";
    }

    @Override
    public String formatInvalidDeleteCommand() {
        return "Invalid format \u2014 Please select a valid number in the list above.";
    }

    @Override
    public String formatLanguageInformationMessage() {
        StringBuilder sb = new StringBuilder("Use /language followed by the two-letter code desired language.\n\nCurrently, the following languages are supported:\n\n");

        for(Language langage : Language.values()) {
            sb.append(String.format(" \u2014 %s (<b>%s</b>)\n", langage.getDisplayName(), langage.getCode()));
        }

        return sb.toString();
    }

    @Override
    public String formatInvalidLangageMessage() {
        return "Invalid language. Use /language to see a list of the supported languages.";
    }

    @Override
    public String formatLanguageSetMessage() {
        return "Language successfully set!";
    }
}
