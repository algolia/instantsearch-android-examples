package com.algolia.custombackend.custombackend;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CustomSearchParameters {

    /** The query text */
    @NonNull
    public String query;

    public CustomSearchParameters(@Nullable String query) {
        this.query = query != null ? query : "";
    }
}
