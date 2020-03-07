package application;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import infrastructure.eventhandler.EventHandler;
import infrastructure.eventhandler.UpdateFetcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import application.entities.CallbackQuery;
import application.entities.Message;
import infrastructure.eventhandler.Update;

@RunWith(MockitoJUnitRunner.class)
public class EventHandlerTest {
    private static final long UPDATE_ID = 12345;
    private static final long NEXT_UPDATE_ID = UPDATE_ID + 1;
    private static final long MESSAGE_ID = 46677;
    private static final long SENDER_ID = 34567;
    private static final String CALLBACK_ID = "78944";
    private static final String MESSAGE_TEXT = "/command";
    private static final String CALLBACK_DATA = "task 1234";

    @Mock
    private CrontaskBot bot;
    @Mock
    private UpdateFetcher fetcher;

    private Update messageUpdate = new Update(UPDATE_ID, new Message(SENDER_ID, MESSAGE_TEXT));
    private Update callbackUpdate = new Update(NEXT_UPDATE_ID, new CallbackQuery(CALLBACK_ID, SENDER_ID, MESSAGE_ID, CALLBACK_DATA));

    private List<Update> updates;

    @InjectMocks
    private EventHandler handler;

    @Test
    public void whenRun_thenAllNewUpdatesAreFetched() {
        when(fetcher.getUpdates(any(Long.class))).thenReturn(new ArrayList<Update>());

        handler.run();

        verify(fetcher).getUpdates(0);
    }

    @Test
    public void whenRun_thenAllNewUpdatesAreHandled() {
        when(fetcher.getUpdates(any(Long.class))).thenReturn(Arrays.asList(messageUpdate, callbackUpdate));

        handler.run();

        verify(bot, times(1)).handleMessage(messageUpdate.message);
        verify(bot, times(1)).handleCallbackQuery(callbackUpdate.callbackQuery);
    }

    @Test
    public void whenRunTwice_thenUpdatesAreHandledBothTimes() {
        when(fetcher.getUpdates(any(Long.class))).thenReturn(Collections.singletonList(messageUpdate));
        handler.run();
        verify(bot, times(1)).handleMessage(messageUpdate.message);

        when(fetcher.getUpdates(any(Long.class))).thenReturn(Collections.singletonList(callbackUpdate));
        handler.run();
        verify(bot, times(1)).handleCallbackQuery(callbackUpdate.callbackQuery);
    }

    @Test
    public void whenRunTwice_thenOffsetIsUsed() {
        when(fetcher.getUpdates(any(Long.class))).thenReturn(Collections.singletonList(messageUpdate));

        handler.run();
        handler.run();

        verify(fetcher).getUpdates(UPDATE_ID+1);
    }


    @Test
    public void whenRun_messageUpdateIsHandled() {
        when(fetcher.getUpdates(any(Long.class))).thenReturn(Collections.singletonList(messageUpdate));

        handler.run();

        verify(bot).handleMessage(messageUpdate.message);
    }

    @Test
    public void whenRun_callBackQueryIsHandled() {
        when(fetcher.getUpdates(any(Long.class))).thenReturn(Collections.singletonList(callbackUpdate));

        handler.run();

        verify(bot).handleCallbackQuery(callbackUpdate.callbackQuery);
    }
}