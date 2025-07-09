package com.yugioh.app.yugioh_api.model;

import lombok.Data;

@Data
public class CardPrice {
    private String cardmarket_price;
    private String tcgplayer_price;
    private String ebay_price;
    private String amazon_price;
    private String coolstuffinc_price;
}
