package com.emarsys.pnp.inbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emarsys.plugnplay.inbox.R
import com.emarsys.plugnplay.inbox.databinding.EmsInboxListItemBinding
import com.squareup.picasso.Picasso

class EmarsysInboxListAdapter(private val viewModel: EmarsysInboxViewModel, private val listener: (EmarsysInboxMessage) -> Unit) :
    ListAdapter<EmarsysInboxMessage, EmarsysInboxListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ems_inbox_list_item, parent, false),
            viewModel, listener
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<EmarsysInboxMessage>() {
        override fun areItemsTheSame(oldItem: EmarsysInboxMessage, newItem: EmarsysInboxMessage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EmarsysInboxMessage, newItem: EmarsysInboxMessage): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.receivedAt == newItem.receivedAt &&
                    oldItem.isPinned == newItem.isPinned
        }
    }

    class ViewHolder(private val view: View, private val viewModel: EmarsysInboxViewModel, val listener: (EmarsysInboxMessage) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val binding = EmsInboxListItemBinding.bind(view)
        private val title: TextView = binding.title
        private val date: TextView = binding.body
        private val pinIcon: ImageButton = binding.pinIconButton
        private val highPriorityIcon: ImageView = binding.highPriorityIcon
        private val image: ImageView = binding.image
        private val notOpenedView: ImageView = binding.notOpenedView

        fun bindTo(message: EmarsysInboxMessage) {
            title.text = message.title
            date.text = message.body
            pinIcon.isSelected = message.isPinned
            highPriorityIcon.isVisible = message.isHighPriority
            if (!message.isOpened) notOpenedView.setBackgroundColor(itemView.context.resources.getColor(R.color.default_color, itemView.context.theme))
            Picasso.get().load(message.imageUrl)
                .placeholder(R.drawable.emarsys_logo)
                .into(image)
            viewModel.seen(message)
            pinIcon.setOnClickListener { viewModel.pin(message) }
            view.setOnClickListener {
                viewModel.opened(message)
                listener(message)
            }
        }
    }
}

