package com.emarsys.pnp.inbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emarsys.plugnplay.inbox.R
import com.emarsys.plugnplay.inbox.databinding.EmsInboxListItemBinding

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
                    oldItem.receivedAt == newItem.receivedAt &&
                    oldItem.isPinned == newItem.isPinned
        }
    }

    class ViewHolder(private val view: View, private val viewModel: EmarsysInboxViewModel) :
        RecyclerView.ViewHolder(view) {
        private val binding = EmsInboxListItemBinding.bind(view)
        private val title: TextView = binding.notificationTitle
        private val date: TextView = binding.notificationDate
        private val pin: ImageButton = binding.notificationPin

        fun bindTo(message: EmarsysInboxMessage) {
            title.text = message.title
            date.text = message.receivedAt
            pin.isSelected = message.isPinned
            pin.setOnClickListener { viewModel.pin(message) }
            view.setOnClickListener { viewModel.opened(message) }
        }
    }
}

