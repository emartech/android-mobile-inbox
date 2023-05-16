package com.emarsys.pnp.inbox.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emarsys.Emarsys
import com.emarsys.plugnplay.inbox.R

class InboxSampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.ems_inbox_fragment_holder)

        Emarsys.setContact(100010824, "biancalui")
    }
}
