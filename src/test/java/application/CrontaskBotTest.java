package application;

import static org.mockito.Mockito.verify;

import application.entities.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import configuration.MessageFormatter;

@RunWith(MockitoJUnitRunner.class)
public class CrontaskBotTest {
    public static final long SENDER_ID = 123465L;
    public static final long OTHER_SENDER_ID = 456789L;
    public static final String NEW_TASK_COMMAND = "/newtask";
    public static final String TASK_NAME = "task name";

    @Mock
    private TelegramAPI api;

    @InjectMocks
    private CrontaskBot bot;

    @Test
    public void whenReceiveNewTaskCommand_thenNameIsAsked() {
        bot.handleMessage(new Message(SENDER_ID, NEW_TASK_COMMAND));

        verify(api).sendMessage(SENDER_ID, MessageFormatter.getNameRequestMessage());
    }

    @Test
    public void whenReceiveHelpCommand_thenHelpIsGiven() {
        bot.handleMessage(new Message(SENDER_ID, NEW_TASK_COMMAND));

        verify(api).sendMessage(SENDER_ID, MessageFormatter.getHelpMessage());
    }

    @Test
    public void whenNewTaskNameIsGiven_thenScheduleIsAsked() {
        bot.handleMessage(new Message(SENDER_ID, NEW_TASK_COMMAND));

        bot.handleMessage(new Message(SENDER_ID, TASK_NAME));

        verify(api).sendMessage(SENDER_ID, MessageFormatter.getCronRequestMessage());
    }

    @Test
    public void whenAUserIsNamingATask_andOtherUserCreatesOne_thenUsersGetProperMessage() {
        bot.handleMessage(new Message(SENDER_ID, NEW_TASK_COMMAND));
        bot.handleMessage(new Message(SENDER_ID, TASK_NAME));

        bot.handleMessage(new Message(OTHER_SENDER_ID, NEW_TASK_COMMAND));


        verify(api).sendMessage(SENDER_ID, MessageFormatter.getCronRequestMessage());
        verify(api).sendMessage(OTHER_SENDER_ID, MessageFormatter.getNameRequestMessage());
    }
}