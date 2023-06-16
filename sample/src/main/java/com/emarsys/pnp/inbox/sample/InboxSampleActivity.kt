package com.emarsys.pnp.inbox.sample

import android.os.Bundle
import com.emarsys.Emarsys
import com.emarsys.pnp.inbox.EmarsysInboxActivity

class InboxSampleActivity : EmarsysInboxActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Emarsys.setContact(100010824, "biancalui")
    }
}
