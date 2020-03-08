import application.CrontaskBot;
import domain.TaskFactory;
import domain.TaskRepository;
import application.EventHandler;
import infrastructure.persistence.inmemory.TaskRepositoryInMemory;
import application.TaskExecutor;
import infrastructure.telegram.HttpWrapper;
import infrastructure.telegram.JsonWrapper;
import infrastructure.telegram.TelegramHttpApi;
import infrastructure.util.RandomLongGenerator;
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
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

        // Event Handler schedule
        EventHandler handler = new EventHandler(api, bot);
        executor.scheduleAtFixedRate(handler, 1, 1, TimeUnit.SECONDS);

        // Task Executer
        TaskExecutor taskExecutor = new TaskExecutor(api, taskRepository);
        executor.scheduleAtFixedRate(taskExecutor, 0, 1, TimeUnit.MINUTES);
    }
}
