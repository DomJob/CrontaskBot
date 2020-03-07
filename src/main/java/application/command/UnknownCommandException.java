package application.command;

public class UnknownCommandException extends RuntimeException {
    public UnknownCommandException(String message) {
        super(String.format("Unknown command %s.", message));
    }
}
