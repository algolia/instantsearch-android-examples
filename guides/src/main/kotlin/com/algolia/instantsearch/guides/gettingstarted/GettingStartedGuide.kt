package com.algolia.instantsearch.guides.gettingstarted

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.algolia.instantsearch.guides.R

class GettingStartedGuide : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getting_started)

        showProductFragment()
    }

    fun showFacetFragment() {
        supportFragmentManager.commit {
            replace<ProductFragment>(R.id.container)
            setReorderingAllowed(true)
            addToBackStack("facet")
        }
    }

    fun showProductFragment() {
        supportFragmentManager.commit {
            replace<ProductFragment>(R.id.container)
            setReorderingAllowed(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        supportFragmentManager.popBackStack()
        return super.onSupportNavigateUp()
    }
}