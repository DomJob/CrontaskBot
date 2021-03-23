package bot.states;

import bot.command.Command;
import bot.models.ReceivedMessage;
import bot.models.TaskListing;
import domain.task.Task;

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
                return delete(context, command);
            default:
                context.sendInvalidCommandDuringListing();
                return this;
        }
    }

    private BotState delete(BotContext context, Command command) {
        try {
            int index = Integer.parseInt(command.getParameter(1)) - 1;

            Task task = listing.getTask(index);

            context.deleteTask(task);

            context.sendDeletedTaskMessage();
            return new DefaultState();
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            context.sendInvalidDeleteCommand();
            return this;
        }
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
