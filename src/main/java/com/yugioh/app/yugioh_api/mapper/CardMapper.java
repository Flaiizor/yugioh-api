package com.yugioh.app.yugioh_api.mapper;

import com.yugioh.app.yugioh_api.dto.*;
import com.yugioh.app.yugioh_api.model.Card;

import java.util.List;
import java.util.Locale;

public class CardMapper {

    public static CardDTO toDTO(Card card) {
        if (card == null || card.getType() == null) {
            return null;
        }

        String type = card.getType().toLowerCase(Locale.ROOT);

        // Spell or Trap
        if (type.contains("spell") || type.contains("trap")) {
            return toSpellTrapDTO(card);
        }

        // Link Monsters
        if (type.contains("link")) {
            return toLinkDTO(card);
        }

        // Pendulum Monsters
        if (card.getScale() != null && card.getPend_desc() != null) {
            return toPendulumDTO(card);
        }

        // Standard Monster Cards
        return toMonsterDTO(card);
    }

    public static SpellTrapCardDTO toSpellTrapDTO(Card card) {
        return new SpellTrapCardDTO(
                card.getName(),
                card.getType(),
                card.getDesc(),
                card.getRace(),
                card.getBanlist_info()
        );
    }

    public static MonsterCardDTO toMonsterDTO(Card card) {
        return new MonsterCardDTO(
                card.getName(),
                card.getType(),
                card.getDesc(),
                card.getRace(),
                card.getAttribute(),
                card.getLevel(),
                card.getAtk(),
                card.getDef(),
                card.getBanlist_info()
        );
    }

    public static PendulumMonsterDTO toPendulumDTO(Card card) {
        return new PendulumMonsterDTO(
                card.getName(),
                card.getType(),
                card.getDesc(),
                card.getRace(),
                card.getAttribute(),
                card.getLevel(),
                card.getAtk(),
                card.getDef(),
                card.getScale(),
                card.getPend_desc(),
                card.getBanlist_info()
        );
    }

    public static LinkMonsterDTO toLinkDTO(Card card) {
        return new LinkMonsterDTO(
                card.getName(),
                card.getType(),
                card.getDesc(),
                card.getRace(),
                card.getAttribute(),
                card.getAtk(),
                card.getLinkval(),
                card.getLinkmarkers() != null ? card.getLinkmarkers() : List.of(),
                card.getBanlist_info()
        );
    }
}
