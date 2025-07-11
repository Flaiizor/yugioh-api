package com.yugioh.app.yugioh_api.model;
import lombok.Data;

import java.util.List;

@Data
public class Card {
    private int id;
    private String name;
    private String type;
    private String desc;
    private String race;
    private String archetype;

    // Monster-specific
    private Integer atk;
    private Integer def;
    private Integer level;
    private String attribute;
    private Integer scale;
    private String pend_desc;
    private Integer linkval;
    private List<String> linkmarkers;

    // Optional nested data
    private List<CardSet> card_sets;
    private List<CardImage> card_images;
    private List<CardPrice> card_prices;
    private BanlistInfo banlist_info;
}

