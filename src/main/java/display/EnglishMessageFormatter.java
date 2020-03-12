package display;

import static display.FormattingUtils.sanitize;

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
        return "Operation cancelled."; // TODO
    }

    @Override
    public String formatHelpMessage() {
        return "CrontaskBot lets you set up reminders.\n\n"
            + "/task — Create a new task\n"
            + "/timezone — Change your timezone\n"
            + "/cancel — Cancel the ongoing operation\n"
            + "/help — Display this help message\n"
            + "\n"
            + "You can set up tasks to remind you periodically following the unix cron format. Use <a href=\"https://crontab.guru/\">this website</a> for more information on the cron syntax.\n\n"
            + "You can also set up a one time reminder by giving a date or a time, e.g. 2020-03-25 16:05, 16:05 or just 2020-03-25.\n\n"
            + "Additionally, you can type \"in 5 minutes\" or \"in 3 days and 5 hours\" to set up a reminder for the future without the exact time.\n"
            + "\n"
            + "This bot checks tasks every minute, and as a result, alerts can be off by up to 30 seconds.";
    }

    @Override
    public String formatTaskNameRequestMessage() {
        return "Enter the name of the task.";
    }

    @Override
    public String formatInvalidScheduleFormat() {
        return "Invalid format, please try again. Or use /help for more information about schedule format"; // TODO
    }

    @Override
    public String formatTaskCreatedMessage() {
        return "Task successfully created!";
    }

    @Override
    public String formatScheduleRequestedMessage() {
        return "Enter a schedule for this task. It can follow the cron syntax, or it can be an exact date/time or an amount of time from now."; // TODO
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
}
