package bot.states;

import bot.command.Command;
import bot.entities.ReceivedMessage;
import bot.entities.TaskListing;
import domain.task.Task;
import domain.task.TaskId;
import java.util.List;

public class TasksListedState implements BotState {
    private TaskListing listing;

    public TasksListedState(TaskListing listing) {
        this.listing = listing;
    }

    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        Command command = message.getCommand();
        switch (command) {
            case CANCEL:
                context.sendOperationCancelledMessage();
                return new DefaultState();
            case NEXT:
                nextPage(context);
                return this;
            case PREVIOUS:
                previousPage(context);
                return this;
            case DELETE:
                try {
                    Task task = getSpecifiedTask(command);
                    context.deleteTask(task);
                    context.sendDeletedTaskMessage();
                    return new DefaultState();
                } catch (AssertionError e) {
                    context.sendInvalidCommand();
                    return this;
                }
            default:
                context.sendInvalidCommand();
                return this;
        }
    }

    private Task getSpecifiedTask(Command command) {
        List<String> parameters = command.getParameters();
        assert(parameters.size() == 2);

        String indexStr = parameters.get(1);
        assert(indexStr.matches("^\\d+$"));

        int index = Integer.parseInt(indexStr) - 1;
        assert(index >= 0 && index < listing.size());

        return listing.getTask(index);
    }

    private void previousPage(BotContext context) {
        try {
            listing.previousPage();
            context.sendListOfTasksMessage(listing);
        } catch (IllegalStateException e) {
            context.sendInvalidCommand();
        }
    }

    private void nextPage(BotContext context) {
        try {
            listing.nextPage();
            context.sendListOfTasksMessage(listing);
        } catch (IllegalStateException e) {
            context.sendInvalidCommand();
        }
    }
}
