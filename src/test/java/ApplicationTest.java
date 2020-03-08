import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import application.CrontaskBot;
import application.TelegramApi;
import domain.TaskFactory;
import domain.TaskRepository;
import infrastructure.persistence.inmemory.TaskRepositoryInMemory;
import infrastructure.util.RandomLongGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import ui.EnglishMessageFactory;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {
    @Mock
    private TelegramApi api;
    @Spy
    private EnglishMessageFactory messageFactory;

    private TaskRepository taskRepository;
    private CrontaskBot bot;
    private TaskFactory taskFactory = new TaskFactory(new RandomLongGenerator());;

    @Before
    public void setUp() {
        taskRepository = new TaskRepositoryInMemory();
        bot = new CrontaskBot(api, taskRepository, taskFactory, messageFactory);
    }

    @Test
    public void test() {

    }
}