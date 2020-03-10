package infrastructure.util;

public class RandomLongGeneratorSpy extends RandomLongGenerator {
    private long lastIdGenerated;

    @Override
    public long generate() {
        lastIdGenerated = super.generate();
        return lastIdGenerated;
    }

    public long getLastIdGenerated() {
        return lastIdGenerated;
    }
}