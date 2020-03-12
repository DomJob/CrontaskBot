package infrastructure;

import bot.CrontaskBot;
import bot.TelegramApi;
import bot.entities.Update;
import domain.time.Time;
import java.time.Instant;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import service.TaskService;

public class Scheduler {
    private CrontaskBot bot;
    private TelegramApi api;
    private TaskService taskService;
    private long lastUpdate = 0;

    public Scheduler(CrontaskBot bot, TelegramApi api, TaskService taskService) {
        this.bot = bot;
        this.api = api;
        this.taskService = taskService;
    }

    public void start() {
        long timeUntilStartOfMinute = 60 - Instant.now().getEpochSecond() % 60;

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.scheduleAtFixedRate(this::handleEvents, 0, 1, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(this::checkTasks, timeUntilStartOfMinute, 60, TimeUnit.SECONDS);
    }

    public void checkTasks() {
        taskService.checkTasks(now(), bot::notifyTaskTriggered);
    }

    public void handleEvents() {
        for (Update update : api.getUpdates(lastUpdate)) {
            update.setTime(now());

            switch (update.type) {
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

    public Time now() {
        long secondsSinceEpoch = Instant.now().getEpochSecond();

        long minutes = Math.round((double) secondsSinceEpoch / 60);

        return new Time(minutes);
    }
}
