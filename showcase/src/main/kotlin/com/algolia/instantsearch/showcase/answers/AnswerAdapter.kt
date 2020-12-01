package com.algolia.instantsearch.showcase.answers

import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.algolia.instantsearch.core.hits.HitsView
import com.algolia.instantsearch.helper.android.inflate
import com.algolia.instantsearch.showcase.R
import com.algolia.instantsearch.showcase.answers.AnswerAdapter.AnswerViewHolder
import kotlinx.android.synthetic.main.list_item_answer.view.*

class AnswerAdapter : ListAdapter<Talk, AnswerViewHolder>(AnswerDiffUtil), HitsView<Talk> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(parent.inflate(R.layout.list_item_answer))
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun setHits(hits: List<Talk>) {
        submitList(hits)
    }

    class AnswerViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(answer: Talk) {
            view.title.text = answer.title
            view.speaker.text = answer.mainSpeaker
            view.transcript.text = answer.answer?.let {
                HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)
            } ?: answer.transcript
        }
    }

    object AnswerDiffUtil : DiffUtil.ItemCallback<Talk>() {

        override fun areItemsTheSame(
            oldItem: Talk,
            newItem: Talk
        ): Boolean {
            return oldItem.objectID == newItem.objectID
        }

        override fun areContentsTheSame(
            oldItem: Talk,
            newItem: Talk
        ): Boolean {
            return oldItem == newItem
        }
    }
}