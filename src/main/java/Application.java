import bot.CrontaskBot;
import bot.TelegramApi;
import display.ConcreteMessageFormatterProvider;
import domain.schedule.Schedule;
import domain.task.TaskFactory;
import domain.time.Time;
import domain.time.Timezone;
import domain.user.User;
import domain.user.UserId;
import infrastructure.Scheduler;
import infrastructure.persistence.SQLRepository;
import infrastructure.telegram.HttpWrapper;
import infrastructure.telegram.JsonWrapper;
import infrastructure.telegram.TelegramHttpApi;
import infrastructure.util.RandomLongGenerator;
import service.TaskService;
import service.UserService;

public class Application {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Argument must be bot's token.");
        }
        String token = args[0];

        SQLRepository repo = new SQLRepository();

        TaskFactory taskFactory = new TaskFactory(new RandomLongGenerator());

        UserService userService = new UserService(repo);
        TaskService taskService = new TaskService(taskFactory, repo);

        TelegramApi api = new TelegramHttpApi(token, new HttpWrapper(), new JsonWrapper());
        CrontaskBot bot = new CrontaskBot(api, taskService, userService, new ConcreteMessageFormatterProvider());

        Scheduler scheduler = new Scheduler(bot, api, taskService);

        scheduler.start();
    }
}
