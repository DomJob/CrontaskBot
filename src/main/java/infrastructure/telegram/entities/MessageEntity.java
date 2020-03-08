package infrastructure.telegram.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageEntity {
    public long message_id;
    public UserEntity from;
    public String text;
    public long date;
}
