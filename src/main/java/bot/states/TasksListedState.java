package bot.states;

import bot.entities.ReceivedMessage;
import bot.entities.TaskListing;

public class TasksListedState implements BotState {
    private TaskListing listing;

    public TasksListedState(TaskListing listing) {
        this.listing = listing;
    }

    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        switch (message.getCommand()) {
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
                // TODO
                return new DefaultState();
            default:
                context.sendInvalidCommand();
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
