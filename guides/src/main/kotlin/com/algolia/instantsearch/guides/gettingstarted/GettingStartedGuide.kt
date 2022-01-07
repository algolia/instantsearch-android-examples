package com.algolia.instantsearch.guides.gettingstarted

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.guides.R
import com.algolia.instantsearch.guides.databinding.ActivityGettingStartedBinding

class GettingStartedGuide : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityGettingStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showProductFragment()
    }

    fun showFacetFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, FacetFragment())
            .addToBackStack("facet")
            .commit()
    }

    fun showProductFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, ProductFragment())
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return super.onSupportNavigateUp()
    }
}