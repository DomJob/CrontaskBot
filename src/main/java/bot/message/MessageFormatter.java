package bot.message;

import bot.entities.TaskListing;
import domain.task.Task;
import domain.time.Time;
import domain.time.Timezone;

public interface MessageFormatter {
    String formatTaskTriggeredMessage(Task task);

    String formatDefaultMessage();

    String formatUnknownCommandMessage();

    String formatNoOngoingOperationMessage();

    String formatHelpMessage();

    String formatTaskNameRequestMessage();

    String formatStartMessage();

    String formatOperationCancelledMessage();

    String formatInvalidScheduleFormat();

    String formatTaskCreatedMessage();

    String formatScheduleRequestedMessage();

    String formatTimezoneOffsetRequestedMessage(Timezone currentTimezone, Time now);

    String formatTimezoneSetMessage();

    String formatInvalidTimezoneMessage();

    String formatInvalidCommand();

    String formatTaskListingMessage(TaskListing listing);

    String formatNoTasksMessage();
}
