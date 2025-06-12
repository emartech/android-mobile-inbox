package com.emarsys.pnp.inbox

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.emarsys.mobileengage.api.action.ActionModel
import com.emarsys.mobileengage.api.action.AppEventActionModel
import com.emarsys.mobileengage.api.action.OpenExternalUrlActionModel
import org.json.JSONObject

class EmarsysInboxActionButton(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private val backgroundDrawable: GradientDrawable
    var action: ActionModel? = null
        set(value) {
            field = value
            text = value?.title
        }
    var cornerRadius: Int = 0
        set(value) {
            field = value
            setButtonCornerRadius(value)
        }
    var borderColor: Int = Color.TRANSPARENT
        set(value) {
            field = value
            backgroundDrawable.setStroke(borderWidth, borderColor)
        }
    var borderWidth: Int = 0
        set(value) {
            field = value
            backgroundDrawable.setStroke(borderWidth, borderColor)
        }

    init {
        id = View.generateViewId()

        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            100
        )
        layoutParams.topMargin = dpToPx(5, context.resources)
        layoutParams.bottomMargin = dpToPx(5, context.resources)
        this.layoutParams = layoutParams

        backgroundDrawable = GradientDrawable()
        backgroundDrawable.shape = GradientDrawable.RECTANGLE
        background = backgroundDrawable

        gravity = Gravity.CENTER

        text = action?.title
        setTextColor(EmarsysInboxConfig.bodyTintColor)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        isAllCaps = false

        EmarsysInboxConfig.actionButtonStyler?.let { it(this) }

        setOnClickListener {
            action?.let { act -> performInboxAction(context, act) }
        }
    }

    private fun performInboxAction(context: Context, action: ActionModel) {
        when(action) {
            is OpenExternalUrlActionModel -> {
                onOpenExternalUrlTriggered(context, action)
            }
            is AppEventActionModel -> {
                val jsonObjectPayload = action.payload?.let { JSONObject(it) }
                EmarsysInboxConfig.actionEventHandler?.invoke(context, action.name, jsonObjectPayload)
            }
            else -> {
                // unsupported
            }
        }
    }

    private fun onOpenExternalUrlTriggered(context: Context, actionModel: OpenExternalUrlActionModel) {
        val uri = Uri.parse(actionModel.url.toString())
        val externalUrlIntent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(externalUrlIntent)
    }

    private fun setButtonCornerRadius(cornerRadius: Int) {
        backgroundDrawable.cornerRadius = dpToPx(cornerRadius, context.resources).toFloat()
    }

    override fun setBackgroundColor(color: Int) {
        backgroundDrawable.setColor(color)
    }

    private fun dpToPx(dp: Int, resources: Resources): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}
