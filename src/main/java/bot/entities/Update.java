package bot.entities;

import domain.time.Time;

public class Update {
    public long id;
    public ReceivedMessage message;
    public CallbackQuery callbackQuery;
    public UpdateType type;

    public Update(long id, ReceivedMessage message) {
        this.id = id;
        this.message = message;
        this.type = UpdateType.MESSAGE;
    }

    public Update(long id, CallbackQuery callbackQuery) {
        this.id = id;
        this.callbackQuery = callbackQuery;
        this.type = UpdateType.CALLBACK;
    }

    public void setTime(Time time) {
        switch (type) {
            case CALLBACK:
                callbackQuery.time = time;
                break;
            case MESSAGE:
                message.time = time;
                break;
        }
    }
}
