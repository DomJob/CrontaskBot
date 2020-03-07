package application.command;

public class UnknownCommandException extends RuntimeException {
    public UnknownCommandException(String command) {
            super(String.format("Command %s is unknown.", command));
        }
}
