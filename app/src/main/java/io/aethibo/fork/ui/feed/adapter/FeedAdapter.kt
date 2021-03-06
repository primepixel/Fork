/*
 * Created by Karic Kenan on 21.4.2021
 * Copyright (c) 2021 . All rights reserved.
 */

package io.aethibo.fork.ui.feed.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.aethibo.domain.EventType
import io.aethibo.domain.response.EventsResponse
import io.aethibo.fork.R

class FeedAdapter : ListAdapter<EventsResponse, FeedAdapter.FeedViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<EventsResponse>() {
        override fun areItemsTheSame(oldItem: EventsResponse, newItem: EventsResponse): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: EventsResponse, newItem: EventsResponse): Boolean =
            oldItem.hashCode() == newItem.hashCode()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(getItem(position) ?: return)
    }

    inner class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(event: EventsResponse) = with(itemView) {

            /**
             * Init view
             */
            val title = findViewById<TextView>(R.id.tvEventItemTitle)
            val description = findViewById<TextView>(R.id.tvEventItemDescription)
            val date = findViewById<TextView>(R.id.tvEventItemDate)

            /**
             * Assign values
             */
            title.text = context.getString(R.string.labelEventTypeTitle, event.type)
            date.text = event.createdAt

            when (event.type) {
                EventType.CreateEvent.name -> description.text = context.getString(
                    R.string.labelEventTypeCreate,
                    event.actor.displayLogin,
                    event.repo.name
                )
                EventType.IssueCommentEvent.name -> description.text = context.getString(
                    R.string.labelEventTypeIssueComment,
                    event.actor.displayLogin,
                    event.repo.name
                )
                EventType.IssuesEvent.name -> description.text = context.getString(
                    R.string.labelEventTypeIssue,
                    event.actor.displayLogin,
                    event.repo.name
                )
                EventType.PublicEvent.name -> description.text = context.getString(
                    R.string.labelEventTypePublic,
                    event.actor.displayLogin,
                    event.repo.name,
                    event.public.toString()
                )
                EventType.PushEvent.name -> description.text = context.getString(
                    R.string.labelEventTypePush,
                    event.actor.displayLogin,
                    event.repo.name
                )
                EventType.WatchEvent.name -> description.text = context.getString(
                    R.string.labelEventTypeWatch,
                    event.actor.displayLogin,
                    event.repo.name
                )
                EventType.ForkEvent.name -> description.text = context.getString(
                    R.string.labelEventTypeFork,
                    event.actor.displayLogin,
                    event.repo.name
                )
            }

            setOnClickListener {
                onEventClickListener?.let { click ->
                    click(event)
                }
            }
        }
    }

    /**
     * Click listeners
     */
    private var onEventClickListener: ((EventsResponse) -> Unit)? = null

    fun setOnEventItemClickListener(listener: (EventsResponse) -> Unit) {
        onEventClickListener = listener
    }
}