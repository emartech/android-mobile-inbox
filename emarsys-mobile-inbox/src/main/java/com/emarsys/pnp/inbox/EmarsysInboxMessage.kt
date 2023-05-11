package com.emarsys.pnp.inbox

import android.os.Parcelable
import android.text.format.DateFormat
import com.emarsys.mobileengage.api.action.ActionModel
import com.emarsys.mobileengage.api.inbox.Message
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class EmarsysInboxMessage(
    @IgnoredOnParcel private val message: @RawValue Message,
    val id: String = message.id,
    val campaignId: String = message.campaignId,
    val collapseId: String? = message.collapseId,
    val title: String = message.title,
    val body: String = message.body,
    val imageUrl: String? = message.imageUrl,
    val receivedAt: String = DateFormat.format(dateFormat, message.receivedAt * 1000) as String,
    val updatedAt: String? = message.updatedAt?.times(1000)?.let {
        DateFormat.format(dateFormat, it)
    } as String,
    var tags: MutableList<String> = message.tags?.toMutableList() ?: mutableListOf(),
    val properties: Map<String, String> = message.properties ?: mapOf(),
    @IgnoredOnParcel val actions: @RawValue List<ActionModel>? = message.actions
) : Parcelable {

    companion object {
        const val dateFormat = "HH:mm - dd MMM yyyy"
        const val openedTag = "opened"
        const val seenTag = "seen"
        const val pinnedTag = "pinned"
        const val deletedTag = "deleted"
        const val highPriorityTag = "high"
    }

    fun isOpened(): Boolean {
        return tags.contains(openedTag)
    }

    fun isSeen(): Boolean {
        return tags.contains(seenTag)
    }

    fun isPinned(): Boolean {
        return tags.contains(pinnedTag)
    }

    fun isDeleted(): Boolean {
        return tags.contains(deletedTag)
    }

    fun isHighPriority(): Boolean {
        return tags.contains(highPriorityTag)
    }
}
