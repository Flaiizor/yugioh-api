package com.yugioh.app.yugioh_api.controller;

import com.yugioh.app.yugioh_api.client.YgoApiClient;
import com.yugioh.app.yugioh_api.dto.CardDTO;
import com.yugioh.app.yugioh_api.mapper.CardMapper;
import com.yugioh.app.yugioh_api.model.BanlistInfo;
import com.yugioh.app.yugioh_api.model.Card;
import com.yugioh.app.yugioh_api.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@Tag(name = "Cards", description = "Endpoints for searching and filtering Yu-Gi-Oh! cards")
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

    // Exact name match (helpful for single-card lookup)
    @GetMapping("/name/{name}")
    public ResponseEntity<CardDTO> getCardByName(@PathVariable String name,
                                                 @RequestParam(defaultValue = "false") boolean minimal) {
        List<Card> result = fetchCards(new NameQuery(name));
        if (result.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Card card = result.get(0);
        return ResponseEntity.ok(minimal ? CardMapper.toSpellTrapDTO(card) : CardMapper.toDTO(card));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CardDTO>> searchCards(
            @RequestParam(required = false) String partialName,
            @RequestParam(required = false) String banlist,
            @RequestParam(required = false) String banlistStatus,
            @RequestParam(required = false) String race,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String archetype,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) String attribute,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        List<CardQuery> queryList = new ArrayList<>();
        if (partialName != null) queryList.add(new PartialNameQuery(partialName));
        if (banlist != null) queryList.add(new BanlistQuery(banlist));
        if (race != null) queryList.add(new RaceQuery(race));
        if (type != null) queryList.add(new TypeQuery(type));
        if (archetype != null) queryList.add(new ArchetypeQuery(archetype));
        if (level != null) queryList.add(new LevelQuery(level));
        if (attribute != null) queryList.add(new AttributeQuery(attribute));

        List<Card> cards = fetchCards(new CompositeQuery(queryList));
        if (cards.isEmpty()) return ResponseEntity.notFound().build();

        if (banlist != null && banlistStatus != null) {
            String status = banlistStatus.trim().toLowerCase();
            cards = cards.stream()
                    .filter(card -> {
                        BanlistInfo info = card.getBanlist_info();
                        if (info == null) return false;
                        return switch (banlist.toLowerCase()) {
                            case "tcg" -> matchesStatus(info.getBan_tcg(), status);
                            case "ocg" -> matchesStatus(info.getBan_ocg(), status);
                            case "goat" -> matchesStatus(info.getBan_goat(), status);
                            default -> false;
                        };
                    })
                    .toList();
        }

        if (sortBy != null) {
            boolean ascending = sortOrder.equalsIgnoreCase("asc");
            cards.sort((c1, c2) -> compareCards(c1, c2, sortBy, ascending));
        }

        List<CardDTO> dtos = cards.stream()
                .map(CardMapper::toDTO)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    private boolean matchesStatus(String actual, String expected) {
        return actual != null && actual.equalsIgnoreCase(expected);
    }

    private int compareCards(Card c1, Card c2, String sortBy, boolean ascending) {
        int result = switch (sortBy.toLowerCase()) {
            case "name" -> c1.getName().compareToIgnoreCase(c2.getName());
            case "atk" -> Integer.compare(
                    c1.getAtk() != null ? c1.getAtk() : -1,
                    c2.getAtk() != null ? c2.getAtk() : -1);
            case "def" -> Integer.compare(
                    c1.getDef() != null ? c1.getDef() : -1,
                    c2.getDef() != null ? c2.getDef() : -1);
            case "level" -> Integer.compare(
                    c1.getLevel() != null ? c1.getLevel() : -1,
                    c2.getLevel() != null ? c2.getLevel() : -1);
            case "type" -> c1.getType().compareToIgnoreCase(c2.getType());
            case "race" -> c1.getRace().compareToIgnoreCase(c2.getRace());
            default -> 0;
        };
        return ascending ? result : -result;
    }

}
