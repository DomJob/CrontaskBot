package display;

import bot.message.MessageFormatter;
import bot.message.MessageFormatterProvider;
import domain.user.Language;
import java.util.HashMap;
import java.util.Map;

public class ConcreteMessageFormatterProvider implements MessageFormatterProvider {
    private static Map<Language, MessageFormatter> formatters = new HashMap<>();

    static {
        formatters.put(Language.ENGLISH, new EnglishMessageFormatter());
    }

    @Override
    public MessageFormatter provide(Language language) {
        return formatters.get(language);
    }
}