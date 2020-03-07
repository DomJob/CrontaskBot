package application.states;

import static org.mockito.Mockito.verify;

import application.BotStateTest;
import application.command.Command;
import configuration.Messages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultStateTest extends BotStateTest {
    private static final long SENDER_ID = 12345L;

    @Before
    public void setUp() {
        state = new DefaultState();
    }

    @Test
    public void notACommand_thenDefaultMessage() {
        handleMessage("hello");

        verify(bot).sendMessage(SENDER_ID, Messages.defaultMessage());
    }

    @Test
    public void startCommand_thenStartMessage() {
        handleMessage(Command.START);

        verify(bot).sendMessage(SENDER_ID, Messages.startMessage());
    }

    @Test
    public void helpCommand_thenHelpMessage() {
        handleMessage(Command.HELP);

        verify(bot).sendMessage(SENDER_ID, Messages.helpMessage());
    }

    @Test
    public void newTaskCommand_thenNameRequestedMessage() {
        handleMessage(Command.NEWTASK);

        verify(bot).sendMessage(SENDER_ID, Messages.nameRequestedMessage());
    }

    @Test
    public void unknownCommand_thenDefaultMessage() {
        handleMessage("/whatever");

        verify(bot).sendMessage(SENDER_ID, Messages.unknownCommand());
    }
}