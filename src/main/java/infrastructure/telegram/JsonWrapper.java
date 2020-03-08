package infrastructure.telegram;

import application.entities.Button;
import application.entities.CallbackQuery;
import application.entities.ReceivedMessage;
import application.entities.Update;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import infrastructure.telegram.entities.AnswerCallbackQueryRequest;
import infrastructure.telegram.entities.GetUpdatesRequest;
import infrastructure.telegram.entities.InlineKeyboardButton;
import infrastructure.telegram.entities.InlineKeyboardMarkup;
import infrastructure.telegram.entities.ResultsEntity;
import infrastructure.telegram.entities.SendMessageRequest;
import infrastructure.telegram.entities.UpdateEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonWrapper {
    private ObjectMapper mapper = new ObjectMapper();

    public String formatMessageRequest(long chatId, String text, List<Button> buttons) {
        SendMessageRequest parameters = new SendMessageRequest(chatId, text);

        if (buttons != null && !buttons.isEmpty()) {
            parameters.reply_markup = serializeButtons(buttons);
        }

        return serializeParameters(parameters);
    }

    public String formatAnswerCallbackQueryRequest(String id, String text) {
        return serializeParameters(new AnswerCallbackQueryRequest(id, text));
    }

    public String formatGetUpdatesRequest(long offset) {
        return serializeParameters(new GetUpdatesRequest(offset));
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
            ReceivedMessage message = new ReceivedMessage(updateEntity.message.from.id, updateEntity.message.text);

            return new Update(updateEntity.update_id, message);
        }

        if (updateEntity.callback_query != null) {
            CallbackQuery query = new CallbackQuery(updateEntity.callback_query.id,
                updateEntity.callback_query.from.id,
                updateEntity.callback_query.message.message_id,
                updateEntity.callback_query.data);

            return new Update(updateEntity.update_id, query);
        }

        return null; // Unsupported update
    }

    private String serializeButtons(List<Button> buttons) {
        List<InlineKeyboardButton> buttonRow =
            buttons.stream()
                .map(b -> new InlineKeyboardButton(b.text, b.data))
                .collect(Collectors.toList());

        return serializeParameters(new InlineKeyboardMarkup(buttonRow));
    }

    private String serializeParameters(Object parameters) {
        try {
            return mapper.writeValueAsString(parameters);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new SerializerException();
        }
    }
}
