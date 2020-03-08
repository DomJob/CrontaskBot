package application;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import application.command.Command;
import application.entities.ReceivedMessage;
import domain.Schedule;
import domain.Task;
import domain.TaskFactory;
import domain.TaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import ui.EnglishMessageFactory;

@RunWith(MockitoJUnitRunner.class)
public class CrontaskBotTest {
    public static final long SENDER_ID = 123465L;
    public static final String TASK_NAME = "task name";

    @Mock
    private TelegramApi api;
    @Mock
    private TaskRepository repository;
    @Mock
    private TaskFactory taskFactory;
    @Mock
    private Task task;
    @Mock
    private Schedule schedule;
    @Spy
    private EnglishMessageFactory messageFactory;

    @InjectMocks
    private CrontaskBot bot;

    @Test
    public void createTask_callsFactory() {
        bot.createTask(TASK_NAME, SENDER_ID, schedule);

        verify(taskFactory).create(TASK_NAME, SENDER_ID, schedule, false);
    }

    @Test
    public void createTask_savesToRepo() {
        when(taskFactory.create(TASK_NAME, SENDER_ID, schedule, false)).thenReturn(task);

        bot.createTask(TASK_NAME, SENDER_ID, schedule);

        verify(repository).save(task);
    }
}