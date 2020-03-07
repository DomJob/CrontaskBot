package context;

import java.util.HashMap;
import java.util.Map;

public enum ServiceLocator {
    instance;

    private final Map<Class<?>, Object> instances = new HashMap<>();

    public <T> void register(Class<T> contract, T instance) {
        if (instances.containsKey(contract)) {
            throw new ServiceAlreadyRegisteredException(contract);
        }
        instances.put(contract, instance);
    }

    public <T> T resolve(Class<T> contract) {
        T instance = (T) instances.get(contract);
        if (instance == null) {
            throw new UnknownContractResolvedException(contract);
        }
        return instance;
    }
}
