package infrastructure.telegram;

import bot.entities.Button;
import bot.entities.CallbackQuery;
import bot.entities.ReceivedMessage;
import bot.entities.Update;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.user.UserId;
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
        List<Parameter> parameters = new ArrayList<Parameter>() {{
            add(new Parameter("chat_id", chatId));
            add(new Parameter("text", text));
            add(new Parameter("parse_mode", "HTML"));
            add(new Parameter("disable_web_page_preview", true));
            if (buttons != null && !buttons.isEmpty()) {
                add(new Parameter("reply_markup", serializeButtons(buttons)));
            }
        }};

        return serializeParameters(parameters);
    }

    public String formatAnswerCallbackQueryRequest(String id, String text) {
        return serializeParameters(
            new Parameter("callback_query_id", id),
            new Parameter("text", text));
    }

    public String formatGetUpdatesRequest(long offset) {
        return serializeParameters(new Parameter("offset", offset));
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
            ReceivedMessage message = new ReceivedMessage(new UserId(updateEntity.message.from.id), updateEntity.message.text);

            return new Update(updateEntity.update_id, message);
        }

        if (updateEntity.callback_query != null) {
            CallbackQuery query = new CallbackQuery(updateEntity.callback_query.id,
                new UserId(updateEntity.callback_query.from.id),
                updateEntity.callback_query.message.message_id,
                updateEntity.callback_query.data);

            return new Update(updateEntity.update_id, query);
        }

        return null; // Unsupported update
    }

    private String serializeButtons(List<Button> buttons) {
        List<Map<String, String>> buttonRow =
            buttons.stream()
                .map(b -> new HashMap<String, String>() {{
                    put("text", b.text);
                    put("callback_data", b.data);
                }})
                .collect(Collectors.toList());

        return serializeParameters(new Parameter("inline_keyboard", Collections.singleton(buttonRow)));
    }

    public String formatDeleteMessageRequest(long chatId, long messageId) {
        return serializeParameters(new Parameter("chat_id", chatId),
            new Parameter("message_id", messageId));
    }

    private String serializeParameters(Parameter... parameters) {
        Map<String, Object> mapParams = new HashMap<>();

        for (Parameter p : parameters) {
            mapParams.put(p.key, p.value);
        }

        try {
            return mapper.writeValueAsString(mapParams);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new SerializerException();
        }
    }

    private String serializeParameters(List<Parameter> parameters) {
        return serializeParameters(parameters.toArray(new Parameter[0]));
    }
}
