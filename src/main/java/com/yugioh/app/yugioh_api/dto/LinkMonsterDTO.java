package com.yugioh.app.yugioh_api.dto;


import com.yugioh.app.yugioh_api.model.BanlistInfo;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkMonsterDTO extends SpellTrapCardDTO {
    private String name;
    private String type;
    private String desc;
    private String race;
    private String attribute;
    private int atk;
    private int linkVal;
    private List<String> linkMarkers;
    private BanlistInfo banlistInfo;

    @Override
    public BanlistInfo getBanlistInfo() {
        return banlistInfo;
    }
}
