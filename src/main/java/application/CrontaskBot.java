package application;

import application.entities.CallbackQuery;
import application.entities.Message;
import application.states.DefaultState;
import java.util.HashMap;
import java.util.Map;

public class CrontaskBot {
    private TelegramAPI api;

    private Map<Long, BotState> states = new HashMap<>();

    public CrontaskBot(TelegramAPI api) {
        this.api = api;
    }

    public void handleMessage(Message message) {
        BotState state = states.getOrDefault(message.sender, new DefaultState());

        states.put(message.sender, state.handleMessage(message, this));
    }

    public void handleCallbackQuery(CallbackQuery query) {

    }

    public void sendMessage(long user, String text) {
        api.sendMessage(user, text);
    }
}
