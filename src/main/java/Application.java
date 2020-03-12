import bot.CrontaskBot;
import bot.TelegramApi;
import display.ConcreteMessageFactoryProvider;
import domain.task.TaskFactory;
import domain.task.TaskRepository;
import domain.user.UserRepository;
import infrastructure.Scheduler;
import infrastructure.persistence.TaskRepositorySQL;
import infrastructure.persistence.UserRepositorySQL;
import infrastructure.telegram.HttpWrapper;
import infrastructure.telegram.JsonWrapper;
import infrastructure.telegram.TelegramHttpApi;
import infrastructure.util.RandomLongGenerator;
import service.TaskService;
import service.UserService;

public class Application {
    public static void main(String[] args) {
        new UserRepositorySQL().findById(42069);
        new UserRepositorySQL().findById(42069);
    }

    public static void realMain(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Argument must be bot's token.");
        }
        String token = args[0];

        TaskRepository taskRepository = new TaskRepositorySQL();
        UserRepository userRepository = new UserRepositorySQL();

        TaskFactory taskFactory = new TaskFactory(new RandomLongGenerator());

        UserService userService = new UserService(userRepository);
        TaskService taskService = new TaskService(taskFactory, taskRepository);

        TelegramApi api = new TelegramHttpApi(token, new HttpWrapper(), new JsonWrapper());
        CrontaskBot bot = new CrontaskBot(api, taskService, userService, new ConcreteMessageFactoryProvider());

        new Scheduler(bot, api, taskService).start();
    }
}
