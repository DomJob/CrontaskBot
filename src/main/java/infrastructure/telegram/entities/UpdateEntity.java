package infrastructure.telegram.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateEntity {
    public long update_id;
    public MessageEntity message;
    public CallbackQueryEntity callback_query;
}
