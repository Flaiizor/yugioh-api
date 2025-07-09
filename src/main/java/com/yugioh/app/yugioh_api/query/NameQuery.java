package com.yugioh.app.yugioh_api.query;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class NameQuery implements CardQuery {
    private final String name;

    public NameQuery(String name) {
        this.name = name;
    }

    @Override
    public String toQueryString() {
        return "name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
    }
}