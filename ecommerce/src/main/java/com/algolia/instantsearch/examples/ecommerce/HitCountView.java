package com.algolia.instantsearch.examples.ecommerce;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.TextView;

import com.algolia.instantsearch.Searcher;
import com.algolia.instantsearch.model.SearchResults;
import com.algolia.instantsearch.views.AlgoliaWidget;
import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Query;


public class HitCountView extends TextView implements AlgoliaWidget {
    public HitCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSearcher(@NonNull Searcher searcher) {
    }

    @Override
    public void onReset() {
        setVisibility(GONE);
    }

    @Override
    public void onResults(SearchResults results, boolean isLoadingMore) {
        setVisibility(VISIBLE);
        setText(String.format("%d first results", results.nbHits));
    }

    @Override
    public void onError(Query query, AlgoliaException error) {
        setVisibility(VISIBLE);
        setText("Error, please try again");
    }
}
