package com.emarsys.pnp.inbox

import android.os.Handler
import android.os.Looper
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
                    message.updatePinnedInListView(false)
                }
            }
        } else {
            Emarsys.messageInbox.addTag(EmarsysInboxMessage.pinnedTag, message.id) {
                if (it == null) {
                    message.updatePinnedInListView(true)
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
                }
            }
        }
    }

    fun seen(message: EmarsysInboxMessage) {
        if (!message.isSeen()) {
            Emarsys.messageInbox.addTag(EmarsysInboxMessage.seenTag, message.id) {
                if (it == null) {
                    message.tags.add(EmarsysInboxMessage.seenTag)
                }
            }
        }
    }

    fun refresh() {
        isRefreshing.value = true
        Emarsys.messageInbox.fetchMessages { messages ->
            isRefreshing.value = false
            messages.result?.let { notificationStatus ->
                this.messages.postValue(notificationStatus.messages
                    .filter { !(it.tags?.contains(EmarsysInboxMessage.deletedTag) ?: false) }
                    .map { EmarsysInboxMessage(it) } as MutableList)
            }
            messages.errorCause?.let { cause ->
                error.value = "Error fetching messages: ${cause.message}"
            }
        }
    }

    private fun EmarsysInboxMessage.updatePinnedInListView(add: Boolean) {
        val newMessage = EmarsysInboxMessage.copy(this)
        if (add) {
            newMessage.tags.add(EmarsysInboxMessage.pinnedTag)
        } else {
            newMessage.tags.remove(EmarsysInboxMessage.pinnedTag)
        }
        val list = messages.value?.toMutableList()
        list?.indexOfFirst { it.id == this.id }?.let {
            list[it] = newMessage
            messages.postValue(list)
        }
        Handler(Looper.getMainLooper()).postDelayed({
            Emarsys.messageInbox.fetchMessages { }
        }, 3000)
    }

    private fun EmarsysInboxMessage.removeFromListView() {
        val list = messages.value?.toMutableList()
        list?.indexOfFirst { it.id == this.id }?.let {
            list.removeAt(it)
            messages.postValue(list)
        }
    }
}
