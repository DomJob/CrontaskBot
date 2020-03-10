import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bot.CrontaskBot;
import bot.TelegramApi;
import bot.entities.CallbackQuery;
import bot.entities.ReceivedMessage;
import bot.message.Message;
import bot.message.MessageFactoryProvider;
import display.FakeMessageFactory;
import domain.task.Task;
import domain.task.TaskFactory;
import domain.time.Time;
import domain.user.Language;
import domain.user.User;
import infrastructure.persistence.Sqlite;
import infrastructure.persistence.TaskRepositorySQL;
import infrastructure.persistence.UserRepositorySQL;
import infrastructure.util.RandomLongGeneratorSpy;
import java.sql.SQLException;
import org.junit.After;
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
    private static final String INVALID_SCHEDULE = "this aint a schedule";
    private static final String AT_3_PM_EVERYDAY = "0 15 * * *";
    private static final String IN_5_MINUTES = "in 5 minutes";

    private static final Time CURRENT_TIME = Time.fromDate(2020, 3, 8, 1, 44);

    private static final String CALLBACK_QUERY_ID = "8888845646";
    private static final int MESSAGE_ID = 456;

    private static final long USER_ID = 12345678L;
    private static final long OTHER_USER_ID = 789456L;

    private static final Message TASK_CREATED_MESSAGE = new Message("task created yay");

    private static User USER = new User(USER_ID);

    @Mock
    private TelegramApi api;
    @Mock
    private MessageFactoryProvider messageFactoryProvider;
    @Spy
    private FakeMessageFactory messageFactory = new FakeMessageFactory();
    @Spy
    private TaskRepositorySQL taskRepository = new TaskRepositorySQL();
    @Spy
    private UserRepositorySQL userRepository = new UserRepositorySQL();

    private RandomLongGeneratorSpy longGenerator = new RandomLongGeneratorSpy();

    private CrontaskBot bot;

    @Before
    public void setUp() {
        Sqlite.setPath("crontaskbot_IT.db");

        bot = new CrontaskBot(api, taskRepository, userRepository, new TaskFactory(longGenerator), messageFactoryProvider);

        when(messageFactoryProvider.provide(any(Language.class))).thenReturn(messageFactory);

        TASK_CREATED_MESSAGE.setReceiver(USER);
        when(messageFactory.createTaskTriggeredMessage(any(Task.class))).thenReturn(TASK_CREATED_MESSAGE);
    }

    @After
    public void tearDown() throws SQLException {
        Sqlite.getConnection().createStatement().executeUpdate("DELETE FROM task");
        Sqlite.getConnection().createStatement().executeUpdate("DELETE FROM user");
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

        bot.checkTasks(CURRENT_TIME);

        assertThatATaskWasTriggered();
    }

    @Test
    public void createTask_triggers_dayOfWeek() {
        createTask("0 0 * * 3"); // Every wednesday at midnight

        bot.checkTasks(Time.fromDate(2020, 3, 11, 0, 0)); // Wednesday
        bot.checkTasks(Time.fromDate(2020, 3, 18, 0, 0)); // Wednesday
        bot.checkTasks(Time.fromDate(2020, 3, 25, 0, 0)); // Wednesday
        bot.checkTasks(Time.fromDate(2020, 3, 25, 0, 1)); // Wednesday wrong minute
        bot.checkTasks(Time.fromDate(2020, 3, 5, 0, 0)); // Tuesday

        assertThatATaskWasTriggered(3);
    }

    @Test
    public void createTask_triggers_dayOfMonth() {
        createTask("30 5 7 * *"); // Every 7th of the month at 5:30 am

        bot.checkTasks(Time.fromDate(2020, 3, 7, 5, 30)); // OK
        bot.checkTasks(Time.fromDate(2020, 4, 7, 5, 30)); // OK
        bot.checkTasks(Time.fromDate(2020, 7, 7, 5, 30)); // OK
        bot.checkTasks(Time.fromDate(2020, 7, 7, 5, 29)); // NO
        bot.checkTasks(Time.fromDate(2020, 7, 7, 5, 31)); // NO
        bot.checkTasks(Time.fromDate(2020, 7, 9, 5, 30)); // NO

        assertThatATaskWasTriggered(3);
    }

    @Test
    public void createTask_triggers_complex() {
        createTask("0 */2 * 9 2"); // Every even hour on Tuesdays in September

        bot.checkTasks(Time.fromDate(2020, 9, 8, 5, 0)); // NO
        bot.checkTasks(Time.fromDate(2020, 9, 8, 8, 0)); // OK
        bot.checkTasks(Time.fromDate(2020, 9, 9, 8, 0)); // NO
        bot.checkTasks(Time.fromDate(2020, 9, 15, 16, 0)); // OK
        bot.checkTasks(Time.fromDate(2020, 9, 22, 6, 0)); // OK
        bot.checkTasks(Time.fromDate(2020, 9, 22, 6, 1)); // NO

        assertThatATaskWasTriggered(3);
    }

    @Test
    public void createTask_cronEvery5minutes_isTriggeredManyTimes() {
        createTask(SCHEDULE_EVERY_5_MINUTES);

        for (int i = 0; i < 60; i++) {
            bot.checkTasks(CURRENT_TIME.plusMinutes(i));
        }

        assertThatATaskWasTriggered(12);
    }

    @Test
    public void createTask_relativeTime_canBeTriggered() {
        createTask(IN_5_MINUTES);

        bot.checkTasks(CURRENT_TIME.plusMinutes(4));
        bot.checkTasks(CURRENT_TIME.plusMinutes(5));
        bot.checkTasks(CURRENT_TIME.plusMinutes(6));

        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void createTask_exactTime_canBeTriggered() {
        createTask("2020-03-15 15:20");

        bot.checkTasks(Time.fromDate(2020, 3, 15, 15, 19));
        bot.checkTasks(Time.fromDate(2020, 3, 15, 15, 20));
        bot.checkTasks(Time.fromDate(2020, 3, 15, 15, 21));

        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void createTask_exactTimeWithoutYear_currentYearIsUsed() {
        int year = CURRENT_TIME.year();

        createTask("03-15 15:20");

        bot.checkTasks(Time.fromDate(year, 3, 15, 15, 19));
        bot.checkTasks(Time.fromDate(year, 3, 15, 15, 20));
        bot.checkTasks(Time.fromDate(year, 3, 15, 15, 21));

        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void createTask_exactTimeWithoutYearMonthDay_currentValuesAreUsed() {
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

        bot.checkTasks(CURRENT_TIME.plusMinutes(15));

        assertThatATaskWasTriggered();
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
        setTimezone("-5:00");

        createTask("2020-03-15 15:20");

        Time timeUTC = Time.fromDate(2020, 3, 15, 15, 20);

        // Doesnt trigger at UTC time
        bot.checkTasks(timeUTC);
        verify(messageFactory, never()).createTaskTriggeredMessage(any(Task.class));

        // But it does trigger 5 hours later
        bot.checkTasks(timeUTC.plusHours(5));
        verify(messageFactory, times(1)).createTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void setTimezone_relativeTask_unaffected() {
        setTimezone("-4:00");

        createTask(IN_5_MINUTES);

        // Trigger 5 mins from now regardless of timezone
        bot.checkTasks(CURRENT_TIME.plusMinutes(5));

        assertThatATaskWasTriggered();
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

    private void snoozeTask() {
        long taskId = longGenerator.getLastIdGenerated();

        CallbackQuery query = new CallbackQuery(CALLBACK_QUERY_ID, USER_ID, MESSAGE_ID, "snooze " + Long.toString(taskId));
        query.time = CURRENT_TIME;
        bot.handleCallbackQuery(query);
    }

    private void dismissTask() {
        long taskId = longGenerator.getLastIdGenerated();
        bot.handleCallbackQuery(new CallbackQuery(CALLBACK_QUERY_ID, USER_ID, MESSAGE_ID, "dismiss " + Long.toString(taskId)));
    }

    private void assertThatATaskWasTriggered() {
        verify(messageFactory).createTaskTriggeredMessage(any(Task.class));
        verify(api).sendMessage(TASK_CREATED_MESSAGE);
    }


    private void assertThatATaskWasTriggered(int howMany) {
        verify(messageFactory, times(howMany)).createTaskTriggeredMessage(any(Task.class));
        verify(api, times(howMany)).sendMessage(TASK_CREATED_MESSAGE);
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