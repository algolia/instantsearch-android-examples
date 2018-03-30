package com.algolia.custombackend.elasticbackend;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ElasticSearchParameters {

    /** The query text */
    @NonNull
    public String query;

    public ElasticSearchParameters(@Nullable String query) {
        this.query = query != null ? query : "";
    }
}
