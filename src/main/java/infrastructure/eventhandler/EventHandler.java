package infrastructure.eventhandler;

import application.CrontaskBot;

public class EventHandler implements Runnable {
    private UpdateFetcher updateFetcher;
    private CrontaskBot bot;
    private long lastUpdate = 0;

    public EventHandler(UpdateFetcher updateFetcher, CrontaskBot bot) {
        this.updateFetcher = updateFetcher;
        this.bot = bot;
    }

    @Override
    public void run() {
        for(Update update : updateFetcher.getUpdates(lastUpdate)) {
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
            lastUpdate = update.id + 1;
        }
    }
}
