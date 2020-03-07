import application.CrontaskBot;
import application.entities.Button;
import domain.TaskFactory;
import domain.TaskRepository;
import infrastructure.eventhandler.EventHandler;
import infrastructure.persistence.inmemory.TaskRepositoryInMemory;
import infrastructure.telegram.HttpWrapper;
import infrastructure.telegram.JsonWrapper;
import infrastructure.telegram.TelegramHttpApi;
import infrastructure.util.RandomLongGenerator;
import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Application {
    public static void main(String[] args) {
        if(args.length != 1) {
            throw new IllegalArgumentException("Argument must be bot's token.");
        }
        String token = args[0];

        TelegramHttpApi api = new TelegramHttpApi(token, new HttpWrapper(), new JsonWrapper());

        TaskRepository taskRepository = new TaskRepositoryInMemory();

        CrontaskBot bot = new CrontaskBot(api, taskRepository, new TaskFactory(new RandomLongGenerator()));
        EventHandler handler = new EventHandler(api, bot);
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

        // Event Handler schedule
        executor.scheduleAtFixedRate(handler, 1, 1, TimeUnit.SECONDS);

        // TODO - Task executor schedule
    }
}
