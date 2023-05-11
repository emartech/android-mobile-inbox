package com.emarsys.pnp.inbox

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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
                    oldItem.isPinned() == newItem.isPinned()
        }
    }

    class ViewHolder(private val view: View, private val viewModel: EmarsysInboxViewModel, val listener: (EmarsysInboxMessage) -> Unit) :
        RecyclerView.ViewHolder(view) {
        private val binding = EmsInboxListItemBinding.bind(view)
        private val title: TextView = binding.title
        private val body: TextView = binding.body
        private val pinIcon: ImageButton = binding.pinIconButton
        private val highPriorityIcon: ImageView = binding.highPriorityIcon
        private val icon: ImageView = binding.icon
        private val notOpenedView: ImageView = binding.notOpenedView

        fun bindTo(message: EmarsysInboxMessage) {
            title.text = message.title
            title.setTextColor(EmarsysInboxConfig.bodyForegroundColor)

            body.text = message.body
            body.setTextColor(EmarsysInboxConfig.bodyForegroundColor)

            pinIcon.isSelected = message.isPinned()
            pinIcon.setImageDrawable(
                if (pinIcon.isSelected) ContextCompat.getDrawable(itemView.context, EmarsysInboxConfig.favImageOn)
                else ContextCompat.getDrawable(itemView.context, EmarsysInboxConfig.favImageOff)
            )
            pinIcon.setColorFilter(
                if (pinIcon.isSelected) EmarsysInboxConfig.bodyHighlightTintColor
                else EmarsysInboxConfig.bodyTintColor
            )

            highPriorityIcon.isVisible = message.isHighPriority()
            highPriorityIcon.setImageDrawable(ContextCompat.getDrawable(itemView.context, EmarsysInboxConfig.highPriorityImage))

            notOpenedView.setBackgroundColor(if (message.isOpened()) Color.TRANSPARENT else EmarsysInboxConfig.notOpenedViewColor)

            val imageUrl = message.properties["icon"].takeIf { !it.isNullOrBlank() } ?: message.imageUrl
            Picasso.get()
                .load(imageUrl)
                .placeholder(EmarsysInboxConfig.defaultImage)
                .into(icon)
            icon.setBackgroundColor(EmarsysInboxConfig.imageCellBackgroundColor)
            pinIcon.setOnClickListener { viewModel.pin(message) }

            viewModel.seen(message)
            view.setOnClickListener {
                viewModel.opened(message)
                listener(message)
            }
        }
    }
}
