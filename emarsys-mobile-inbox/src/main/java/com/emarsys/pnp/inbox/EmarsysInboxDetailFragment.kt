package com.emarsys.pnp.inbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.emarsys.plugnplay.inbox.R
import com.emarsys.plugnplay.inbox.databinding.EmsInboxDetailFragmentBinding
import com.emarsys.plugnplay.inbox.databinding.EmsInboxDetailFragmentItemBinding

class EmarsysInboxDetailFragment : Fragment() {
    private val viewModel: EmarsysInboxViewModel by activityViewModels()
    private var _binding: EmsInboxDetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = EmsInboxDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pager.adapter = object : RecyclerView.Adapter<EmarsysInboxDetailViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): EmarsysInboxDetailViewHolder {
                return EmarsysInboxDetailViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.ems_inbox_detail_fragment_item, parent, false),
                    this@EmarsysInboxDetailFragment
                )
            }

            override fun getItemCount(): Int {
                return viewModel.messages.value?.count() ?: 0
            }

            override fun onBindViewHolder(holder: EmarsysInboxDetailViewHolder, position: Int) {
                viewModel.messages.value?.get(position)?.let { holder.bindTo(it) }
            }
        }
        viewModel.selectedItem.value?.let { binding.pager.setCurrentItem(it, false) }

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.selectedItem.value = binding.pager.currentItem
            }
        })

        viewModel.messages.observe(viewLifecycleOwner) {
            (binding.pager.adapter as RecyclerView.Adapter<*>).notifyDataSetChanged()
        }
    }

    private class EmarsysInboxDetailViewHolder(itemView: View, val fragment: Fragment) :
        RecyclerView.ViewHolder(itemView) {
        val binding = EmsInboxDetailFragmentItemBinding.bind(itemView)
        val title = binding.title
        val body = binding.body
        val image = binding.image

        fun bindTo(message: EmarsysInboxMessage) {
            title.text = message.title
            body.text = message.body

            if (message.imageUrl != null) {
                Glide.with(fragment)
                    .load(message.imageUrl)
                    .centerCrop()
                    .into(image)
            } else {
                image.setImageDrawable(null)
            }
        }
    }
}


