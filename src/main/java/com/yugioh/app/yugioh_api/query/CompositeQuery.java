package com.yugioh.app.yugioh_api.query;

import java.util.List;
import java.util.stream.Collectors;

public class CompositeQuery implements CardQuery {

    private final List<CardQuery> queries;

    public CompositeQuery(List<CardQuery> queries) {
        this.queries = queries;
    }

    @Override
    public String toQueryString() {
        return queries.stream()
                .map(CardQuery::toQueryString)
                .collect(Collectors.joining("&"));
    }
}
