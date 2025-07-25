package com.yugioh.app.yugioh_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class BanlistInfo {
    private String ban_tcg;
    private String ban_ocg;
    private String ban_goat;
}
