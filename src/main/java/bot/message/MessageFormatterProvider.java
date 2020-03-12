package bot.message;

import domain.user.Language;

public interface MessageFormatterProvider {
    MessageFormatter provide(Language language);
}
