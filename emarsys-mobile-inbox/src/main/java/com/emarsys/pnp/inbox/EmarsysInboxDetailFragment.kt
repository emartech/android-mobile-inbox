package com.emarsys.pnp.inbox

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.emarsys.plugnplay.inbox.R
import com.emarsys.plugnplay.inbox.databinding.EmsInboxDetailFragmentItemBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class EmarsysInboxDetailFragment : Fragment() {
    private val args: EmarsysInboxDetailFragmentArgs by navArgs()
    private lateinit var binding: EmsInboxDetailFragmentItemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EmsInboxDetailFragmentItemBinding.inflate(inflater, container, false)

        val view = binding.root

        Picasso.get()
            .load(args.inboxMessage.imageUrl)
            .placeholder(R.drawable.emarsys_logo)
            .into(binding.image, object : Callback {
                override fun onSuccess() {
                    binding.image.scaleType = ImageView.ScaleType.CENTER_CROP
                }

                override fun onError(e: Exception?) {
                    binding.image.scaleType = ImageView.ScaleType.FIT_CENTER
                }
            })
        binding.title.text = args.inboxMessage.title
        binding.body.text = args.inboxMessage.body
        binding.body.movementMethod = ScrollingMovementMethod()
        binding.datetime.text = args.inboxMessage.receivedAt

        val actions = args.inboxMessage.actions ?: listOf()
        actions.forEachIndexed { index, action ->
            val button = EmarsysInboxActionButton(requireContext())
            button.action = action
            binding.buttonContainer.addView(button, index)
        }

        binding.toolbar.setNavigationOnClickListener { view.findNavController().popBackStack() }

        return view
    }
}
