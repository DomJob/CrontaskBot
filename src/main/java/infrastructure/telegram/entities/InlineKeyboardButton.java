package infrastructure.telegram.entities;

public class InlineKeyboardButton {
    public String text;
    public String callback_data;

    public InlineKeyboardButton(String text, String callback_data) {
        this.text = text;
        this.callback_data = callback_data;
    }
}
