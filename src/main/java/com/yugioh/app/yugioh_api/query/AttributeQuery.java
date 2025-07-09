package com.yugioh.app.yugioh_api.query;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AttributeQuery implements CardQuery{
    private final String attribute;

    public AttributeQuery(String attribute){
        this.attribute = attribute;
    }

    @Override
    public String toQueryString(){
        return "attribute=" + URLEncoder.encode(attribute, StandardCharsets.UTF_8);
    }
}
