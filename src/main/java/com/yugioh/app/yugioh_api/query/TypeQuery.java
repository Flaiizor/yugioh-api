package com.yugioh.app.yugioh_api.query;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TypeQuery implements CardQuery{
    private final String type;

    public TypeQuery(String type){
        this.type = type;
    }

    @Override
    public String toQueryString(){
        return "type=" + URLEncoder.encode(type, StandardCharsets.UTF_8);
    }
}
