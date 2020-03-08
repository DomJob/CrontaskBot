package infrastructure.telegram.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CallbackQueryEntity {
    public String id;
    public UserEntity from;
    public MessageEntity message;
    public String data;
}
