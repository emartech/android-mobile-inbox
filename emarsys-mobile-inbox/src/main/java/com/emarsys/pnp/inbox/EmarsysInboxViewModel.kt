package com.emarsys.pnp.inbox

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emarsys.Emarsys

class EmarsysInboxViewModel() : ViewModel() {
    val error = MediatorLiveData<String?>()
    val selectedItem = MutableLiveData<Int?>()
    val isRefreshing = MutableLiveData<Boolean>()
    val messages: MutableLiveData<List<EmarsysInboxMessage>> by lazy {
        MutableLiveData<List<EmarsysInboxMessage>>().also {
            refresh()
        }
    }

    fun pin(message: EmarsysInboxMessage) {
        if (message.isPinned()) {
            Emarsys.messageInbox.removeTag(EmarsysInboxMessage.pinnedTag, message.id) {
                if (it == null) {
                    message.tags.remove(EmarsysInboxMessage.pinnedTag)
                    message.updateInListView()
                }
            }
        } else {
            Emarsys.messageInbox.addTag(EmarsysInboxMessage.pinnedTag, message.id) {
                if (it == null) {
                    message.tags.add(EmarsysInboxMessage.pinnedTag)
                    message.updateInListView()
                }
            }
        }
    }

    fun removed(listPosition: Int) {
        messages.value?.get(listPosition)?.let {
            Emarsys.messageInbox.addTag(EmarsysInboxMessage.deletedTag, it.id)
            it.removeFromListView()
        }
    }

    fun opened(message: EmarsysInboxMessage) {
        if (!message.isOpened()) {
            Emarsys.messageInbox.addTag(EmarsysInboxMessage.openedTag, message.id) {
                if (it == null) {
                    message.tags.add(EmarsysInboxMessage.openedTag)
                    message.updateInListView()
                }
            }
        }
        selectedItem.value = messages.value?.indexOfFirst { it.id == message.id }
    }

    fun seen(message: EmarsysInboxMessage) {
        if (!message.isSeen()) {
            Emarsys.messageInbox.addTag(EmarsysInboxMessage.seenTag, message.id)
        }
    }

    fun refresh() {
        isRefreshing.value = true
        Emarsys.messageInbox.fetchMessages { messages ->
            isRefreshing.value = false
            messages.result?.let { notificationStatus ->
                this.messages.value = notificationStatus.messages
                    .filter { !(it.tags?.contains(EmarsysInboxMessage.deletedTag) ?: false) }
                    .sortedByDescending { it.receivedAt }
                    .sortedByDescending { it.tags?.contains(EmarsysInboxMessage.pinnedTag) }
                    .map { EmarsysInboxMessage(it) } as MutableList
            }
            messages.errorCause?.let { cause ->
                error.value = "Error fetching messages: ${cause.message}"
            }
        }
    }

    private fun EmarsysInboxMessage.updateInListView() {
        // RecyclerView needs a new list to compare to.
        val list = messages.value?.toMutableList()
        list?.indexOfFirst { it.id == this.id }?.let { list.set(it, this); messages.value = list }
    }

    private fun EmarsysInboxMessage.removeFromListView() {
        val list = messages.value?.toMutableList()
        list?.indexOfFirst { it.id == this.id }?.let { list.removeAt(it); messages.value = list }
    }
}
