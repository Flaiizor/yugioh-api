package com.yugioh.app.yugioh_api.query;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class RaceQuery implements CardQuery{
    private final String race;

    public RaceQuery(String race){
        this.race = race;
    }

    @Override
    public String toQueryString(){
        return "race=" + URLEncoder.encode(race, StandardCharsets.UTF_8);
    }
}
