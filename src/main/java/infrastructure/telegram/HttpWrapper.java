package infrastructure.telegram;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpWrapper {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    public String post(String url, String jsonBody) {
        try {
            RequestBody body = RequestBody.create(jsonBody, JSON);
            Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

            return client.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
