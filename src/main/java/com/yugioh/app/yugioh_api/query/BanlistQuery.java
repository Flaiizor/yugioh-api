package com.yugioh.app.yugioh_api.query;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class BanlistQuery implements CardQuery{
    private final String banlist;

    public BanlistQuery(String banlist){
        this.banlist = banlist;
    }

    @Override
    public String toQueryString(){
        return "banlist=" + URLEncoder.encode(banlist, StandardCharsets.UTF_8);
    }
}
