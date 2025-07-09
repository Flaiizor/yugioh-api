package com.yugioh.app.yugioh_api.query;

public class LevelQuery implements CardQuery {

    private final int level;

    public LevelQuery(int level) {
        this.level = level;
    }

    @Override
    public String toQueryString() {
        return "level=" + level;
    }
}
