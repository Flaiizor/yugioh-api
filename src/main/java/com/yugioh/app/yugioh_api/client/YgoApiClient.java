package com.yugioh.app.yugioh_api.client;

import com.yugioh.app.yugioh_api.model.Card;
import com.yugioh.app.yugioh_api.query.CardQuery;

import java.io.IOException;
import java.util.List;

public interface YgoApiClient {
    List<Card> fetchCards(CardQuery query) throws IOException;
}
