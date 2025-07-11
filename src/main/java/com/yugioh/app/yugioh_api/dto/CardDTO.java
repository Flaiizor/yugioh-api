package com.yugioh.app.yugioh_api.dto;

import com.yugioh.app.yugioh_api.model.BanlistInfo;

public interface CardDTO {
    String getName();
    String getType();
    String getDesc();
    String getRace();
    BanlistInfo getBanlistInfo();
}