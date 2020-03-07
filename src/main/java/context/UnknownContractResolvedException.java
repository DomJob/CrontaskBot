package context;

public class UnknownContractResolvedException extends RuntimeException {
    public <T> UnknownContractResolvedException(Class<T> contract) {
        super("No implementation found for " + contract.getSimpleName());
    }
}
