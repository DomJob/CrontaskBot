package infrastructure.telegram;

import static org.junit.Assert.*;

import bot.entities.Button;
import bot.entities.Update;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class JsonWrapperTest {
    private JsonWrapper jsonWrapper = new JsonWrapper();

    @Test
    public void formatMessageRequest_withKeyboard() {
        List<Button> buttons = Arrays.asList(new Button("Button 1", "data1"), new Button("Button 2", "data2"));

        String req = jsonWrapper.formatMessageRequest(12345, "hello world", buttons);

        assertEquals("{\"parse_mode\":\"HTML\",\"disable_web_page_preview\":true,\"text\":\"hello world\",\"reply_markup\":\"{\\\"inline_keyboard\\\":[[{\\\"text\\\":\\\"Button 1\\\",\\\"callback_data\\\":\\\"data1\\\"},{\\\"text\\\":\\\"Button 2\\\",\\\"callback_data\\\":\\\"data2\\\"}]]}\",\"chat_id\":12345}", req);
    }

    @Test
    public void formatMessageRequest() {
        String req = jsonWrapper.formatMessageRequest(12345, "hello world", null);

        assertEquals("{\"parse_mode\":\"HTML\",\"disable_web_page_preview\":true,\"text\":\"hello world\",\"chat_id\":12345}", req);
    }

    @Test
    public void formatCallbackQueryRequest() {
        String req = jsonWrapper.formatAnswerCallbackQueryRequest("callbackid", "hello");

        assertEquals("{\"callback_query_id\":\"callbackid\",\"text\":\"hello\"}", req);
    }

    @Test
    public void formatGetUpdatesRequest() {
        String req = jsonWrapper.formatGetUpdatesRequest(123);

        assertEquals("{\"offset\":123}", req);
    }

    @Test
    public void deserializeUpdates() {
        String updatesJson = "{\"ok\":true,\"result\":[{\"update_id\":751621864,\n" +
            "\"message\":{\"message_id\":1,\"from\":{\"id\":123456789,\"is_bot\":false,\"first_name\":\"anyone\",\"last_name\":\"\\u2602\\ufe0f\",\"username\":\"username\"}," +
            "\"chat\":{\"id\":12345678,\"first_name\":\"anyone\",\"last_name\":\"\\u2602\\ufe0f\",\"username\":\"username\",\"type\":\"private\"}," +
            "\"date\":1583571293," +
            "\"text\":\"/start\",\"entities\":[{\"offset\":0,\"length\":6,\"type\":\"bot_command\"}]}}]}";

        List<Update> updates = jsonWrapper.deSerializeUpdates(updatesJson);

        assertEquals(1, updates.size());
        assertEquals(123456789, updates.get(0).message.userId);
        assertEquals("/start", updates.get(0).message.text);

    }
}