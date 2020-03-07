package infrastructure.telegram.entities;

import application.entities.Message;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CallbackQueryEntity {
    public UserEntity from;
    public Message message;
}
