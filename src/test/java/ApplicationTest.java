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
import application.message.MessageFactoryProvider;
import display.EnglishMessageFactory;
import domain.task.Task;
import domain.task.TaskFactory;
import domain.time.Time;
import domain.user.Language;
import domain.user.User;
import domain.util.LongGenerator;
import infrastructure.persistence.inmemory.TaskRepositoryInMemory;
import infrastructure.persistence.inmemory.UserRepositoryInMemory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {
    private static final String TASK_NAME = "task name";
    private static final String SCHEDULE_EVERY_MINUTE = "* * * * *";
    private static final String SCHEDULE_EVERY_5_MINUTES = "*/5 * * * *";
    private static final String SCHEDULE_NEVER = "65 * * * *";
    private static final String INVALID_SCHEDULE = "this aint a schedule";
    private static final String AT_3_PM_EVERYDAY = "0 15 * * *";
    private static final String IN_5_MINUTES = "in 5 minutes";

    private static final Time CURRENT_TIME = Time.fromDate(2020, 3, 8, 1, 44);
    private static final Time ANY_TIME = Time.fromDate(2020, 3, 8, 1, 44);

    private static final long TASK_ID = 1234;
    private static final String CALLBACK_QUERY_ID = "8888845646";
    private static final int MESSAGE_ID = 456;

    private static final long USER_ID = 12345678L;
    private static final long OTHER_USER_ID = 789456L;

    private static User USER = new User(USER_ID);

    @Mock
    private TelegramApi api;
    @Mock
    private MessageFactoryProvider messageFactoryProvider;
    @Spy
    private EnglishMessageFactory messageFactory = new EnglishMessageFactory();
    @Spy
    private TaskRepositoryInMemory taskRepository = new TaskRepositoryInMemory();
    @Spy
    private UserRepositoryInMemory userRepository = new UserRepositoryInMemory();
    @Mock
    private LongGenerator longGenerator;

    private Message message;

    private CrontaskBot bot;

    @Before
    public void setUp() {
        bot = new CrontaskBot(api, taskRepository, userRepository, new TaskFactory(longGenerator), messageFactoryProvider);

        when(messageFactoryProvider.provide(any(Language.class))).thenReturn(messageFactory);
        when(longGenerator.generate()).thenReturn(TASK_ID);

        message = new Message("any text");
        message.setReceiver(USER);
    }

    @Test
    public void createTask_happyPath() {
        createTask(SCHEDULE_EVERY_MINUTE);

        verify(messageFactory).createTaskCreatedMessage();
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void createTask_invalidSchedule() {
        createTask(INVALID_SCHEDULE);

        verify(messageFactory).createInvalidScheduleFormat();
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void createTask_taskCanBeTriggered() {
        createTask(SCHEDULE_EVERY_MINUTE);

        Task task = taskRepository.findById(TASK_ID);
        when(messageFactory.createTaskTriggeredMessage(task)).thenReturn(message);

        bot.checkTasks(ANY_TIME);

        verify(messageFactory).createTaskTriggeredMessage(task);
        verify(api).sendMessage(message);
    }

    @Test
    public void createTask_cronEvery5minutes_isTriggeredManyTimes() {
        createTask(SCHEDULE_EVERY_5_MINUTES);

        Task task = taskRepository.findById(TASK_ID);
        when(messageFactory.createTaskTriggeredMessage(task)).thenReturn(message);

        for (int i = 0; i < 60; i++) {
            bot.checkTasks(ANY_TIME.plusMinutes(i));
        }

        verify(messageFactory, times(12)).createTaskTriggeredMessage(task);
        verify(api, times(12)).sendMessage(message);
    }

    @Test
    public void createReminder_relativeTime_canBeTriggered() {
        createTask(IN_5_MINUTES);

        bot.checkTasks(CURRENT_TIME.plusMinutes(4));
        bot.checkTasks(CURRENT_TIME.plusMinutes(5));
        bot.checkTasks(CURRENT_TIME.plusMinutes(6));

        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void createReminder_exactTime_canBeTriggered() {
        createTask("2020-03-15 15:20");

        bot.checkTasks(Time.fromDate(2020, 3, 15, 15, 19));
        bot.checkTasks(Time.fromDate(2020, 3, 15, 15, 20));
        bot.checkTasks(Time.fromDate(2020, 3, 15, 15, 21));

        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void createReminder_exactTimeWithoutYear_currentYearIsUsed() {
        int year = CURRENT_TIME.year();

        createTask("03-15 15:20");

        bot.checkTasks(Time.fromDate(year, 3, 15, 15, 19));
        bot.checkTasks(Time.fromDate(year, 3, 15, 15, 20));
        bot.checkTasks(Time.fromDate(year, 3, 15, 15, 21));

        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void createReminder_exactTimeWithoutYearMonthDay_currentValuesAreUsed() {
        int year = CURRENT_TIME.year();
        int month = CURRENT_TIME.month();
        int day = CURRENT_TIME.day();

        createTask("15:20");

        bot.checkTasks(Time.fromDate(year, month, day, 15, 19));
        bot.checkTasks(Time.fromDate(year, month, day, 15, 20));
        bot.checkTasks(Time.fromDate(year, month, day, 15, 21));

        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void canBeUsedByTwoUsersAtSameTime() {
        newMessage("/task")
            .send();

        newMessage("/task")
            .from(OTHER_USER_ID)
            .send();

        newMessage(TASK_NAME)
            .send();

        newMessage(TASK_NAME)
            .from(OTHER_USER_ID)
            .send();

        newMessage(IN_5_MINUTES)
            .from(OTHER_USER_ID)
            .send();

        newMessage(SCHEDULE_EVERY_MINUTE)
            .send();

        verify(messageFactory, times(2)).createTaskCreatedMessage();
    }

    @Test
    public void dismissTask_deletesMessage() {
        createTask(SCHEDULE_EVERY_MINUTE);

        dismissTask();

        verify(api).deleteMessage(USER_ID, MESSAGE_ID);
    }

    @Test
    public void snoozeTask_deletesItAndRetriggersItLater() {
        createTask(SCHEDULE_EVERY_MINUTE);

        snoozeTask();

        verify(api).answerCallbackQuery(eq(CALLBACK_QUERY_ID), any(String.class));
        verify(api).deleteMessage(USER_ID, MESSAGE_ID);

        Task task = taskRepository.findById(TASK_ID);
        bot.checkTasks(CURRENT_TIME.plusMinutes(15));

        verify(messageFactory).createTaskTriggeredMessage(task);
    }

    private void snoozeTask() {
        CallbackQuery query = new CallbackQuery(CALLBACK_QUERY_ID, USER_ID, MESSAGE_ID, "snooze " + TASK_ID);
        query.time = CURRENT_TIME;
        bot.handleCallbackQuery(query);
    }

    @Test
    public void setTimezone_triggersCronTask_correctTimeInTimezone() {
        setTimezone("-4:00");

        createTask(AT_3_PM_EVERYDAY);

        Time threePmUTC = Time.fromDate(2020, 3, 15, 15, 0);

        // Doesnt trigger at 3 PM UTC
        bot.checkTasks(threePmUTC);
        verify(messageFactory, never()).createTaskTriggeredMessage(any(Task.class));

        // But it does trigger 4 hours later
        bot.checkTasks(threePmUTC.plusHours(4));
        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void setTimezone_canTriggerReminderAtCorrectTimeInTimezone() {
        setTimezone("-4:00");

        createTask("2020-03-15 15:20");

        Time timeUTC = Time.fromDate(2020, 3, 15, 15, 20);

        // Doesnt trigger at UTC time
        bot.checkTasks(timeUTC);
        verify(messageFactory, never()).createTaskTriggeredMessage(any(Task.class));

        // But it does trigger 4 hours later
        bot.checkTasks(timeUTC.plusHours(4));
        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void setTimezone_relativeTask_unaffected() {
        setTimezone("-4:00");

        createTask(IN_5_MINUTES);

        // Trigger 5 mins from now regardless of timezone
        bot.checkTasks(CURRENT_TIME.plusMinutes(5));

        verify(messageFactory).createTaskTriggeredMessage(any(Task.class));
    }

    private void createTask(String schedule) {
        newMessage("/task")
            .send();

        newMessage(TASK_NAME)
            .send();

        newMessage(schedule)
            .send();
    }

    private void setTimezone(String offsetString) {
        newMessage("/timezone")
            .send();
        newMessage(offsetString)
            .send();
    }

    private void dismissTask() {
        bot.handleCallbackQuery(new CallbackQuery(CALLBACK_QUERY_ID, USER_ID, MESSAGE_ID, "dismiss " + TASK_ID));
    }

    private MessageBuilder newMessage(String text) {
        return new MessageBuilder(bot, text);
    }

    private static class MessageBuilder {
        private ReceivedMessage message = new ReceivedMessage(USER_ID, "");
        private CrontaskBot bot;

        MessageBuilder(CrontaskBot bot, String text) {
            this.bot = bot;
            message.text = text;
            message.time = CURRENT_TIME;
        }

        public MessageBuilder from(long userId) {
            message.userId = userId;
            return this;
        }

        public MessageBuilder at(Time time) {
            message.time = time;
            return this;
        }

        public void send() {
            bot.handleMessage(message);
        }
    }
}