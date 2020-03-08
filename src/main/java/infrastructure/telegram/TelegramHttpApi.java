package infrastructure.telegram;

import application.Message;
import application.TelegramApi;
import application.entities.Button;
import com.fasterxml.jackson.databind.ObjectMapper;
import application.entities.Update;
import java.util.List;

public class TelegramHttpApi implements TelegramApi {
    private String token;
    private ObjectMapper objectMapper = new ObjectMapper();
    private HttpWrapper httpWrapper;
    private JsonWrapper jsonWrapper;

    public TelegramHttpApi(String token, HttpWrapper httpWrapper, JsonWrapper jsonWrapper) {
        this.token = token;
        this.httpWrapper = httpWrapper;
        this.jsonWrapper = jsonWrapper;
    }

    @Override
    public List<Update> getUpdates(long offset) {
        String url = formatUrl("getUpdates");
        String request = jsonWrapper.formatGetUpdatesRequest(offset);
        String updatesJson = httpWrapper.post(url, request);
        return jsonWrapper.deSerializeUpdates(updatesJson);
    }

    @Override
    public void sendMessage(Message message) {
        String url = formatUrl("sendMessage");
        String request = jsonWrapper.formatMessageRequest(message.getReceiver(), message.getText(), message.getButtons());
        httpWrapper.post(url, request);
    }

    @Override
    public void answerCallbackQuery(String id, String text) {
        String url = formatUrl("answerCallbackQuery");
        String request = jsonWrapper.formatAnswerCallbackQueryRequest(id, text);
        httpWrapper.post(url, request);
    }

    private String formatUrl(String method) {
        return String.format("https://api.telegram.org/bot%s/%s", token, method);
    }
}
