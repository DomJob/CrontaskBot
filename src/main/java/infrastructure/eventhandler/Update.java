package infrastructure.eventhandler;

import application.entities.CallbackQuery;
import application.entities.Message;

public class Update {
    public long id;
    public Message message;
    public CallbackQuery callbackQuery;
    public UpdateType type;

    public Update(long id, Message message) {
        this.id = id;
        this.message = message;
        this.type = UpdateType.MESSAGE;
    }

    public Update(long id, CallbackQuery callbackQuery) {
        this.id = id;
        this.callbackQuery = callbackQuery;
        this.type = UpdateType.CALLBACK;
    }
}
