package com.emarsys.pnp.inbox

import android.text.format.DateFormat
import com.emarsys.mobileengage.api.inbox.Message

data class EmarsysInboxMessage(
    private val message: Message,
    val id: String = message.id,
    val title: String = message.title,
    val body: String = message.body,
    val imageUrl: String? = message.imageUrl,
    val receivedAt: String = DateFormat.format(dateFormat, message.receivedAt * 1000) as String,
    val isOpened: Boolean = message.tags?.contains("opened") ?: false,
    val isSeen: Boolean = message.tags?.contains("seen") ?: false,
    val isPinned: Boolean = message.tags?.contains("pinned") ?: false,
    val isDeleted: Boolean = message.tags?.contains("deleted") ?: false,
    val updatedAt: String = DateFormat.format(dateFormat, message.updatedAt * 1000) as String,
    val properties: Map<String, String> = message.properties ?: mapOf(),
    val ttl: Int? = message.ttl
) {


    companion object {
        var dateFormat = "yyyy-MM-dd kk:mm"
    }
}