package infrastructure.telegram.entities;

import java.util.Collections;
import java.util.List;

public class InlineKeyboardMarkup {
    public List<List<InlineKeyboardButton>> inline_keyboard;

    public InlineKeyboardMarkup(List<InlineKeyboardButton> inline_keyboard) {
        this.inline_keyboard = Collections.singletonList(inline_keyboard);
    }
}
