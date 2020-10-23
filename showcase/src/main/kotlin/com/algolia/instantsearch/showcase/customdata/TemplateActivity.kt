package com.algolia.instantsearch.showcase.customdata

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.showcase.R
import kotlinx.android.synthetic.main.showcase_query_rule_custom_data_template.*

class TemplateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.showcase_query_rule_custom_data_template)
        setupToolbar()
        val text = intent.getStringExtra(EXTRA_CONTENT)
        content.text = text
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.title = null
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_CONTENT = "TEMPLATE_CONTENT"
    }
}
