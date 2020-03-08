import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import application.CrontaskBot;
import application.TelegramApi;
import application.entities.CallbackQuery;
import application.entities.ReceivedMessage;
import application.message.Message;
import domain.Task;
import domain.TaskFactory;
import domain.Time;
import domain.util.LongGenerator;
import infrastructure.persistence.inmemory.TaskRepositoryInMemory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import ui.EnglishMessageFactory;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {
    public static final String TASK_NAME = "task name";
    public static final String SCHEDULE_EVERY_MINUTE = "* * * * *";
    public static final String SCHEDULE_NEVER = "65 * * * *";
    public static final String INVALIDE_SCHEDULE = "this aint a schedule";
    public static final Time ANY_TIME = Time.fromDate(2020, 3, 8, 1, 44);
    public static final long TASK_ID = 1234;
    public static final String CRON_SCHEDULE_EVERY_5_MINUTES = "*/5 * * * *";
    public static final String CALLBACK_QUERY_ID = "8888845646";
    public static final int MESSAGE_ID = 456;
    private static long USER_ID = 12345678L;
    private static long OTHER_USER_ID = 789456L;

    @Mock
    private TelegramApi api;
    @Spy
    private EnglishMessageFactory messageFactory = new EnglishMessageFactory();
    @Spy
    private TaskRepositoryInMemory taskRepository = new TaskRepositoryInMemory();
    @Mock
    private LongGenerator longGenerator;

    private Message message;
    private Message otherMessage;

    private CrontaskBot bot;


    @Before
    public void setUp() {
        bot = new CrontaskBot(api, taskRepository, new TaskFactory(longGenerator), messageFactory);
        when(longGenerator.generate()).thenReturn(TASK_ID);
        message = new Message("any text");
        message.setReceiver(USER_ID);

        otherMessage = new Message("other text");
        otherMessage.setReceiver(OTHER_USER_ID);
    }

    @Test
    public void createTask_happyPath() {
        sendMessage("/task");

        sendMessage(TASK_NAME);

        sendMessage(SCHEDULE_EVERY_MINUTE);

        verify(messageFactory).createTaskCreatedMessage();
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void createTask_invalidSchedule() {
        sendMessage("/task");

        sendMessage(TASK_NAME);

        sendMessage(INVALIDE_SCHEDULE);

        verify(messageFactory).createInvalidCronFormatMessage();
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void createTask_taskCanBeTriggered() {
        sendMessage("/task");
        sendMessage(TASK_NAME);
        sendMessage(SCHEDULE_EVERY_MINUTE);

        Task task = taskRepository.findById(TASK_ID);
        when(messageFactory.createTaskTriggeredMessage(task)).thenReturn(message);

        bot.checkTasks(ANY_TIME);

        verify(messageFactory).createTaskTriggeredMessage(task);
        verify(api).sendMessage(message);
    }

    @Test
    public void createTask_cronEvery5minutes_isTriggeredManyTimes() {
        sendMessage("/task");
        sendMessage(TASK_NAME);
        sendMessage(CRON_SCHEDULE_EVERY_5_MINUTES);

        Task task = taskRepository.findById(TASK_ID);
        when(messageFactory.createTaskTriggeredMessage(task)).thenReturn(message);

        for(int i=0; i<60; i++) {
            bot.checkTasks(ANY_TIME.plusMinutes(i));
        }

        verify(messageFactory, times(12)).createTaskTriggeredMessage(task);
        verify(api, times(12)).sendMessage(message);
    }

    @Test
    public void createReminder_happyPath() {
        sendMessage("/reminder");

        sendMessage(TASK_NAME);

        sendMessage("in 5 minutes");

        verify(messageFactory).createReminderCreatedMessage();
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void createReminder_invalidSchedule() {
        sendMessage("/reminder");
        sendMessage(TASK_NAME);
        sendMessage(INVALIDE_SCHEDULE);

        verify(messageFactory).createInvalidReminderScheduleMessage();
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void createReminder_canBeTriggered() {
        sendMessage("/reminder");

        sendMessage(TASK_NAME);

        sendMessage("in 5 minutes");

        Task task = taskRepository.findById(TASK_ID);
        when(messageFactory.createTaskTriggeredMessage(task)).thenReturn(message);

        bot.checkTasks(Time.now().plusMinutes(5));

        verify(messageFactory).createTaskTriggeredMessage(task);
        verify(api).sendMessage(message);
    }

    @Test
    public void canBeUsedByTwoUsersAtSameTime() {
        sendMessage("/task");
        sendMessageAsOtherUser("/reminder");
        sendMessage(TASK_NAME);
        sendMessageAsOtherUser(TASK_NAME);
        sendMessage(SCHEDULE_EVERY_MINUTE);
        sendMessageAsOtherUser("in 5 minutes");

        verify(messageFactory).createTaskCreatedMessage();
        verify(messageFactory).createReminderCreatedMessage();
    }

    @Test
    public void dismissTask_deletesId() {
        sendMessage("/task");
        sendMessage(TASK_NAME);
        sendMessage(SCHEDULE_EVERY_MINUTE);

        bot.handleCallbackQuery(new CallbackQuery(CALLBACK_QUERY_ID, USER_ID, MESSAGE_ID, "dismiss " + TASK_ID));

        verify(api).deleteMessage(USER_ID, MESSAGE_ID);
    }

    @Test
    public void snoozeTask_deletesItAndRetriggersItLater() {
        sendMessage("/task");
        sendMessage(TASK_NAME);
        sendMessage(SCHEDULE_NEVER);

        bot.handleCallbackQuery(new CallbackQuery(CALLBACK_QUERY_ID, USER_ID, MESSAGE_ID, "snooze " + TASK_ID));

        verify(api).answerCallbackQuery(eq(CALLBACK_QUERY_ID), any(String.class));
        verify(api).deleteMessage(USER_ID, MESSAGE_ID);

        Task task = taskRepository.findById(TASK_ID);
        bot.checkTasks(Time.now().plusMinutes(15));

        verify(messageFactory).createTaskTriggeredMessage(task);
    }

    private void sendMessage(String text) {
        bot.handleMessage(new ReceivedMessage(USER_ID, text));
    }

    private void sendMessageAsOtherUser(String text) {
        bot.handleMessage(new ReceivedMessage(OTHER_USER_ID, text));
    }
}