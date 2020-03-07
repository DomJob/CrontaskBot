package application.command;

public class NotACommandException extends RuntimeException {
    public NotACommandException(String message) {
        super(String.format("Message %s does not contain a command.", message));
    }
}
