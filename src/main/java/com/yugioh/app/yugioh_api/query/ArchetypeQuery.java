package com.yugioh.app.yugioh_api.query;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ArchetypeQuery implements CardQuery {
    private final String archetype;

    public ArchetypeQuery(String archetype) {
        this.archetype = archetype;
    }

    @Override
    public String toQueryString() {
        return "archetype=" + URLEncoder.encode(archetype, StandardCharsets.UTF_8);
    }
}