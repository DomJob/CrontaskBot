import application.CrontaskBot;
import application.TelegramApi;
import application.message.MessageFactory;
import domain.Task.TaskFactory;
import domain.Task.TaskRepository;
import domain.user.UserRepository;
import infrastructure.Scheduler;
import infrastructure.persistence.inmemory.TaskRepositoryInMemory;
import infrastructure.persistence.inmemory.UserRepositoryInMemory;
import infrastructure.telegram.HttpWrapper;
import infrastructure.telegram.JsonWrapper;
import infrastructure.telegram.TelegramHttpApi;
import infrastructure.util.RandomLongGenerator;
import ui.EnglishMessageFactory;
import ui.MessageFactoryProviderImpl;

public class Application {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("Argument must be bot's token.");
        }
        String token = args[0];

        TaskRepository taskRepository = new TaskRepositoryInMemory();
        UserRepository userRepository = new UserRepositoryInMemory();
        MessageFactory messageFactory = new EnglishMessageFactory();

        TelegramApi api = new TelegramHttpApi(token, new HttpWrapper(), new JsonWrapper());
        CrontaskBot bot = new CrontaskBot(api, taskRepository, userRepository, new TaskFactory(new RandomLongGenerator()), new MessageFactoryProviderImpl());

        new Scheduler(bot, api).start();
    }
}
