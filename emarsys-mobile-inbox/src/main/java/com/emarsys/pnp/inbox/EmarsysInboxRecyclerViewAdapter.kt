package com.emarsys.pnp.inbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emarsys.plugnplay.inbox.R
import kotlinx.android.synthetic.main.ems_inbox_list_item.view.*

class EmarsysInboxRecyclerViewAdapter(private val viewModel: EmarsysInboxViewModel) :
    ListAdapter<EmarsysInboxMessage, EmarsysInboxRecyclerViewAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ems_inbox_list_item, parent, false),
            viewModel
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<EmarsysInboxMessage>() {
        override fun areItemsTheSame(
            oldItem: EmarsysInboxMessage,
            newItem: EmarsysInboxMessage
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: EmarsysInboxMessage,
            newItem: EmarsysInboxMessage
        ): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.updatedAt == newItem.updatedAt &&
                    oldItem.isPinned == newItem.isPinned
        }
    }

    class ViewHolder(val view: View, val viewModel: EmarsysInboxViewModel) :
        RecyclerView.ViewHolder(view) {
        val title: TextView = view.notification_title
        val date: TextView = view.notification_date
        val pin: ImageButton = view.notification_pin

        fun bindTo(message: EmarsysInboxMessage) {
            title.text = message.title
            date.text = message.updatedAt
            pin.isSelected = message.isPinned
            pin.setOnClickListener { viewModel.pin(message) }
            view.setOnClickListener { viewModel.opened(message) }
        }
    }
}

