package com.algolia.instantsearch.guides.directory

import com.algolia.instantsearch.guides.gettingstarted.GettingStartedGuide
import com.algolia.instantsearch.guides.insights.InsightsActivity
import com.algolia.instantsearch.guides.places.PlacesActivity
import com.algolia.instantsearch.guides.querysuggestion.QuerySuggestionActivity
import com.algolia.search.model.ObjectID

val guides = mapOf(
    ObjectID("guide_getting_started") to GettingStartedGuide::class,
    ObjectID("guide_places") to PlacesActivity::class,
    ObjectID("guide_query_suggestion") to QuerySuggestionActivity::class,
    ObjectID("guide_insights") to InsightsActivity::class,
    ObjectID("guide_compose_ui") to InsightsActivity::class,
    ObjectID("guide_voice_search") to InsightsActivity::class,
)