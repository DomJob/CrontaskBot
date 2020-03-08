import application.CrontaskBot;
import application.EventHandler;
import application.Message;
import application.MessageFactory;
import application.TaskExecutor;
import domain.TaskFactory;
import domain.TaskRepository;
import domain.Time;
import infrastructure.persistence.inmemory.TaskRepositoryInMemory;
import infrastructure.telegram.HttpWrapper;
import infrastructure.telegram.JsonWrapper;
import infrastructure.telegram.TelegramHttpApi;
import infrastructure.util.RandomLongGenerator;
import java.time.Instant;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import ui.EnglishMessageFactory;

public class Application {
    public static void main(String[] args) {
        if(args.length != 1) {
            throw new IllegalArgumentException("Argument must be bot's token.");
        }
        String token = args[0];

        TaskRepository taskRepository = new TaskRepositoryInMemory();
        TelegramHttpApi api = new TelegramHttpApi(token, new HttpWrapper(), new JsonWrapper());
        MessageFactory messageFactory = new EnglishMessageFactory();

        CrontaskBot bot = new CrontaskBot(api, taskRepository, new TaskFactory(new RandomLongGenerator()), messageFactory);

        startSchedules(bot);
    }

    private static void startSchedules(CrontaskBot bot) {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        executor.scheduleAtFixedRate(bot::handleEvents, 0, 1, TimeUnit.SECONDS);

        waitUntilStartOfMinute();
        executor.scheduleAtFixedRate(() -> bot.checkTasks(Time.now()), 0, 1, TimeUnit.MINUTES);
    }

    private static void waitUntilStartOfMinute() {
        while(Instant.now().getEpochSecond() % 60 != 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
