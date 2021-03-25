package infrastructure.util;

import domain.util.LongGenerator;

import java.security.SecureRandom;

public class RandomLongGenerator implements LongGenerator {
    @Override
    public long generate() {
        return new SecureRandom().nextLong() << 1 >>> 1;
    }
}
