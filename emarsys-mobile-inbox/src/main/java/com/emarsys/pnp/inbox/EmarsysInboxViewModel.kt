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
        if (message.isPinned)
            Emarsys.messageInbox.removeTag("pinned", message.id)
        else
            Emarsys.messageInbox.addTag("pinned", message.id)
        message.copy(isPinned = !message.isPinned).updateInListView()
    }

    fun removed(listPosition: Int) {
        messages.value?.get(listPosition)?.let {
            Emarsys.messageInbox.addTag("deleted", it.id)
            it.removeFromListView()
        }
    }

    fun opened(message: EmarsysInboxMessage) {
        if (!message.isOpened) {
            Emarsys.messageInbox.addTag("opened", message.id)
        }
        selectedItem.value = messages.value?.indexOfFirst { it.id == message.id }
    }


    fun refresh() {
        isRefreshing.value = true
        Emarsys.messageInbox.fetchMessages {
            isRefreshing.value = false
            it.result?.let { notificationStatus ->
                messages.value = notificationStatus.messages
                    .filter { !(it.tags?.contains("deleted") ?: false) }
                    .sortedByDescending { it.receivedAt }
                    .map { EmarsysInboxMessage(it) } as MutableList
            }
            it.errorCause?.let { cause ->
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


