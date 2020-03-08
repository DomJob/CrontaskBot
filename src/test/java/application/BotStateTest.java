package application;

import application.command.Command;
import application.entities.ReceivedMessage;
import application.states.BotContext;
import org.mockito.Mock;

public class BotStateTest {
    protected static final long SENDER_ID = 12345L;

    @Mock
    protected BotContext context;

    protected BotState state;

    protected BotState handleMessage(String text) {
        return state.handleMessage(new ReceivedMessage(SENDER_ID, text), context);
    }

    protected BotState handleMessage(Command command) {
        return state.handleMessage(new ReceivedMessage(SENDER_ID, command.toString()), context);
    }
}