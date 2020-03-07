package application.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CommandTest {

    @Test
    public void parseCommand() {
        Command command = Command.parse("/newtask");

        assertEquals(Command.NEWTASK, command);
    }

    @Test
    public void parseCommand_withParameters() {
        String parameter = "anything";

        Command command = Command.parse("/newtask " + parameter);

        assertEquals(Command.NEWTASK, command);

        assertEquals(parameter, command.getParameters().get(1));
    }

    @Test
    public void getMessageCommand() {
        String commandText = "/newtask";
        Command command = Command.parse(commandText);

        assertEquals(commandText, command.toString());
    }

    @Test(expected = UnknownCommandException.class)
    public void parseUnknownCommand() {
        String commandText = "/abcdawere";

        Command command = Command.parse(commandText);
    }

    @Test(expected = NotACommandException.class)
    public void parseNotACommand() {
        String commandText = "not a command";

        Command command = Command.parse(commandText);
    }
}