package com.algolia.instantsearch.showcase.answers

import android.view.View
import androidx.core.text.HtmlCompat
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.showcase.databinding.IncludeAnswerBinding

class AnswerView(private val binding: IncludeAnswerBinding) : HitsView<Talk> {

    override fun setHits(hits: List<Talk>) {
        val talk = hits.getOrNull(0)
        if (talk == null) {
            binding.root.visibility = View.GONE
            return
        }
        val content = talk.answer ?: talk.transcript
        binding.root.visibility = View.VISIBLE
        binding.answerTitle.text = talk.title
        binding.answerContent.text = HtmlCompat.fromHtml(content, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}
