package infrastructure;

import application.CrontaskBot;
import application.TelegramAPI;
import application.entities.CallbackQuery;
import telegram.Update;

public class EventHandler implements Runnable {
    private TelegramAPI api;
    private CrontaskBot bot;
    private long lastUpdate = 0;

    public EventHandler(TelegramAPI api, CrontaskBot bot) {
        this.api = api;
        this.bot = bot;
    }

    @Override
    public void run() {
        for(Update update : api.getUpdates(lastUpdate)) {
            switch(update.type) {
                case MESSAGE:
                    bot.handleMessage(update.message);
                    break;
                case CALLBACK:
                    bot.handleCallbackQuery(update.callbackQuery);
                    break;
                default:
                    throw new IllegalStateException();
            }
            lastUpdate = update.id;
        }
    }
}
