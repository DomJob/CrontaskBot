package infrastructure.telegram.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessageRequest {
    public long chat_id;
    public String text;
    public String reply_markup;
    public String parse_mode = "HTML";
    public boolean disable_web_page_preview = true;

    public SendMessageRequest(long chat_id, String text) {
        this.chat_id = chat_id;
        this.text = text;
    }
}
