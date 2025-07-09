package com.yugioh.app.yugioh_api.controller;

import com.yugioh.app.yugioh_api.client.YgoApiClient;
import com.yugioh.app.yugioh_api.model.Card;
import com.yugioh.app.yugioh_api.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final YgoApiClient ygoApiClient;

    @Autowired
    public CardController(YgoApiClient ygoApiClient) {
        this.ygoApiClient = ygoApiClient;
    }

    private List<Card> fetchCards(CardQuery... queries) {
        try {
            return ygoApiClient.fetchCards(new CompositeQuery(List.of(queries)));
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch cards", e);
        }
    }

    @GetMapping("/archetype/{name}")
    public List<Card> getCardsByArchetype(@PathVariable String name) {
        return fetchCards(new ArchetypeQuery(name));
    }

    @GetMapping("/name/{name}")
    public Card getCardByName(@PathVariable String name) {
        List<Card> result = fetchCards(new NameQuery(name));
        return result.isEmpty() ? null : result.get(0);
    }

    @GetMapping("/partialname/{name}")
    public List<Card> getCardsByPartialName(@PathVariable String name) {
        return fetchCards(new PartialNameQuery(name));
    }

    @GetMapping("/level/{level}")
    public List<Card> getCardsByLevel(@PathVariable int level) {
        return fetchCards(new LevelQuery(level));
    }

    @GetMapping("/attribute/{attribute}")
    public List<Card> getCardsByAttribute(@PathVariable String attribute) {
        return fetchCards(new AttributeQuery(attribute));
    }

    @GetMapping("/type/{type}")
    public List<Card> getCardsByType(@PathVariable String type) {
        return fetchCards(new TypeQuery(type));
    }

    @GetMapping("race/{race}")
    public List<Card> getCardsByRace(@PathVariable String race) {
        return fetchCards(new RaceQuery(race));
    }

    @GetMapping("/search")
    public List<Card> searchCards(
            @RequestParam(required = false) String race,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String archetype,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) String attribute) {
        try {
            List<CardQuery> queryList = new ArrayList<>();

            if (race != null) queryList.add(new RaceQuery(race));
            if (type != null) queryList.add(new TypeQuery(type));
            if (archetype != null) queryList.add(new ArchetypeQuery(archetype));
            if (level != null) queryList.add(new LevelQuery(level));
            if (attribute != null) queryList.add(new AttributeQuery(attribute));

            return ygoApiClient.fetchCards(new CompositeQuery(queryList));
        } catch (IOException e) {
            throw new RuntimeException("Failed to search cards with filters", e);
        }
    }
}
