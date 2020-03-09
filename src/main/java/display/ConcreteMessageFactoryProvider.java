package display;

import bot.message.MessageFactory;
import domain.user.Language;
import java.util.HashMap;
import java.util.Map;

public class ConcreteMessageFactoryProvider implements bot.message.MessageFactoryProvider {
    private static Map<Language, MessageFactory> factories = new HashMap<>();

    static {
        factories.put(Language.ENGLISH, new EnglishMessageFactory());
    }

    @Override
    public MessageFactory provide(Language language) {
        return factories.get(language);
    }
}