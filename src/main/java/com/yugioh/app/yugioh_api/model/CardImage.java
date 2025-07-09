package com.yugioh.app.yugioh_api.model;

import lombok.Data;

@Data
public class CardImage {
    private long id;
    private String image_url;
    private String image_url_small;
    private String image_url_cropped;
}
