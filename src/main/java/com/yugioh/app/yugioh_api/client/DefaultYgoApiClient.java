package com.yugioh.app.yugioh_api.client;

import com.google.gson.*;
import com.yugioh.app.yugioh_api.model.Card;
import com.yugioh.app.yugioh_api.query.CardQuery;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DefaultYgoApiClient implements YgoApiClient {

    private static final String BASE_URL = "https://db.ygoprodeck.com/api/v7/cardinfo.php";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    @Override
    public List<Card> fetchCards(CardQuery query) throws IOException {
        String url = BASE_URL + "?" + query.toQueryString();
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                log.warn("Failed request: {}", response);
                return new ArrayList<>();
            }

            JsonObject jsonObject = gson.fromJson(response.body().charStream(), JsonObject.class);
            JsonArray dataArray = jsonObject.getAsJsonArray("data");

            List<Card> cards = new ArrayList<>();
            for (JsonElement element : dataArray) {
                cards.add(gson.fromJson(element, Card.class));
            }

            return cards;
        }
    }
}
