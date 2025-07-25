package com.yugioh.app.yugioh_api.controller;

import com.yugioh.app.yugioh_api.client.YgoApiClient;
import com.yugioh.app.yugioh_api.dto.CardDTO;
import com.yugioh.app.yugioh_api.dto.LinkMonsterDTO;
import com.yugioh.app.yugioh_api.dto.PendulumMonsterDTO;
import com.yugioh.app.yugioh_api.dto.SpellTrapCardDTO;
import com.yugioh.app.yugioh_api.mapper.CardMapper;
import com.yugioh.app.yugioh_api.model.BanlistInfo;
import com.yugioh.app.yugioh_api.model.Card;
import com.yugioh.app.yugioh_api.query.CompositeQuery;
import com.yugioh.app.yugioh_api.query.NameQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class CardControllerTests {

    private YgoApiClient ygoApiClient;
    private CardController cardController;

    private Card createLinkuribohCard() {
        Card card = new Card();
        card.setName("Linkuriboh");
        card.setType("Link Monster");
        card.setDesc("1 Level 1 Monster\\r\\nWhen an opponent's monster declares an attack...");
        card.setRace("Cyberse");
        card.setBanlist_info(new BanlistInfo("Forbidden", null, null));
        card.setAttribute("DARK");
        card.setAtk(300);
        card.setLinkval(1);
        card.setLinkmarkers(List.of("Bottom"));
        return card;
    }

    private Card createDarkMagicianCard() {
        Card card = new Card();
        card.setName("Dark Magician");
        card.setType("Normal Monster");
        card.setDesc("''The ultimate wizard in terms of attack and defense.''");
        card.setRace("Spellcaster");
        card.setAttribute("DARK");
        card.setLevel(7);
        card.setAtk(2500);
        card.setDef(2100);
        card.setBanlist_info(new BanlistInfo(null, null, null));
        return card;
    }

    @BeforeEach
    void setUp() {
        ygoApiClient = Mockito.mock(YgoApiClient.class);
        cardController = new CardController(ygoApiClient);
    }

    @Test
    void getCardByName_returnsCardDTO_whenCardExists() throws Exception {
        // Arrange
        Card sampleCard = createDarkMagicianCard();

        Mockito.when(ygoApiClient.fetchCards(any(CompositeQuery.class)))
                .thenReturn(List.of(sampleCard));

        // Act
        ResponseEntity<CardDTO> response = cardController.getCardByName("Dark Magician", false);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Dark Magician", response.getBody().getName());
    }

    @Test
    void getCardByName_returnsCardDTO_whenGivenIncorrectCaseName() throws Exception {
        Card sampleCard = createDarkMagicianCard();
        Mockito.when(ygoApiClient.fetchCards(any(CompositeQuery.class)))
                .thenReturn(List.of(sampleCard));

        ResponseEntity<CardDTO> response = cardController.getCardByName("darK MAGiCian", false);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Dark Magician", response.getBody().getName());
    }

    @Test
    void getCardByName_returns404_whenCardNotFound() throws Exception {
        // Arrange
        Mockito.when(ygoApiClient.fetchCards(any(CompositeQuery.class)))
                .thenReturn(List.of());

        // Act
        ResponseEntity<CardDTO> response = cardController.getCardByName("NonExistentCard", false);

        // Assert
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void getCardByName_returns404_whenGivenEmptyString() throws Exception {
        Mockito.when(ygoApiClient.fetchCards(any(CompositeQuery.class)))
                .thenReturn(List.of());

        ResponseEntity<CardDTO> response = cardController.getCardByName("", false);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void getCardByName_returnsMinimalDTO_whenMinimalFlagTrue() throws Exception {
        Card sampleCard = createLinkuribohCard();
        Mockito.when(ygoApiClient.fetchCards(any(CompositeQuery.class)))
                .thenReturn(List.of(sampleCard));

        ResponseEntity<CardDTO> response = cardController.getCardByName("Linkuriboh", true);
        assertEquals("Linkuriboh", response.getBody().getName());
        assertEquals(SpellTrapCardDTO.class, response.getBody().getClass());
        assertNotEquals(LinkMonsterDTO.class, response.getBody().getClass());
    }

    @Test
    void getCardByName_returnsFullDTO_whenMinimalFlagFalse() throws Exception {
        Card sampleCard = createLinkuribohCard();

        Mockito.when(ygoApiClient.fetchCards(any(CompositeQuery.class)))
                .thenReturn(List.of(sampleCard));

        ResponseEntity<CardDTO> response = cardController.getCardByName("Linkuriboh", false);
        assertEquals("Linkuriboh", response.getBody().getName());
        assertNotEquals(SpellTrapCardDTO.class, response.getBody().getClass());
        assertEquals(LinkMonsterDTO.class, response.getBody().getClass());
    }

    @Test
    void getCardByName_throwsException_whenApiFails() throws Exception {
        Mockito.when(ygoApiClient.fetchCards(any(CompositeQuery.class)))
                .thenThrow(new IOException("API failure"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cardController.getCardByName("Dark Magician", false));

        assertTrue(exception.getMessage().contains("Failed to fetch cards"));
    }

    @Test
    void getCardByName_returns404_whenNameIsNull() throws Exception {
        ResponseEntity<CardDTO> response = cardController.getCardByName(null, false);
        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }


    @Test
    void searchCards_returnsFilteredAndSortedDTOs() throws Exception {
        Card card1 = new Card();
        card1.setName("Card A");
        card1.setRace("Dragon");
        card1.setType("Effect Monster");
        card1.setAtk(2500);
        card1.setBanlist_info(new BanlistInfo("Limited", null, null));

        Card card2 = new Card();
        card2.setName("Card B");
        card2.setRace("Dragon");
        card2.setType("Effect Monster");
        card2.setAtk(1800);
        card2.setBanlist_info(new BanlistInfo("Forbidden", null, null));

        Card card3 = new Card();
        card3.setName("Card C");
        card3.setRace("Beast");
        card3.setType("Effect Monster");
        card3.setAtk(2000);
        card3.setBanlist_info(new BanlistInfo("Limited", null, null));

        Mockito.when(ygoApiClient.fetchCards(any(CompositeQuery.class)))
                .thenAnswer(invocation -> {
                    return List.of(card1, card2, card3).stream()
                            .filter(c -> c.getName().contains("Card"))
                            .filter(c -> "Effect Monster".equals(c.getType()))
                            .filter(c -> "Dragon".equals(c.getRace()))
                            .toList();
                });

        ResponseEntity<List<CardDTO>> response = cardController.searchCards(
                "Card",
                "tcg",
                "limited",
                "Dragon",
                "Effect Monster",
                null,
                null,
                null,
                "atk",
                "desc"
        );

        assertEquals(200, response.getStatusCodeValue());
        List<CardDTO> results = response.getBody();
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Card A", results.get(0).getName());
    }


}
