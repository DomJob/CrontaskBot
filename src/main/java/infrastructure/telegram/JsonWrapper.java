package infrastructure.telegram;

import application.entities.Button;
import application.entities.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import infrastructure.eventhandler.Update;
import infrastructure.telegram.entities.ResultsEntity;
import infrastructure.telegram.entities.UpdateEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonWrapper {
    private ObjectMapper mapper = new ObjectMapper();

    public String formatMessageRequest(long chatId, String text, List<Button> buttons) {
        Map<String, Object> parameters = new MapBuilder()
            .add("chat_id", chatId)
            .add("text", text)
            .build();

        if (buttons != null && !buttons.isEmpty()) {
            parameters.put("reply_markup", serializeButtons(buttons));
        }

        return serializeParameters(parameters);
    }

    public String formatAnswerCallbackQueryRequest(String id, String text) {
        Map<String, Object> parameters = new MapBuilder()
            .add("callback_query_id", id)
            .add("text", text)
            .build();

        return serializeParameters(parameters);
    }

    public String formatGetUpdatesRequest(long offset) {
        Map<String, Object> parameters = new MapBuilder()
            .add("offset", offset)
            .build();

        return serializeParameters(parameters);
    }

    public List<Update> deSerializeUpdates(String updatesJson) {
        try {
            ResultsEntity e = mapper.readValue(updatesJson, ResultsEntity.class);

            List<Update> updates = new ArrayList<>();

            for (UpdateEntity entity : e.result) {
                Update update = mapUpdate(entity);
                if (update != null) {
                    updates.add(mapUpdate(entity));
                }
            }

            return updates;
        } catch (IOException e) {
            e.printStackTrace();
            throw new SerializerException();
        }
    }

    private Update mapUpdate(UpdateEntity updateEntity) {
        if (updateEntity.message != null) {
            Message message = new Message(updateEntity.message.from.id, updateEntity.message.text);

            return new Update(updateEntity.update_id, message);
        }

        if (updateEntity.callback_query != null) {

        }

        return null; // Unsupported update
    }

    private String serializeButtons(List<Button> buttons) {
        List<Map<String, Object>> buttonRow = buttons.stream().map(b -> new MapBuilder()
            .add("text", b.text)
            .add("callback_data", b.data)
            .build()).collect(Collectors.toList());

        return serializeParameters(new HashMap<String, Object>() {{
            put("inline_keyboard", Collections.singletonList(buttonRow));
        }});
    }

    private String serializeParameters(Map<String, Object> parameters) {
        try {
            return mapper.writeValueAsString(parameters);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new SerializerException();
        }
    }

    private class MapBuilder {
        private Map<String, Object> map = new HashMap<>();

        public Map<String, Object> build() {
            return map;
        }

        public MapBuilder add(String key, Object value) {
            map.put(key, value);
            return this;
        }
    }
}
