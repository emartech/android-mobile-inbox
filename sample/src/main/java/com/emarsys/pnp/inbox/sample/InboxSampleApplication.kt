package com.emarsys.pnp.inbox.sample

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.emarsys.Emarsys
import com.emarsys.config.EmarsysConfig
import com.emarsys.mobileengage.api.event.EventHandler
import org.json.JSONObject

class InboxSampleApplication : Application(), EventHandler {

    override fun onCreate() {
        super.onCreate()
        val config = EmarsysConfig(application=this,
            applicationCode="EMS98-029BE",
            verboseConsoleLoggingEnabled= true)
        createNotificationChannels()
        Emarsys.setup(config)

        Emarsys.inApp.setEventHandler(this)
        Emarsys.push.setNotificationEventHandler(this)
        Emarsys.push.setSilentMessageEventHandler(this)
        Emarsys.geofence.setEventHandler(this)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel(
                "ems_sample_news",
                "News",
                "News and updates go into this channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            createNotificationChannel(
                "ems_sample_messages",
                "Messages",
                "Important messages go into this channel",
                NotificationManager.IMPORTANCE_HIGH
            )
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        id: String,
        name: String,
        description: String,
        importance: Int
    ) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(id, name, importance)
        channel.description = description
        notificationManager.createNotificationChannel(channel)
    }

    override fun handleEvent(context: Context, eventName: String, payload: JSONObject?) {
    }
}