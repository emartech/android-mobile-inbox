package com.emarsys.pnp.inbox

import android.content.Context
import android.graphics.Color
import android.view.View
import com.emarsys.plugnplay.inbox.R
import org.json.JSONObject

object EmarsysInboxConfig {
    var headerView: ((Context) -> View)? = null
    var headerBackgroundColor: Int = Color.parseColor("#5F9F9F")
    var headerForegroundColor: Int = Color.WHITE
    var bodyBackgroundColor: Int = Color.parseColor("#D1EEEE")
    var bodyForegroundColor: Int = Color.BLACK
    var bodyTintColor: Int = Color.parseColor("#007AFF")
    var bodyHighlightTintColor: Int = Color.parseColor("#FFD700")
    var activityIndicatorColor: Int = Color.DKGRAY
    var notOpenedViewColor: Int = Color.parseColor("#5F9F9F")
    var imageCellBackgroundColor: Int = Color.WHITE
    var favImageOff: Int = R.drawable.ems_inbox_star_outline
    var favImageOn: Int = R.drawable.ems_inbox_star
    var defaultImage: Int = R.drawable.ems_inbox_logo
    var highPriorityImage: Int = R.drawable.ems_inbox_high_priority_icon
    var actionEventHandler: ((Context, String, JSONObject?) -> Unit)? = null
    var actionButtonStyler: ((EmarsysInboxActionButton) -> Unit)? = null
}
