package display;

import bot.message.MessageFormatter;
import bot.message.MessageFormatterProvider;
import domain.user.Language;

import java.util.HashMap;
import java.util.Map;

public class ConcreteMessageFormatterProvider implements MessageFormatterProvider {
    private static final Map<Language, MessageFormatter> formatters = new HashMap<>();

    static {
        formatters.put(Language.ENGLISH, new EnglishMessageFormatter());
        formatters.put(Language.FRENCH, new FrenchMessageFormatter());
    }

    @Override
    public MessageFormatter provide(Language language) {
        if (language == null) {
            return formatters.get(Language.ENGLISH);
        }
        return formatters.get(language);
    }
}
