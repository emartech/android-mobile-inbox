package com.emarsys.pnp.inbox.sample

import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.util.Log
import com.emarsys.Emarsys
import com.emarsys.config.EmarsysConfig
import com.emarsys.pnp.inbox.EmarsysInboxConfig
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.security.ProviderInstaller

class InboxSampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val config = EmarsysConfig(application=this,
            applicationCode="EMS98-029BE",
            verboseConsoleLoggingEnabled= true)
        Emarsys.setup(config)

        EmarsysInboxConfig.actionEventHandler = { _, eventName, payload ->
            Log.i("PNP-INBOX", "eventName: $eventName, payload: $payload")
        }
        EmarsysInboxConfig.actionButtonStyler = { button ->
            button.setBackgroundColor(Color.WHITE)
            button.cornerRadius = 10
        }

        upgradeSecurityProvider()
    }

    private fun upgradeSecurityProvider() {
        ProviderInstaller.installIfNeededAsync(this, object :
            ProviderInstaller.ProviderInstallListener {
            override fun onProviderInstalled() {

            }
            override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
                GoogleApiAvailability.getInstance().showErrorNotification(this@InboxSampleApplication, errorCode)
            }
        })
    }
}