package application.states;

import static org.mockito.Mockito.verify;

import application.command.Command;
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

        verify(context).sendDefaultMessage();
    }

    @Test
    public void startCommand_thenStartMessage() {
        handleMessage(Command.START);

        verify(context).sendStartMessage();
    }

    @Test
    public void helpCommand_thenHelpMessage() {
        handleMessage(Command.HELP);

        verify(context).sendHelpMessage();
    }

    @Test
    public void newTaskCommand_thenNameRequestedMessage() {
        handleMessage(Command.NEW_TASK);

        verify(context).sendTaskNameRequestMessage();
    }

    @Test
    public void unknownCommand_thenDefaultMessage() {
        handleMessage("/whatever");

        verify(context).sendUnknownCommandMessage();
    }
}