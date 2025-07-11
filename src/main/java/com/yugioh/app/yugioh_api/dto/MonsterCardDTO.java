package com.yugioh.app.yugioh_api.dto;

import com.yugioh.app.yugioh_api.model.BanlistInfo;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonsterCardDTO implements CardDTO {
    private String name;
    private String type;
    private String desc;
    private String race;
    private String attribute;
    private Integer level;
    private Integer atk;
    private Integer def;
    private BanlistInfo banlistInfo;

    @Override
    public BanlistInfo getBanlistInfo() {
        return banlistInfo;
    }
}
