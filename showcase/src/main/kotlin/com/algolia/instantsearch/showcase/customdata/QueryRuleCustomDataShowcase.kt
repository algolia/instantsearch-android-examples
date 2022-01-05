package com.algolia.instantsearch.showcase.customdata

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.core.hits.connectHitsView
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.helper.customdata.QueryRuleCustomDataConnector
import com.algolia.instantsearch.helper.searchbox.SearchBoxConnector
import com.algolia.instantsearch.helper.searchbox.SearchMode
import com.algolia.instantsearch.helper.searchbox.connectView
import com.algolia.instantsearch.helper.searcher.hits.HitsSearcher
import com.algolia.instantsearch.showcase.*
import com.algolia.instantsearch.showcase.customdata.TemplateActivity.Companion.EXTRA_CONTENT
import com.algolia.instantsearch.showcase.list.product.Product
import com.algolia.instantsearch.showcase.list.product.ProductAdapter
import com.algolia.search.helper.deserialize
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.include_search.searchView
import kotlinx.android.synthetic.main.include_search_info.*
import kotlinx.android.synthetic.main.showcase_query_rule_custom_data.*

class QueryRuleCustomDataShowcase : AppCompatActivity() {

    private val searcher = HitsSearcher(client, stubIndexName)
    private val searchBox = SearchBoxConnector(searcher, searchMode = SearchMode.AsYouType)
    private val queryRuleCustomData = QueryRuleCustomDataConnector<Banner>(searcher)
    private val connection = ConnectionHandler(searchBox, queryRuleCustomData)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_query_rule_custom_data)

        val adapter = ProductAdapter()
        val searchBoxView = SearchBoxViewAppCompat(searchView)

        connection += searchBox.connectView(searchBoxView)
        connection += searcher.connectHitsView(adapter) { response ->
            response.hits.deserialize(Product.serializer())
        }

        queryRuleCustomData.subscribe { model ->
            when {
                model == null -> noBanner()
                model.banner != null -> showBannerImage(model)
                model.title != null -> showBannerText(model)
            }
        }

        searchBox.viewModel.eventSubmit.subscribe {
            val model = queryRuleCustomData.viewModel.item.value ?: return@subscribe
            if (model.banner == null && model.title == null) {
                redirect(model.link, resources.getString(R.string.redirect_via_submit))
            }
        }

        configureToolbar(toolbar)
        configureSearcher(searcher)
        configureRecyclerView(hits, adapter)
        configureSearchView(searchView, getString(R.string.search_products))
        configureHelp(info)

        searcher.searchAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
        connection.clear()
    }

    private fun showBannerImage(model: Banner) {
        bannerImage.visibility = View.VISIBLE
        Glide.with(this)
            .load(model.banner)
            .fitCenter()
            .into(bannerImage)

        bannerImage.setOnClickListener {
            redirect(model.link, resources.getString(R.string.redirect_via_banner_tap))
        }
    }

    private fun showBannerText(model: Banner) {
        bannerText.visibility = View.VISIBLE
        bannerText.text = model.title
        bannerText.setOnClickListener {
            redirect(model.link, resources.getString(R.string.redirect_via_banner_tap))
        }
    }

    private fun redirect(link: String, content: String? = null) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        content?.let { intent.putExtra(EXTRA_CONTENT, it) }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun noBanner() {
        bannerImage.reset()
        bannerText.reset()
    }

    private fun View.reset() {
        visibility = View.GONE
        setOnClickListener(null)
    }

    private fun configureHelp(view: View) {
        view.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.help)
                .setItems(R.array.query_rule_custom_data_help, null)
                .setPositiveButton(R.string.ok, null)
                .show()
        }
    }
}
