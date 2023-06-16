package com.emarsys.pnp.inbox

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emarsys.pnp.inbox.databinding.EmsInboxItemDetailBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class EmarsysInboxDetailAdapter(private val context: Context, private val viewModel: EmarsysInboxViewModel) :
    RecyclerView.Adapter<EmarsysInboxDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ems_inbox_item_detail, parent, false),
            context
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewModel.messages.value?.get(position)?.let {
            holder.bindTo(it)
            viewModel.opened(it)
        }
    }

    override fun getItemCount(): Int = viewModel.messages.value?.count() ?: 0

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        private val binding = EmsInboxItemDetailBinding.bind(view)
        private val image: ImageView = binding.image
        private val title: TextView = binding.title
        private val body: TextView = binding.body
        private val datetime: TextView = binding.datetime
        private val buttonContainer = binding.buttonContainer

        fun bindTo(message: EmarsysInboxMessage) {
            Picasso.get()
                .load(message.imageUrl.takeIf { !it.isNullOrBlank() })
                .placeholder(R.drawable.ems_inbox_logo)
                .into(image, object : Callback {
                    override fun onSuccess() {
                        image.scaleType = ImageView.ScaleType.CENTER_CROP
                    }

                    override fun onError(e: Exception?) {
                        image.scaleType = ImageView.ScaleType.FIT_CENTER
                    }
                })
            title.text = message.title
            body.text = message.body
            body.movementMethod = ScrollingMovementMethod()
            datetime.text = message.receivedAt

            buttonContainer.removeAllViews()
            val actions = message.actions ?: listOf()
            actions.forEachIndexed { index, action ->
                val button = EmarsysInboxActionButton(context)
                button.action = action
                buttonContainer.addView(button, index)
            }
        }
    }
}
