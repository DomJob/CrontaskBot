package bot.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CommandTest {

    @Test
    public void parseCommand() {
        Command command = Command.parse("/task");

        assertEquals(Command.TASK, command);
    }

    @Test
    public void parseCommand_withParameters() {
        String parameter = "anything";

        Command command = Command.parse("/task " + parameter);

        assertEquals(Command.TASK, command);

        assertEquals(parameter, command.getParameters().get(1));
    }

    @Test
    public void getMessageCommand() {
        String commandText = "/task";
        Command command = Command.parse(commandText);

        assertEquals(commandText, command.toString());
    }

    @Test
    public void parseUnknownCommand() {
        String commandText = "/abcdawere";

        Command command = Command.parse(commandText);

        assertEquals(Command.UNKNOWN, command);
    }

    @Test
    public void parseNotACommand() {
        String commandText = "not a command";

        Command command = Command.parse(commandText);

        assertEquals(Command.NOT_A_COMMAND, command);
    }
}