package bot.message;

import domain.task.Task;
import domain.time.Time;
import domain.time.Timezone;
import java.util.List;

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

    String formatTaskListingMessage(List<Task> tasks, int from, int to, int nbTasks);
}
