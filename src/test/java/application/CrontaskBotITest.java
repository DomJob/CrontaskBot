package application;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import application.command.Command;
import application.entities.Message;
import domain.Schedule;
import domain.Task;
import domain.TaskFactory;
import domain.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import configuration.Messages;

@RunWith(MockitoJUnitRunner.class)
public class CrontaskBotITest {
    public static final long SENDER_ID = 123465L;
    public static final long OTHER_SENDER_ID = 456789L;
    public static final String TASK_NAME = "task name";
    public static final String TASK_SCHEDULE = "0 0 * * *";
    public static final String INVALID_SCHEDULE = "what the heck is a cron?";
    public static final String NEW_TASK_COMMAND = Command.NEW_TASK.toString();

    @Mock
    private TelegramApi api;
    @Mock
    private TaskRepository repository;
    @Mock
    private TaskFactory factory;
    @Mock
    private Task task;
    @Mock
    private Schedule schedule;

    @InjectMocks
    private CrontaskBot bot;

    @Test
    public void whenAUserIsNamingATask_andOtherUserCreatesOne_thenUsersGetProperMessage() {
        bot.handleMessage(new Message(SENDER_ID, NEW_TASK_COMMAND));
        bot.handleMessage(new Message(SENDER_ID, TASK_NAME));

        bot.handleMessage(new Message(OTHER_SENDER_ID, NEW_TASK_COMMAND));

        verify(api).sendMessage(SENDER_ID, Messages.cronRequestedMessage());
        verify(api).sendMessage(OTHER_SENDER_ID, Messages.nameRequestedMessage());
    }

    @Test
    public void createTask_happyPath() {
        bot.handleMessage(new Message(SENDER_ID, NEW_TASK_COMMAND));
        bot.handleMessage(new Message(SENDER_ID, TASK_NAME));
        bot.handleMessage(new Message(SENDER_ID, TASK_SCHEDULE));

        verify(factory).create(eq(TASK_NAME), eq(SENDER_ID), any(Schedule.class), eq(false));
    }

    @Test
    public void createTask_invalidCron() {
        bot.handleMessage(new Message(SENDER_ID, NEW_TASK_COMMAND));
        bot.handleMessage(new Message(SENDER_ID, TASK_NAME));
        bot.handleMessage(new Message(SENDER_ID, INVALID_SCHEDULE));

        verify(api).sendMessage(SENDER_ID, Messages.invalidCronMessage());
    }
}