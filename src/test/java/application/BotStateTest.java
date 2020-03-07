package application;

import application.command.Command;
import application.entities.Message;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

public class BotStateTest {
    protected static final long SENDER_ID = 12345L;

    @Mock
    protected CrontaskBot bot;

    protected BotState state;

    protected BotState handleMessage(String text) {
        return state.handleMessage(new Message(SENDER_ID, text), bot);
    }

    protected BotState handleMessage(Command command) {
        return state.handleMessage(new Message(SENDER_ID, command.toString()), bot);
    }
}