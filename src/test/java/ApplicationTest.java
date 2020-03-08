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
import domain.User;
import domain.time.Time;
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
    private static final String TASK_NAME = "task name";
    private static final String SCHEDULE_EVERY_MINUTE = "* * * * *";
    private static final String SCHEDULE_NEVER = "65 * * * *";
    private static final String INVALIDE_SCHEDULE = "this aint a schedule";
    private static final Time ANY_TIME = Time.fromDate(2020, 3, 8, 1, 44);
    private static final long TASK_ID = 1234;
    private static final String CRON_SCHEDULE_EVERY_5_MINUTES = "*/5 * * * *";
    private static final String CALLBACK_QUERY_ID = "8888845646";
    private static final int MESSAGE_ID = 456;
    private static final String AT_3_PM_EVERYDAY = "0 15 * * *";
    private static final long USER_ID = 12345678L;
    private static final long OTHER_USER_ID = 789456L;
    private static User USER = new User(USER_ID);
    private static User OTHER_USER = new User(OTHER_USER_ID);

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
        message.setReceiver(USER);

        otherMessage = new Message("other text");
        otherMessage.setReceiver(OTHER_USER);
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
    public void createReminder_relativeTime_canBeTriggered() {
        sendMessage("/reminder");

        sendMessage(TASK_NAME);

        sendMessage("in 5 minutes");

        bot.checkTasks(Time.now().plusMinutes(4));
        bot.checkTasks(Time.now().plusMinutes(5));
        bot.checkTasks(Time.now().plusMinutes(6));

        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void createReminder_exactTime_canBeTriggered() {
        sendMessage("/reminder");

        sendMessage(TASK_NAME);

        sendMessage("2020-03-15 15:20");

        bot.checkTasks(Time.fromDate(2020,3,15,15,19));
        bot.checkTasks(Time.fromDate(2020,3,15,15,20));
        bot.checkTasks(Time.fromDate(2020,3,15,15,21));

        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
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

    @Test
    public void setTimezone_canTriggerTaskAtCorrectTimeInTimezone() {
        sendMessage("/timezone");
        sendMessage("-4:00");

        sendMessage("/task");
        sendMessage(TASK_NAME);
        sendMessage(AT_3_PM_EVERYDAY);

        Time threePmUTC = Time.fromDate(2020,3,15,15,0);

        // Doesnt trigger at 3 PM UTC
        bot.checkTasks(threePmUTC);
        verify(messageFactory, never()).createTaskTriggeredMessage(any(Task.class));

        // But it does trigger 4 hours later
        bot.checkTasks(threePmUTC.plusHours(4));
        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void setTimezone_canTriggerReminderAtCorrectTimeInTimezone() {
        sendMessage("/timezone");
        sendMessage("-4:00");

        sendMessage("/reminder");
        sendMessage(TASK_NAME);
        sendMessage("2020-03-15 15:20");

        Time timeUTC = Time.fromDate(2020,3,15,15,20);

        // Doesnt trigger at UTC time
        bot.checkTasks(timeUTC);
        verify(messageFactory, never()).createTaskTriggeredMessage(any(Task.class));

        // But it does trigger 4 hours later
        bot.checkTasks(timeUTC.plusHours(4));
        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void setTimezone_relativeReminders_unaffected() {
        sendMessage("/timezone");
        sendMessage("-4:00");

        sendMessage("/task");
        sendMessage(TASK_NAME);
        sendMessage("in 5 minutes");

        // Trigger 5 mins from now regardless of timezone
        bot.checkTasks(Time.now().plusMinutes(5));
        verify(messageFactory, never()).createTaskTriggeredMessage(any(Task.class));
    }

    private void sendMessage(String text) {
        bot.handleMessage(new ReceivedMessage(USER_ID, text));
    }

    private void sendMessageAsOtherUser(String text) {
        bot.handleMessage(new ReceivedMessage(OTHER_USER_ID, text));
    }
}