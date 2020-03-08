package application.message;

import domain.user.Language;

public interface MessageFactoryProvider {
    MessageFactory provide(Language language);
}
