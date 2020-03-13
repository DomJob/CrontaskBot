package infrastructure.util;

import domain.util.LongGenerator;

public class IncrementalLongGenerator implements LongGenerator {
    private long id = 1;

    @Override
    public long generate() {
        return  id++;
    }
}