package infrastructure;

import application.CrontaskBot;
import application.TelegramApi;
import application.entities.Update;
import domain.time.Time;
import java.time.Instant;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private CrontaskBot bot;
    private TelegramApi api;
    private long lastUpdate = 0;

    public Scheduler(CrontaskBot bot, TelegramApi api) {
        this.bot = bot;
        this.api = api;
    }

    public void start() {
        long timeUntilStartOfMinute = 60 - Instant.now().getEpochSecond() % 60;

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.scheduleAtFixedRate(this::handleEvents, 0, 1, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(this::checkTasks, timeUntilStartOfMinute, 60, TimeUnit.SECONDS);
    }

    public void checkTasks() {
        bot.checkTasks(Time.now());
    }

    public void handleEvents() {
        for(Update update : api.getUpdates(lastUpdate)) {
            switch(update.type) {
                case MESSAGE:
                    bot.handleMessage(update.message);
                    break;
                case CALLBACK:
                    bot.handleCallbackQuery(update.callbackQuery);
                    break;
            }
            lastUpdate = update.id + 1;
        }
    }
}
