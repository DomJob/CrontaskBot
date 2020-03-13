package bot.states;

import static java.lang.Integer.min;

import bot.entities.ReceivedMessage;
import domain.task.Task;
import java.util.List;

public class TasksListedState implements BotState {
    private static final int TASKS_PER_PAGE = 10;

    private BotContext context;
    private List<Task> tasks;
    private int offset;
    private int nbTasks;

    public TasksListedState(BotContext context) {
        this.context = context;
        tasks = context.getTasks();
        nbTasks = tasks.size();
        offset = 0;

        sendPage();
    }

    @Override
    public BotState handleMessage(ReceivedMessage message, BotContext context) {
        switch (message.getCommand()) {
            case CANCEL:
                context.sendOperationCancelledMessage();
                return new DefaultState();
            case NEXT:
                nextPage();
                return this;
            case PREVIOUS:
                previousPage();
                return this;
            case DELETE:
                // TODO
                return new DefaultState();
            default:
                context.sendInvalidCommand();
                return this;
        }
    }

    private void previousPage() {
        if (offset - TASKS_PER_PAGE < 0) {
            context.sendInvalidCommand();
            return;
        }

        offset -= TASKS_PER_PAGE;
        sendPage();
    }

    private void nextPage() {
        if (offset + TASKS_PER_PAGE >= nbTasks) {
            context.sendInvalidCommand();
            return;
        }

        offset += TASKS_PER_PAGE;
        sendPage();
    }

    private void sendPage() {
        int from = offset;
        int to = min(offset + TASKS_PER_PAGE, nbTasks);

        List<Task> subList = tasks.subList(from, to);

        context.sendListOfTasksMessage(subList, from, to, nbTasks);
    }
}
