package context;

public class ServiceAlreadyRegisteredException extends RuntimeException {
    public <T> ServiceAlreadyRegisteredException(Class<T> contract) {
        super("A service implementation was already provided for " + contract.getSimpleName());
    }
}
