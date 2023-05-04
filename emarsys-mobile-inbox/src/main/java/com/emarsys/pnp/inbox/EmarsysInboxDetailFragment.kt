package com.emarsys.pnp.inbox

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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

        val actions = args.inboxMessage.actions ?: listOf()
        actions.forEachIndexed { index, action ->
            val button = createActionButton(requireContext(), action)
            binding.buttonContainer.addView(button, index)

            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.buttonContainer)

            if (index == 0) {
                constraintSet.connect(button.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, (button.layoutParams as ViewGroup.MarginLayoutParams).topMargin)
            } else {
                constraintSet.connect(button.id, ConstraintSet.TOP, binding.buttonContainer.getChildAt(index - 1).id, ConstraintSet.BOTTOM, (button.layoutParams as ViewGroup.MarginLayoutParams).topMargin)
            }

            constraintSet.connect(button.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            constraintSet.connect(button.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            constraintSet.applyTo(binding.buttonContainer)
        }

        binding.toolbar.setNavigationOnClickListener { view.findNavController().popBackStack() }

        return view
    }

    private fun createActionButton(context: Context, action: ActionModel): Button {
        val button = Button(context)
        button.id = View.generateViewId()

        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = dpToPx(8, context.resources)
        button.layoutParams = layoutParams

        val backgroundDrawable = GradientDrawable()
        backgroundDrawable.shape = GradientDrawable.RECTANGLE
        backgroundDrawable.cornerRadius = dpToPx(4, context.resources).toFloat()
        backgroundDrawable.setColor(resources.getColor(R.color.default_color))
        button.background = backgroundDrawable

        button.text = action.title
        button.setTextColor(Color.BLACK)
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        button.isAllCaps = false

        button.setOnClickListener {
            performInboxAction(action)
        }

        return button
    }

    private fun dpToPx(dp: Int, resources: Resources): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
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


