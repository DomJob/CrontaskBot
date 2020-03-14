import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bot.CrontaskBot;
import bot.TelegramApi;
import bot.entities.CallbackQuery;
import bot.entities.ReceivedMessage;
import bot.message.MessageFormatter;
import bot.message.MessageFormatterProvider;
import domain.task.Task;
import domain.task.TaskFactory;
import domain.task.TaskId;
import domain.task.TaskRepository;
import domain.time.Time;
import domain.user.Language;
import domain.user.User;
import domain.user.UserId;
import domain.user.UserRepository;
import infrastructure.persistence.SQLRepository;
import infrastructure.persistence.Sqlite;
import infrastructure.persistence.TaskRepositoryInMemory;
import infrastructure.persistence.UserRepositoryInMemory;
import infrastructure.util.IncrementalLongGenerator;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import service.TaskService;
import service.UserService;

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
    private static final UserId USER_ID = new UserId(12345678L);
    private static final UserId OTHER_USER_ID = new UserId(789456L);
    private static boolean SQL = false;
    private static User USER = new User(USER_ID);

    private TaskRepository taskRepository = spy(new TaskRepositoryInMemory());
    private UserRepository userRepository = spy(new UserRepositoryInMemory());

    @Mock
    private TelegramApi api;
    @Mock
    private MessageFormatterProvider messageFormatterProvider;
    @Mock
    private MessageFormatter messageFormatter;
    @Spy
    private UserService userService = new UserService(userRepository);
    @Spy
    private TaskFactory taskFactory = new TaskFactory(new IncrementalLongGenerator());
    @Spy
    private TaskService taskService = new TaskService(taskFactory, taskRepository);

    private CrontaskBot bot;

    @Before
    public void setUp() {
        bot = new CrontaskBot(api, taskService, userService, messageFormatterProvider);

        when(messageFormatterProvider.provide(any(Language.class))).thenReturn(messageFormatter);
    }

    @After
    public void tearDown() throws SQLException {
        if (!SQL) {
            return;
        }
        Sqlite.getConnection().createStatement().executeUpdate("DELETE FROM task");
        Sqlite.getConnection().createStatement().executeUpdate("DELETE FROM user");
    }

    private void setUpSQL() {
        SQL = true;
        Sqlite.setPath("crontaskbot_IT.db");
        taskRepository = spy(new SQLRepository());
        userRepository = (SQLRepository) taskRepository;
    }

    @Test
    public void createTask_happyPath() {
        createTask(SCHEDULE_EVERY_MINUTE);

        verify(messageFormatter).formatTaskCreatedMessage();
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    public void createTask_invalidSchedule() {
        createTask(INVALID_SCHEDULE);

        verify(messageFormatter).formatInvalidScheduleFormat();
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void createTask_taskCanBeTriggered() {
        createTask(SCHEDULE_EVERY_MINUTE);

        checkTasksAt(CURRENT_TIME);

        assertThatATaskWasTriggered();
    }

    @Test
    public void createTask_triggers_dayOfWeek() {
        createTask("0 0 * * 3"); // Every wednesday at midnight

        checkTasksAt(Time.fromDate(2020, 3, 11, 0, 0)); // Wednesday
        checkTasksAt(Time.fromDate(2020, 3, 18, 0, 0)); // Wednesday
        checkTasksAt(Time.fromDate(2020, 3, 25, 0, 0)); // Wednesday
        checkTasksAt(Time.fromDate(2020, 3, 25, 0, 1)); // Wednesday wrong minute
        checkTasksAt(Time.fromDate(2020, 3, 5, 0, 0)); // Tuesday

        assertThatTasksWereTriggered(3);
    }

    @Test
    public void createTask_triggers_dayOfMonth() {
        createTask("30 5 7 * *"); // Every 7th of the month at 5:30 am

        checkTasksAt(Time.fromDate(2020, 3, 7, 5, 30)); // OK
        checkTasksAt(Time.fromDate(2020, 4, 7, 5, 30)); // OK
        checkTasksAt(Time.fromDate(2020, 7, 7, 5, 30)); // OK
        checkTasksAt(Time.fromDate(2020, 7, 7, 5, 29)); // NO
        checkTasksAt(Time.fromDate(2020, 7, 7, 5, 31)); // NO
        checkTasksAt(Time.fromDate(2020, 7, 9, 5, 30)); // NO

        assertThatTasksWereTriggered(3);
    }

    @Test
    public void createTask_triggers_complex() {
        createTask("0 */2 * 9 2"); // Every even hour on Tuesdays in September

        checkTasksAt(Time.fromDate(2020, 9, 8, 5, 0)); // NO
        checkTasksAt(Time.fromDate(2020, 9, 8, 8, 0)); // OK
        checkTasksAt(Time.fromDate(2020, 9, 9, 8, 0)); // NO
        checkTasksAt(Time.fromDate(2020, 9, 15, 16, 0)); // OK
        checkTasksAt(Time.fromDate(2020, 9, 22, 6, 0)); // OK
        checkTasksAt(Time.fromDate(2020, 9, 22, 6, 1)); // NO

        assertThatTasksWereTriggered(3);
    }

    @Test
    public void createTask_cronEvery5minutes_isTriggeredManyTimes() {
        createTask(SCHEDULE_EVERY_5_MINUTES);

        for (int i = 0; i < 60; i++) {
            checkTasksAt(CURRENT_TIME.plusMinutes(i));
        }

        assertThatTasksWereTriggered(12);
    }

    @Test
    public void createTask_relativeTime_canBeTriggered() {
        createTask(IN_5_MINUTES);

        checkTasksAt(CURRENT_TIME.plusMinutes(4));
        checkTasksAt(CURRENT_TIME.plusMinutes(5));
        checkTasksAt(CURRENT_TIME.plusMinutes(6));

        verify(messageFormatter, times(1)).formatTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void createTask_exactTime_canBeTriggered() {
        createTask("2020-03-15 15:20");

        checkTasksAt(Time.fromDate(2020, 3, 15, 15, 19));
        checkTasksAt(Time.fromDate(2020, 3, 15, 15, 20));
        checkTasksAt(Time.fromDate(2020, 3, 15, 15, 21));

        verify(messageFormatter, times(1)).formatTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void createTask_exactTimeWithoutYear_currentYearIsUsed() {
        int year = CURRENT_TIME.year();

        createTask("03-15 15:20");

        checkTasksAt(Time.fromDate(year, 3, 15, 15, 19));
        checkTasksAt(Time.fromDate(year, 3, 15, 15, 20));
        checkTasksAt(Time.fromDate(year, 3, 15, 15, 21));

        verify(messageFormatter, times(1)).formatTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void createTask_exactTimeWithoutYearMonthDay_currentValuesAreUsed() {
        int year = CURRENT_TIME.year();
        int month = CURRENT_TIME.month();
        int day = CURRENT_TIME.day();

        createTask("15:20");

        checkTasksAt(Time.fromDate(year, month, day, 15, 19));
        checkTasksAt(Time.fromDate(year, month, day, 15, 20));
        checkTasksAt(Time.fromDate(year, month, day, 15, 21));

        verify(messageFormatter, times(1)).formatTaskTriggeredMessage(any(Task.class));
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

        verify(messageFormatter, times(2)).formatTaskCreatedMessage();
    }

    @Test
    public void dismissTask_deletesMessage() {
        createTask(SCHEDULE_EVERY_MINUTE);

        dismissTask(1);

        verify(api).deleteMessage(USER_ID, MESSAGE_ID);
    }

    @Test
    public void snoozeTask_deletesItAndRetriggersItLater() {
        createTask(IN_5_MINUTES);

        snoozeTask(1);

        verify(api).answerCallbackQuery(eq(CALLBACK_QUERY_ID), any(String.class));
        verify(api).deleteMessage(USER_ID, MESSAGE_ID);

        checkTasksAt(CURRENT_TIME.plusMinutes(15));

        assertThatATaskWasTriggered();
    }

    @Test
    public void setTimezone_triggersCronTask_correctTimeInTimezone() {
        setTimezone("-4:00");

        createTask(AT_3_PM_EVERYDAY);

        Time threePmUTC = Time.fromDate(2020, 3, 15, 15, 0);

        // Doesnt trigger at 3 PM UTC
        checkTasksAt(threePmUTC);
        verify(messageFormatter, never()).formatTaskTriggeredMessage(any(Task.class));

        // But it does trigger 4 hours later
        checkTasksAt(threePmUTC.plusHours(4));
        verify(messageFormatter, times(1)).formatTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void setTimezone_canTriggerReminderAtCorrectTimeInTimezone() {
        setTimezone("-5:00");

        createTask("2020-03-15 15:20");

        Time timeUTC = Time.fromDate(2020, 3, 15, 15, 20);

        // Doesnt trigger at UTC time
        checkTasksAt(timeUTC);
        verify(messageFormatter, never()).formatTaskTriggeredMessage(any(Task.class));

        // But it does trigger 5 hours later
        checkTasksAt(timeUTC.plusHours(5));
        verify(messageFormatter, times(1)).formatTaskTriggeredMessage(any(Task.class));
    }

    @Test
    public void setTimezone_relativeTask_unaffected() {
        setTimezone("-4:00");

        createTask(IN_5_MINUTES);

        // Trigger 5 mins from now regardless of timezone
        checkTasksAt(CURRENT_TIME.plusMinutes(5));

        assertThatATaskWasTriggered();
    }

    @Test
    public void createTask_canDeleteIt() {
        createTask(IN_5_MINUTES);

        newMessage("/tasks")
            .send();

        newMessage("/delete 1")
            .send();

        verify(taskRepository).delete(new TaskId(1));
    }

    @Test
    public void deleteTask_itWontTrigger() {
        createTask(IN_5_MINUTES);

        newMessage("/tasks")
            .send();

        newMessage("/delete 1")
            .send();

        checkTasksAt(CURRENT_TIME.plusMinutes(5));

        assertThatNoTaskWasTriggered();
    }

    @Test
    public void deleteTask_noIndexGiven_sendsErrorMessage() {
        createTask(IN_5_MINUTES);

        newMessage("/tasks")
            .send();

        newMessage("/delete")
            .send();

        verify(messageFormatter).formatInvalidCommand();
    }

    @Test
    public void deleteTask_indexNotANumber_sendsErrorMessage() {
        createTask(IN_5_MINUTES);

        newMessage("/tasks")
            .send();

        newMessage("/delete asda2")
            .send();

        verify(messageFormatter).formatInvalidCommand();
    }

    @Test
    public void deleteTask_indexTooBig_sendsErrorMessage() {
        createTask(IN_5_MINUTES);

        newMessage("/tasks")
            .send();

        newMessage("/delete 2")
            .send();

        verify(messageFormatter).formatInvalidCommand();
    }

    private void checkTasksAt(Time time) {
        taskService.checkTasks(time, bot::notifyTaskTriggered);
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

    private void snoozeTask(long taskId) {
        CallbackQuery query = new CallbackQuery(CALLBACK_QUERY_ID, USER_ID, MESSAGE_ID, "snooze " + taskId);
        query.time = CURRENT_TIME;
        bot.handleCallbackQuery(query);
    }

    private void dismissTask(long taskId) {
        CallbackQuery query = new CallbackQuery(CALLBACK_QUERY_ID, USER_ID, MESSAGE_ID, "dismiss " + taskId);
        query.time = CURRENT_TIME;
        bot.handleCallbackQuery(query);
    }

    private void assertThatATaskWasTriggered() {
        assertThatTasksWereTriggered(1);
    }

    private void assertThatNoTaskWasTriggered() {
        assertThatTasksWereTriggered(0);
    }

    private void assertThatTasksWereTriggered(int howMany) {
        verify(messageFormatter, times(howMany)).formatTaskTriggeredMessage(any(Task.class));
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

        public MessageBuilder from(UserId userId) {
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