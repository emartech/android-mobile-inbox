package com.emarsys.pnp.inbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.emarsys.plugnplay.inbox.R

class EmarsysInboxActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ems_inbox_fragment_holder)
    }
}