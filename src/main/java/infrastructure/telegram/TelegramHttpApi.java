package infrastructure.telegram;

import bot.TelegramApi;
import bot.message.Message;
import bot.models.Update;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.user.UserId;
import java.util.List;

public class TelegramHttpApi implements TelegramApi {
    private final String token;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpWrapper httpWrapper;
    private final JsonWrapper jsonWrapper;

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
        String request = jsonWrapper.formatMessageRequest(message.getReceiverId().toLong(), message.getText(), message.getButtons());
        httpWrapper.post(url, request);
    }

    @Override
    public void answerCallbackQuery(String id, String text) {
        String url = formatUrl("answerCallbackQuery");
        String request = jsonWrapper.formatAnswerCallbackQueryRequest(id, text);
        httpWrapper.post(url, request);
    }

    @Override
    public void deleteMessage(UserId chatId, long messageId) {
        String url = formatUrl("deleteMessage");
        String request = jsonWrapper.formatDeleteMessageRequest(chatId.toLong(), messageId);
        httpWrapper.post(url, request);
    }

    private String formatUrl(String method) {
        return String.format("https://api.telegram.org/bot%s/%s", token, method);
    }
}
