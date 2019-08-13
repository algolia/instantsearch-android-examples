package com.algolia.instantsearch.showcase.list.actor

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_actor.view.*


class ActorViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(actor: Actor) {
        view.actorName.text = actor.name
    }
}