package com.emarsys.pnp.inbox

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.emarsys.mobileengage.api.action.ActionModel
import com.emarsys.mobileengage.api.action.AppEventActionModel
import com.emarsys.mobileengage.api.action.OpenExternalUrlActionModel
import com.emarsys.plugnplay.inbox.R
import com.emarsys.plugnplay.inbox.databinding.EmsInboxDetailFragmentBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class EmarsysInboxDetailFragment : Fragment() {
    private val args: EmarsysInboxDetailFragmentArgs by navArgs()
    private lateinit var binding: EmsInboxDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EmsInboxDetailFragmentBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.hide()

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

        // TODO: refact
        binding.btnActionOne.visibility = View.GONE
        binding.btnActionTwo.visibility = View.GONE
        binding.btnActionThree.visibility = View.GONE

        val actions = args.inboxMessage.actions ?: listOf()

        if (actions.isNotEmpty()) {
            actions.forEachIndexed { index, action ->
                when (index) {
                    0 -> {
                        binding.btnActionOne.visibility = View.VISIBLE
                        binding.btnActionOne.text = action.title
                        binding.btnActionOne.setOnClickListener {
                            performInboxAction(action)
                        }
                    }
                    1 -> {
                        binding.btnActionTwo.visibility = View.VISIBLE
                        binding.btnActionTwo.text = action.title
                        binding.btnActionTwo.setOnClickListener {
                            performInboxAction(action)
                        }
                    }
                    2 -> {
                        binding.btnActionThree.visibility = View.VISIBLE
                        binding.btnActionThree.text = action.title
                        binding.btnActionThree.setOnClickListener {
                            performInboxAction(action)
                        }
                    }
                    else -> {
                        print("no action")
                    }
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener { view.findNavController().popBackStack() }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    private fun performInboxAction(action: ActionModel) {
        when(action) {
            is OpenExternalUrlActionModel -> {
                onOpenExternalUrlTriggered(requireContext(), action)
            }
            is AppEventActionModel -> {
//                TODO: callback
            }
            else -> { }
        }
    }

    private fun onOpenExternalUrlTriggered(context: Context, actionModel: OpenExternalUrlActionModel) {
        val uri = Uri.parse(actionModel.url.toString())
        val externalUrlIntent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(externalUrlIntent)
    }
}


