package com.yugioh.app.yugioh_api.query;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PartialNameQuery implements CardQuery {
    private final String partialName;

    public PartialNameQuery(String partialName) {
        this.partialName = partialName;
    }

    @Override
    public String toQueryString() {
        return "fname=" + URLEncoder.encode(partialName, StandardCharsets.UTF_8);
    }
}