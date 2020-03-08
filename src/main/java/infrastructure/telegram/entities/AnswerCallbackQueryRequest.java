package infrastructure.telegram.entities;

public class AnswerCallbackQueryRequest {
    public String callback_query_id;
    public String text;

    public AnswerCallbackQueryRequest(String callback_query_id, String text) {
        this.callback_query_id = callback_query_id;
        this.text = text;
    }
}
